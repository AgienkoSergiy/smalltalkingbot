package ua.com.integrity.smalltalkingbot.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.h2.tools.RunScript;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public class DBUtil {

    private static final String PROPERTY_FILE_PATH = "db.properties";

    private static DBUtil instance = null;

    private static BasicDataSource dataSource;

    private DBUtil() {
        initDataSource();
    }

    public static DBUtil getInstance() {
        if (instance == null) {
            synchronized (DBUtil.class) {
                if (instance == null) {
                    instance = new DBUtil();
                }
            }
        }
        return instance;
    }

    private void initDataSource() {
        if (dataSource != null) {
            return;
        }
        try (InputStream inputStream =
                     getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_PATH)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName(properties.getProperty("db.driver"));
            dataSource.setUrl(properties.getProperty("db.url"));
            dataSource.setUsername("");
            dataSource.setPassword("");

            initData();

        } catch (IOException e) {
            throw new RuntimeException("Property file reading error ", e);
        }
    }

    private void initData(){
        try {
            InputStream in = getClass().getResourceAsStream("/scripts.sql");
            BufferedReader input = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            RunScript.execute(dataSource.getConnection(), input);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        return conn;
    }
}
