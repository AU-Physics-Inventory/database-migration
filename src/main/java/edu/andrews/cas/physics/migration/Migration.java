package edu.andrews.cas.physics.migration;

import com.beust.jcommander.JCommander;
import com.mongodb.client.model.Updates;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import edu.andrews.cas.physics.cli.Args;
import edu.andrews.cas.physics.inventory.model.mongodb.accountability.AccountabilityReports;
import edu.andrews.cas.physics.inventory.model.mongodb.accountability.MissingReport;
import edu.andrews.cas.physics.inventory.model.mongodb.asset.*;
import edu.andrews.cas.physics.inventory.model.mongodb.group.Group;
import edu.andrews.cas.physics.inventory.model.mongodb.maintenance.CalibrationDetails;
import edu.andrews.cas.physics.inventory.model.mongodb.maintenance.MaintenanceEvent;
import edu.andrews.cas.physics.inventory.model.mongodb.maintenance.MaintenanceRecord;
import edu.andrews.cas.physics.inventory.model.mongodb.maintenance.Status;
import edu.andrews.cas.physics.inventory.model.mysql.Equipment;
import edu.andrews.cas.physics.inventory.model.mysql.Maintenance;
import edu.andrews.cas.physics.measurement.Quantity;
import edu.andrews.cas.physics.measurement.Unit;
import edu.andrews.cas.physics.migration.database.MongoDBDataSource;
import edu.andrews.cas.physics.migration.database.MySQLDataSource;
import lombok.NonNull;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.mongodb.client.model.Filters.eq;


public class Migration {
    private static final Properties config = new Properties();
    private static final Logger logger = LogManager.getLogger();
    private static final String receiptsFilePath = String.format(".%s%sreceipts.map", System.getProperty("user.home"), File.separator);
    private static final String imagesFilePath = String.format(".%s%simages.map", System.getProperty("user.home"), File.separator);

    private static ConcurrentHashMap<Integer, String> receipts = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, List<String>> images = new ConcurrentHashMap<>();
    private static MongoDatabase mongodb;

    public static void main(String[] argv) throws Exception {
        Args args = new Args();
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);

        config.load(ClassLoader.getSystemResourceAsStream("config.properties"));

        MongoClient client = MongoDBDataSource.getClient();
        mongodb = client.getDatabase(config.getProperty("mongodb.db"));

        String username = config.getProperty("mysql.user");
        String DB_HOST = config.getProperty("mysql.host");
        String DB_PORT = config.getProperty("mysql.port");
        String DB_NAME = config.getProperty("mysql.db");
        String DB_OPTIONS = config.getProperty("mysql.options");
        String DB_CONNECTION_URL = String.format("jdbc:mysql://%s:%s/%s%s", DB_HOST, DB_PORT, DB_NAME, DB_OPTIONS);

        logger.printf(Level.INFO, "Connecting to database via %s\nUsername: \t%s\nPassword: \t***********************", DB_CONNECTION_URL, username);
        Thread.sleep(2000);
        System.out.println("........");
        Thread.sleep(1000);
        System.out.println("........");


        File receiptsFile = new File(args.getReceiptsPath() == null ? receiptsFilePath : args.getReceiptsPath());
        File imagesFile = new File(args.getImagesPath() == null ? imagesFilePath : args.getImagesPath());

        if (!receiptsFile.exists() || !imagesFile.exists()) migrateImagesAndReceipts();
        else {
            FileInputStream fis = new FileInputStream(receiptsFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            receipts = (ConcurrentHashMap<Integer, String>) ois.readObject();
            ois.close();
            fis.close();

            fis = new FileInputStream(imagesFile);
            ois = new ObjectInputStream(fis);
            images = (ConcurrentHashMap<Integer, List<String>>) ois.readObject();
            ois.close();
            fis.close();
        }
        migrateManuals();
        migrateAssets();
        migrateSets();
        migrateGroups();
        migrateLabs();
    }

