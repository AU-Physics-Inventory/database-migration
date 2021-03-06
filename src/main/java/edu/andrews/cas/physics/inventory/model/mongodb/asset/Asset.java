package edu.andrews.cas.physics.inventory.model.mongodb.asset;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import edu.andrews.cas.physics.inventory.model.mongodb.accountability.AccountabilityReports;
import edu.andrews.cas.physics.inventory.model.mongodb.maintenance.MaintenanceRecord;
import edu.andrews.cas.physics.measurement.Quantity;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Asset implements DocumentConversion {
    private final ObjectId _id;
    private final String name;
    private final String location;
    private final List<String> keywords;
    private final List<String> images;
    private final Integer identityNo;
    private final String AUInventoryNo;
    private final boolean consumable;
    private final ManufacturerInfo manufacturerInfo;
    private final List<AssetPurchase> purchases;
    private final Quantity quantity;
    private final AccountabilityReports accountabilityReports;
    private final MaintenanceRecord maintenanceRecord;
    private final String notes;

    public Asset(String name, String location, Integer identityNo, String AUInventoryNo,
                 boolean consumable, ManufacturerInfo manufacturerInfo, Quantity quantity, MaintenanceRecord maintenanceRecord, String notes) {
        _id = null;
        this.name = name;
        this.location = location;
        this.keywords = new ArrayList<>();
        this.identityNo = identityNo;
        this.AUInventoryNo = AUInventoryNo;
        this.consumable = consumable;
        this.manufacturerInfo = manufacturerInfo;
        this.quantity = quantity;
        this.accountabilityReports = new AccountabilityReports();
        this.maintenanceRecord = maintenanceRecord;
        this.notes = notes;
        this.purchases = new ArrayList<>();
        this.images = new ArrayList<>();
    }

    public Asset(String name, String location, List<String> keywords, List<String> images, Integer identityNo, String AUInventoryNo,
                 boolean consumable, ManufacturerInfo manufacturerInfo, List<AssetPurchase> purchases,
                 Quantity quantity, AccountabilityReports accountabilityReports,
                 MaintenanceRecord maintenanceRecord, String notes) {
        _id = null;
        this.name = name;
        this.location = location;
        this.keywords = keywords;
        this.identityNo = identityNo;
        this.AUInventoryNo = AUInventoryNo;
        this.consumable = consumable;
        this.manufacturerInfo = manufacturerInfo;
        this.purchases = purchases;
        this.quantity = quantity;
        this.accountabilityReports = accountabilityReports;
        this.accountabilityReports.setAsset(this);
        this.maintenanceRecord = maintenanceRecord;
        this.notes = notes;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public Integer getIdentityNo() {
        return identityNo;
    }

    public String getAUInventoryNo() {
        return AUInventoryNo;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public ManufacturerInfo getManufacturerInfo() {
        return manufacturerInfo;
    }

    public List<AssetPurchase> getPurchases() {
        return purchases;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public AccountabilityReports getAccountabilityReports() {
        return accountabilityReports;
    }

    public MaintenanceRecord getMaintenanceRecord() {
        return maintenanceRecord;
    }

    public String getNotes() {
        return notes;
    }

    public void addPurchase(AssetPurchase purchase) {
        if (!this.purchases.contains(purchase)) this.purchases.add(purchase);
    }

    public ObjectId get_id() {
        return _id;
    }

    public void addKeyword(String keyword) {
        if (!this.keywords.contains(keyword)) this.keywords.add(keyword);
    }

    public List<String> getImages() {
        return images;
    }

    public void addImage(String img) {
        if (!this.images.contains(img)) images.add(img);
    }

    @Override
    public Document toDocument() {
        return new Document()
                .append("name", getName())
                .append("location", getLocation())
                .append("keywords", getKeywords())
                .append("mfrInfo", getManufacturerInfo().toDocument())
                .append("AUInventoryNo", getAUInventoryNo())
                .append("purchases", getPurchases().stream().map(AssetPurchase::toDocument).toList())
                .append("quantity", getQuantity().toDocument())
                .append("accountabilityReports", getAccountabilityReports().toDocument())
                .append("identityNo", getIdentityNo())
                .append("notes", getNotes())
                .append("maintenanceRecord", getMaintenanceRecord().toDocument())
                .append("consumable", isConsumable())
                .append("images", getImages());
    }
}
