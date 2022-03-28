package edu.andrews.cas.physics.migration.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.IOException;
import java.util.Properties;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBDataSource {
    private static MongoClient client;
    private static final CodecRegistry pojoCodecRegistry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    static {
        try {
            Properties config = new Properties();
            config.load(ClassLoader.getSystemResourceAsStream("config.properties"));
            String USERNAME = config.getProperty("mongdb.user");
            String PASSWORD = config.getProperty("mongodb.pass");
            String AUTH_DB = config.getProperty("mongodb.user.auth.db");
            String DB_HOST = config.getProperty("mongodb.host");
            String DB_PORT = config.getProperty("mongodb.port");
            String DB_CONNECTION_URL = String.format("mongodb://%s:%s", DB_HOST, DB_PORT);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .codecRegistry(pojoCodecRegistry)
                    .credential(MongoCredential.createCredential(USERNAME, AUTH_DB, PASSWORD.toCharArray()))
                    .applyConnectionString(new ConnectionString(DB_CONNECTION_URL))
                    .build();
            client = MongoClients.create(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MongoDBDataSource() {}

    public static MongoClient getClient() {
        return client;
    }
}
