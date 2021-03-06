package edu.andrews.cas.physics.migration.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class MongoDBDataSource {
    private static final Logger logger = LogManager.getLogger();

    private static MongoClient client;

    static {
        try {
            Properties config = new Properties();
            config.load(ClassLoader.getSystemResourceAsStream("config.properties"));
            String USERNAME = config.getProperty("mongodb.user");
            String PASSWORD = config.getProperty("mongodb.pass");
            String AUTH_DB = config.getProperty("mongodb.user.auth.db");
            String DB_HOST = config.getProperty("mongodb.host");
            String DB_PROTOCOL = config.getProperty("mongodb.protocol");
            String DB_CONNECTION_URL = String.format("%s://%s:%s@%s/%s?authSource=%s&tls=true", DB_PROTOCOL, USERNAME, PASSWORD, DB_HOST, AUTH_DB, AUTH_DB);

            logger.info("Connecting to MongoDB using URL: {}", DB_CONNECTION_URL);

            System.setProperty("javax.net.ssl.keyStore", config.getProperty("truststore.path"));
            System.setProperty("javax.net.ssl.keyStorePassword", config.getProperty("truststore.pass"));
            System.setProperty("javax.net.ssl.trustStore", config.getProperty("truststore.path"));
            System.setProperty("javax.net.ssl.trustStorePassword", config.getProperty("truststore.pass"));

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyToSslSettings(builder -> builder.enabled(true))
                    .applyConnectionString(new ConnectionString(DB_CONNECTION_URL))
                    .build();

            client = MongoClients.create(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MongoDBDataSource() {
    }

    public static MongoClient getClient() {
        return client;
    }
}
