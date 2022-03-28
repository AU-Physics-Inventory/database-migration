package edu.andrews.cas.physics.migration.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLDataSource {
    private static final HikariConfig hikariConfig = new HikariConfig();
    private static HikariDataSource ds;

    static {
        try {
            Properties config = new Properties();
            config.load(ClassLoader.getSystemResourceAsStream("config.properties"));
            String username = config.getProperty("mysql.user");
            String password = config.getProperty("mysql.pass");
            String DB_HOST = config.getProperty("mysql.host");
            String DB_PORT = config.getProperty("mysql.port");
            String DB_NAME = config.getProperty("mysql.db");
            String DB_OPTIONS = config.getProperty("mysql.options");
            String DB_CONNECTION_URL = String.format("jdbc:mysql://%s:%s/%s%s", DB_HOST, DB_PORT, DB_NAME, DB_OPTIONS);
            hikariConfig.setJdbcUrl(DB_CONNECTION_URL);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.setConnectionTimeout(600000);
            hikariConfig.setKeepaliveTime(30000);
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
            ds = new HikariDataSource(hikariConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MySQLDataSource() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
