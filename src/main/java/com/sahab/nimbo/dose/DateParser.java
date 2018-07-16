package com.sahab.nimbo.dose.rss;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DateParser {
    List<String> formats = new ArrayList<>();

    private void getConifgs() {
        String resourceName = "dateParseFormat.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQL_URL = props.getProperty("sql_url") + "?" + props.getProperty("con_settings");
        DB_URL = props.getProperty("sql_url") + props.getProperty("db_name") + "?" +
                props.getProperty("con_settings");
        USER = props.getProperty("user");
        PASS = props.getProperty("pass");
    }

    private DateParser() {
        getConifgs();
    }
}
