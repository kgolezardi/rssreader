package ir.sahab.nimbo.dose;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public final String USER;
    public final String PASS;
    public final String SQL_URL;
    public final String CONNECTION_SETTINGS;
    public final String DB_NAME;
    public final String FORMATS;

    private static Config ourInstance = new Config();

    public static Config getInstance() {
        return ourInstance;
    }

    private Config() {
        String resourceName = "config.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CONNECTION_SETTINGS = props.getProperty("con_settings");
        DB_NAME = props.getProperty("db_name");
        SQL_URL = props.getProperty("sql_url");
        USER = props.getProperty("user");
        PASS = props.getProperty("pass");

        FORMATS = props.getProperty("formats");
    }
}