    private static void migrateManuals() throws SQLException, InterruptedException, URISyntaxException {
        Set<String> manualNames = Collections.synchronizedSet(new HashSet<>());
        ArrayList<Integer> identityNos = new ArrayList<>();

        Connection con = MySQLDataSource.getConnection();
        if (!con.isValid(30)) throw new SQLException("Unable to connect to database.");
        logger.info("Connection established successfully!");
        Thread.sleep(4000);
        logger.info("[START MIGRATION] Manuals");
        logger.info("Retrieving IDs...");

        PreparedStatement retrieveIdentityNos = con.prepareStatement("SELECT record_locator FROM manuals;");
        ResultSet resultSet = retrieveIdentityNos.executeQuery();

        while (resultSet.next()) {
            identityNos.add(resultSet.getInt("id"));
        }

        resultSet.close();
        retrieveIdentityNos.close();
        con.close();

        URI endpoint = new URI(config.getProperty("spaces.endpoint"));
        final String spaceName = config.getProperty("spaces.name");
        final String secret = config.getProperty("spaces.secret");
        final String key = config.getProperty("spaces.key");
        S3AsyncClient spaces = S3AsyncClient.builder()
                .endpointOverride(endpoint)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(key, secret)))
                .build();

        MongoCollection<Manual> collection = mongodb.getCollection("manuals", Manual.class);

        identityNos.parallelStream().forEach(identityNo -> {
            logger.info("Migrating manual for ID {}", identityNo);
            try (Connection c = MySQLDataSource.getConnection(); PreparedStatement p = c.prepareStatement("SELECT manual FROM manuals WHERE record_locator = ?;")) {
                p.setInt(identityNo, 1);
                ResultSet r = p.executeQuery();
                InputStream is = r.next() ? r.getBinaryStream("receipt") : null;
                if (is != null) {
                    Pair<File, String> fileNamePair = generateFile(is, manualNames, ".pdf");
                    File manual = fileNamePair.getLeft();
                    String manualName = fileNamePair.getRight();
                    spaces.putObject(PutObjectRequest.builder().bucket(String.format("%s/manuals", spaceName)).key(manualName).build(), AsyncRequestBody.fromFile(manual));
                    collection.insertOne(new Manual(identityNo, false, manualName));
                    manual.delete();
                    is.close();
                }
                r.close();
            } catch (SQLException | IOException e) {
                logger.error(String.format("ERROR ON IDENTITY NO %d", identityNo), e);
                e.printStackTrace();
            }
        });

        logger.info("[END MIGRATION] Manuals");
    }

    private static void migrateLabs() throws SQLException, InterruptedException {
        ArrayList<edu.andrews.cas.physics.inventory.model.mysql.LabCourses> labCourses = new ArrayList<>();

        Connection con = MySQLDataSource.getConnection();
        if (!con.isValid(30)) throw new SQLException("Unable to connect to database.");
        logger.info("Connection established successfully!");
        Thread.sleep(4000);
        logger.info("[START MIGRATION] Labs");
        logger.info("Retrieving lab courses...");

        PreparedStatement retrieveLabCourses = con.prepareStatement("SELECT * FROM lab_courses;");
        ResultSet resultSet = retrieveLabCourses.executeQuery();

        while (resultSet.next()) {
            labCourses.add(new edu.andrews.cas.physics.inventory.model.mysql.LabCourses(resultSet.getInt("id"), resultSet.getString("course_name"), resultSet.getString("course_number")));
        }

        resultSet.close();
        retrieveLabCourses.close();
        con.close();

        MongoCollection<Group> collection = mongodb.getCollection("labs", Group.class);

        // TODO IMPLEMENT REST OF LABS MIGRATION
    }

    private static void migrateGroups() throws SQLException, InterruptedException {
        ArrayList<edu.andrews.cas.physics.inventory.model.mysql.Group> groups = new ArrayList<>();

        Connection con = MySQLDataSource.getConnection();
        if (!con.isValid(30)) throw new SQLException("Unable to connect to database.");
        logger.info("Connection established successfully!");
        Thread.sleep(4000);
        logger.info("[START MIGRATION] Groups");
        logger.info("Retrieving groups...");

        PreparedStatement retrieveGroups = con.prepareStatement("SELECT * FROM groups;");
        ResultSet resultSet = retrieveGroups.executeQuery();

        while (resultSet.next()) {
            groups.add(new edu.andrews.cas.physics.inventory.model.mysql.Group(resultSet.getInt("id"), resultSet.getString("name")));
        }

        resultSet.close();
        retrieveGroups.close();
        con.close();

        MongoCollection<Group> collection = mongodb.getCollection("groups", Group.class);

        groups.parallelStream().forEach(g -> {
            logger.info("Migrating Group ID {}", g.id());
            try (Connection c = MySQLDataSource.getConnection();
                 PreparedStatement p1 = c.prepareStatement("SELECT asset_id FROM group_records WHERE group_id = ?;");
                 PreparedStatement p2 = c.prepareStatement("SELECT asset_record_number FROM group_records WHERE group_id = ?;")) {

                Group group = new Group(g.id(), g.name());

                p1.setInt(1, g.id());
                p2.setInt(1, g.id());

                logger.info("Retrieving assets tied to Group ID {}", g.id());
                ResultSet r1 = p1.executeQuery();
                while (r1.next()) group.addAsset(r1.getInt("asset_id"));
                r1.close();

                logger.info("Retrieving Identity Numbers tied to Group ID {}", g.id());
                ResultSet r2 = p2.executeQuery();
                while (r2.next()) group.addIdentityNo(r2.getInt("asset_record_number"));
                r2.close();

                collection.insertOne(group);
            } catch (SQLException e) {
                logger.error(String.format("Error on Group ID %s", g.id()), e);
            }
            logger.info("[END MIGRATION] Groups");
        });
    }

    private static void migrateSets() throws SQLException, InterruptedException {
        ArrayList<edu.andrews.cas.physics.inventory.model.mysql.Set> sets = new ArrayList<>();

        Connection con = MySQLDataSource.getConnection();
        if (!con.isValid(30)) throw new SQLException("Unable to connect to database.");
        logger.info("Connection established successfully!");
        Thread.sleep(4000);
        logger.info("[START MIGRATION] Sets");
        logger.info("Retrieving sets...");

        PreparedStatement retrieveSets = con.prepareStatement("SELECT * FROM sets;");
        ResultSet resultSet = retrieveSets.executeQuery();

        while (resultSet.next()) {
            sets.add(new edu.andrews.cas.physics.inventory.model.mysql.Set(resultSet.getInt("id"), resultSet.getString("name")));
        }

        resultSet.close();
        retrieveSets.close();
        con.close();

        MongoCollection<edu.andrews.cas.physics.inventory.model.mongodb.set.Set> collection = mongodb.getCollection("sets", edu.andrews.cas.physics.inventory.model.mongodb.set.Set.class);

        sets.parallelStream().forEach(s -> {
            logger.info("Migrating Set ID {}", s.id());
            try (Connection c = MySQLDataSource.getConnection();
                 PreparedStatement p1 = c.prepareStatement("SELECT asset_id FROM set_records WHERE set_id = ?;");
                 PreparedStatement p2 = c.prepareStatement("SELECT asset_record_number FROM set_records WHERE set_id = ?;");
                 PreparedStatement p3 = c.prepareStatement("SELECT collection_group_id FROM set_records WHERE set_id = ?;")) {

                edu.andrews.cas.physics.inventory.model.mongodb.set.Set set = new edu.andrews.cas.physics.inventory.model.mongodb.set.Set(s.id(), s.name());

                p1.setInt(1, s.id());
                p2.setInt(1, s.id());
                p3.setInt(1, s.id());

                logger.info("Retrieving assets tied to Set ID {}", s.id());
                ResultSet r1 = p1.executeQuery();
                while (r1.next()) set.addAsset(r1.getInt("asset_id"));
                r1.close();

                logger.info("Retrieving Identity Numbers tied to Set ID {}", s.id());
                ResultSet r2 = p2.executeQuery();
                while (r2.next()) set.addIdentityNo(r2.getInt("asset_record_number"));
                r2.close();

                logger.info("Retrieving groups tied to Set ID {}", s.id());
                ResultSet r3 = p3.executeQuery();
                while (r3.next()) set.addGroup(r3.getInt("collection_group_id"));
                r3.close();

                collection.insertOne(set);
            } catch (SQLException e) {
                logger.error(String.format("Error on Set ID %s", s.id()), e);
            }
            logger.info("[END MIGRATION] Sets");
        });
    }

    private static void migrateImagesAndReceipts() throws InterruptedException, SQLException, IOException, URISyntaxException {
        Set<String> receiptNames = Collections.synchronizedSet(new HashSet<>());
        Set<String> imageNames = Collections.synchronizedSet(new HashSet<>());
        ArrayList<Integer> ids = new ArrayList<>();

        Connection con = MySQLDataSource.getConnection();
        if (!con.isValid(30)) throw new SQLException("Unable to connect to database.");
        logger.info("Connection established successfully!");
        Thread.sleep(4000);
        logger.info("[START MIGRATION] Images and Receipts");
        logger.info("Retrieving IDs...");

        PreparedStatement retrieveIDs = con.prepareStatement("SELECT id FROM images_and_receipts;");
        ResultSet resultSet = retrieveIDs.executeQuery();

        while (resultSet.next()) {
            ids.add(resultSet.getInt("id"));
        }

        resultSet.close();
        retrieveIDs.close();
        con.close();

        URI endpoint = new URI(config.getProperty("spaces.endpoint"));
        final String spaceName = config.getProperty("spaces.name");
        final String secret = config.getProperty("spaces.secret");
        final String key = config.getProperty("spaces.key");
        S3AsyncClient spaces = S3AsyncClient.builder()
                .endpointOverride(endpoint)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(key, secret)))
                .build();

        ids.parallelStream().forEach(id -> {
            logger.info("Migrating ID {}", id);
            try (Connection c = MySQLDataSource.getConnection();
                 PreparedStatement p1 = c.prepareStatement("SELECT receipt FROM images_and_receipts WHERE id = ?;");
                 PreparedStatement p2 = c.prepareStatement("SELECT image_one FROM images_and_receipts WHERE id = ?;");
                 PreparedStatement p3 = c.prepareStatement("SELECT image_two FROM images_and_receipts WHERE id = ?;");
                 PreparedStatement p4 = c.prepareStatement("SELECT image_three FROM images_and_receipts WHERE id = ?;");
                 PreparedStatement p5 = c.prepareStatement("SELECT image_four FROM images_and_receipts WHERE id = ?;")) {

                p1.setInt(id, 1);
                p2.setInt(id, 1);
                p3.setInt(id, 1);
                p4.setInt(id, 1);
                p5.setInt(id, 1);

                ResultSet r1 = p1.executeQuery();
                InputStream is1 = r1.next() ? r1.getBinaryStream("receipt") : null;

                logger.info("Migrating receipt for ID {}", id);
                if (is1 != null) {
                    Pair<File, String> fileNamePair = generateFile(is1, receiptNames, ".pdf");
                    File receipt = fileNamePair.getLeft();
                    String receiptName = fileNamePair.getRight();
                    spaces.putObject(PutObjectRequest.builder().bucket(String.format("%s/receipts", spaceName)).key(receiptName).build(), AsyncRequestBody.fromFile(receipt));
                    receipts.put(id, receiptName);
                    receipt.delete();
                    is1.close();
                }
                r1.close();

                List<String> imgs = new ArrayList<>();

                logger.info("Migrating image 1 for ID {}", id);
                ResultSet r2 = p2.executeQuery();
                InputStream is2 = r2.next() ? r2.getBinaryStream("image_one") : null;
                migrateImageHelper(imageNames, spaceName, spaces, imgs, is2);
                r2.close();

                logger.info("Migrating image 2 for ID {}", id);
                ResultSet r3 = p3.executeQuery();
                InputStream is3 = r3.next() ? r3.getBinaryStream("image_two") : null;
                migrateImageHelper(imageNames, spaceName, spaces, imgs, is3);
                r3.close();

                logger.info("Migrating image 3 for ID {}", id);
                ResultSet r4 = p4.executeQuery();
                InputStream is4 = r4.next() ? r4.getBinaryStream("image_three") : null;
                migrateImageHelper(imageNames, spaceName, spaces, imgs, is4);
                r4.close();

                logger.info("Migrating image 4 for ID {}", id);
                ResultSet r5 = p5.executeQuery();
                InputStream is5 = r5.next() ? r5.getBinaryStream("image_four") : null;
                migrateImageHelper(imageNames, spaceName, spaces, imgs, is5);
                r4.close();

                images.put(id, imgs);
            } catch (SQLException | IOException e) {
                logger.error(String.format("ERROR ON ID %d", id), e);
                e.printStackTrace();
            }
        });

        logger.info("[END MIGRATION] Images and Receipts");
        logger.info("[BEGIN SERIALIZATION] Images and Receipts");

        File receiptsFile = new File(receiptsFilePath);
        FileOutputStream fos = new FileOutputStream(receiptsFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(receipts);
        oos.close();
        fos.close();

        logger.info("Receipts successfully serialized to {}", receiptsFile.getAbsolutePath());

        File imagesFile = new File(imagesFilePath);
        fos = new FileOutputStream(imagesFile);
        oos = new ObjectOutputStream(fos);
        oos.writeObject(images);
        oos.close();
        fos.close();

       logger.info("Images successfully serialized to {}", imagesFile.getAbsolutePath());
       logger.info("[END SERIALIZATION] Images and Receipts");
    }

    private static void migrateImageHelper(Set<String> imageNames, String spaceName, S3AsyncClient spaces, List<String> imgs, InputStream is) throws IOException {
        if (is != null) {
            Pair<File, String> fileNamePair = generateFile(is, imageNames, null);
            File image = fileNamePair.getLeft();
            String imageName = fileNamePair.getRight();
            spaces.putObject(PutObjectRequest.builder().bucket(String.format("%s/images", spaceName)).key(imageName).build(), AsyncRequestBody.fromFile(image));
            imgs.add(imageName);
            image.delete();
            is.close();
        }
    }

    private static void readInputStreamToFile(@NonNull InputStream imageIS, File image) throws IOException {
        FileOutputStream os = new FileOutputStream(image);
        int read;
        byte[] bytes = new byte[1024];
        while ((read = imageIS.read(bytes)) != -1) os.write(bytes, 0, read);
        os.close();
        imageIS.close();
    }

    private static Pair<File, String> generateFile(@NonNull InputStream is, @NonNull Set<String> set, String fileExt) throws IOException {
        String filename;
        do {
            filename = RandomStringUtils.randomAlphanumeric(8, 20);
        } while (set.contains(filename));
        set.add(filename);

        File file = File.createTempFile(filename, fileExt);
        readInputStreamToFile(is, file);

        if (fileExt != null) filename = filename.concat(fileExt);
        return new ImmutablePair<>(file, filename);
    }

    public static void migrateAssets() throws SQLException, InterruptedException {
        Connection con = MySQLDataSource.getConnection();
        if (!con.isValid(30)) throw new SQLException("Unable to connect to database.");
        logger.info("Connection established successfully!");
        Thread.sleep(4000);
        logger.info("[START MIGRATION] Assets");
        logger.info("Retrieving IDs...");

        ArrayList<Integer> ids = new ArrayList<>();

        PreparedStatement retrieveIDs = con.prepareStatement("SELECT id FROM equipment;");
        ResultSet resultSet = retrieveIDs.executeQuery();

        while (resultSet.next()) {
            ids.add(resultSet.getInt("id"));
        }

        resultSet.close();
        retrieveIDs.close();
        con.close();

        MongoCollection<Asset> assetsCollection = mongodb.getCollection("assets", Asset.class);
        MongoCollection<Manual> manualsCollection = mongodb.getCollection("manuals", Manual.class);

        ids.parallelStream().forEach(id -> {
            logger.info("Migrating ID {}", id);
            try {
                Equipment e = fetchEquipment(id);
                String name = e.getItem_name();
                String location = e.getLocation();
                List<String> keywords = Arrays.stream(e.getKeywords().split(";")).toList();
                List<String> imgs = images.get(e.getId());
                Integer identityNo = e.getRecord_locator() != -1 ? e.getRecord_locator() : null;
                String AUInventoryNo = e.getAu_inventory();
                boolean isConsumable = false;
                boolean hardCopyManualAvailable = e.getHard_copy_available() == 1;

                String brand = e.getBrand();
                String model = e.getModel();
                String partNo = e.getPart();
                String serialNo = e.getSerial_number();
                ManufacturerInfo mfrInfo = new ManufacturerInfo(brand, model, partNo, serialNo);

                Vendor vendor = e.getVendor() == null ? null : new Vendor(e.getVendor(), null);
                LocalDate purchaseDate = e.getPurchase_date();
                double cost = parsePrice(e.getPurchase_amount());
                double unitPrice = parsePrice(e.getUnit_price());
                Quantity purchaseQuantity = parseQuantity(e.getQuantity());
                String receipt = receipts.get(e.getId());
                AssetPurchase purchase = new AssetPurchase(vendor, purchaseDate, cost, unitPrice, purchaseQuantity, null, receipt);
                List<AssetPurchase> purchases = new ArrayList<>();
                purchases.add(purchase);

                Quantity totalQuantity = purchaseQuantity;

                Quantity quantityMissing = parseQuantity(e.getQuantity_missing());
                MissingReport missingReport = new MissingReport(quantityMissing, e.getDate_reported_missing(), e.getReported_missing_by());
                AccountabilityReports accountabilityReports = new AccountabilityReports();
                accountabilityReports.setQuantityMissing(quantityMissing);
                accountabilityReports.addMissingReport(missingReport);

                Maintenance m = fetchMaintenance(e.getId());
                MaintenanceRecord maintenanceRecord;
                if (m != null) {
                    MaintenanceEvent currentStatus = new MaintenanceEvent(Status.lookup(m.getRepair_status()), m.getStatus_change_date());
                    List<MaintenanceEvent> history = Arrays.stream(m.getStatus_history().split(";")).map(s -> {
                        String[] split = s.split(":");
                        Status status = Status.lookup(split[0].strip());
                        LocalDate date = LocalDate.parse(split[1].strip());
                        return new MaintenanceEvent(status, date);
                    }).toList();
                    List<LocalDate> calibrationHistory = Arrays.stream(m.getCalibration_history().split(";")).map(LocalDate::parse).toList();
                    CalibrationDetails details = new CalibrationDetails(m.getNext_calibration_date(), m.getLast_calibration_date(), null, calibrationHistory);
                    String notes = m.getNotes();
                    maintenanceRecord = new MaintenanceRecord(currentStatus, history, details, notes);
                } else maintenanceRecord = new MaintenanceRecord();

                String notes = e.getNotes();

                Asset asset = new Asset(id, name, location, keywords, imgs, identityNo, AUInventoryNo, isConsumable, mfrInfo, purchases, totalQuantity, accountabilityReports, maintenanceRecord, notes);
                assetsCollection.insertOne(asset);
                if (hardCopyManualAvailable)
                    manualsCollection.findOneAndUpdate(eq("identityNo", identityNo), Updates.set("hardcopy", true));
                logger.info("Finished migration of Asset #{}", id);
            } catch (SQLException e) {
                logger.error(String.format("ERROR ON ID %d", id), e);
                e.printStackTrace();
            }

            logger.info("[END MIGRATION] Assets");
        });
    }

    private static Equipment fetchEquipment(@NonNull Integer id) throws SQLException {
        logger.info("Fetching details for asset #{}", id);
        Connection c = MySQLDataSource.getConnection();

        PreparedStatement p = c.prepareStatement("SELECT * FROM equipment WHERE id = ?;");
        p.setInt(1, id);

        ResultSet r = p.executeQuery();

        Equipment e = new Equipment();
        e.setId(r.getInt("id"));
        e.setLocation(r.getString("location"));
        e.setItem_name(r.getString("item_name"));
        e.setKeywords(r.getString("keywords"));
        e.setBrand(r.getString("brand"));
        e.setModel(r.getString("model"));
        e.setPart(r.getString("part"));
        e.setSerial_number(r.getString("serial_number"));
        e.setAu_inventory(r.getString("au_inventory"));
        e.setQuantity(r.getString("quantity"));

        Date purchaseDate = r.getDate("purchase_date");
        e.setPurchase_date(purchaseDate != null ? purchaseDate.toLocalDate() : null);

        e.setPurchase_amount(r.getString("purchase_amount"));
        e.setMissing(r.getString("missing"));
        e.setQuantity_missing(r.getString("quantity_missing"));
        e.setRecord_locator(r.getInt("record_locator"));

        Date dateReportedMissing = r.getDate("date_reported_missing");
        e.setDate_reported_missing(dateReportedMissing != null ? dateReportedMissing.toLocalDate() : null);

        e.setReported_missing_by(r.getString("reported_missing_by"));
        e.setNotes(r.getString("notes"));
        e.setSoft_copy_available(r.getInt("soft_copy_available"));
        e.setHard_copy_available(r.getInt("hard_copy_available"));
        e.setReceipt_available(r.getInt("receipt_available"));
        e.setUnit_price(r.getString("unit_price"));
        e.setVendor(r.getString("vendor"));

        r.close();
        p.close();
        c.close();

        return e;
    }

    private static Maintenance fetchMaintenance(int id) throws SQLException {
        logger.info("Fetching maintenance properties for asset #{}", id);
        Connection c = MySQLDataSource.getConnection();

        PreparedStatement p = c.prepareStatement("SELECT * FROM maintenance WHERE id = ?;");
        p.setInt(1, id);

        ResultSet r = p.executeQuery();

        if (!r.next()) {
            r.close();
            p.close();
            c.close();
            return null;
        }

        Maintenance m = new Maintenance();
        m.setId(r.getInt("id"));
        m.setRepair_status(r.getString("repair_status"));

        Date statusChangeDate = r.getDate("status_change_date");
        Date lastCalibrationDate = r.getDate("last_calibration_date");
        Date nextCalibrationDate = r.getDate("next_calibration_date");

        m.setStatus_change_date(statusChangeDate != null ? statusChangeDate.toLocalDate() : null);
        m.setLast_calibration_date(lastCalibrationDate != null ? lastCalibrationDate.toLocalDate() : null);
        m.setNext_calibration_date(nextCalibrationDate != null ? nextCalibrationDate.toLocalDate() : null);

        m.setStatus_history(r.getString("status_history"));
        m.setCalibration_history(r.getString("calibration_history"));
        m.setNotes(r.getString("notes"));

        r.close();
        p.close();
        c.close();

        return m;
    }

    private static double parsePrice(String s) {
        try {
            return Double.parseDouble(s.replaceAll("\\$", "").strip());
        } catch (NumberFormatException e) {
            Scanner scanner = new Scanner(System.in);
            System.out.printf("Unable to parse double: %s\n", s);
            System.out.print("Enter double: ");
            return scanner.nextDouble();
        }
    }

    private static Quantity parseQuantity(String s) {
        try {
            double value = Double.parseDouble(s.split("[^0-9.]+")[0]);
            Unit unit = parseUnit(s.substring(String.valueOf(value).length()));
            return new Quantity(value, unit);
        } catch (NumberFormatException e) {
            Scanner scanner = new Scanner(System.in);
            System.out.printf("Unable to parse quantity: %s\n", s);
            System.out.print("Enter value: ");
            double value = scanner.nextDouble();
            System.out.print("Enter units: ");
            Unit unit = Unit.lookup(scanner.nextLine());
            return new Quantity(value, unit);
        }
    }

    private static Unit parseUnit(String s) {
        try {
            if (s.isBlank()) return Unit.UNITS;
            return Unit.lookup(s);
        } catch (IllegalArgumentException e) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(e.getMessage());
            System.out.print("Enter units: ");
            return Unit.lookup(scanner.nextLine());
        }
    }
}
