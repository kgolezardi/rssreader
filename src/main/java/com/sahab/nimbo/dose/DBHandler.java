package com.sahab.nimbo.dose;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class DBHandler {
    private String SQL_URL;
    private String DB_URL;
    private String USER;
    private String PASS;
    private String DB_NAME;

    private static DBHandler ourInstance = new DBHandler();

    private Connection conn = null;
    // TODO: connection pool
    // TODO: hash primary key
    // TODO: command line interface
    // TODO: unit tests

    private DBHandler() {
        getConifgs();
        try {
            initDatabase();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            initTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static DBHandler getInstance() {
        return ourInstance;
    }

    private void getConifgs() {
        String resourceName = "db.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DB_NAME = props.getProperty("db_name");
        SQL_URL = props.getProperty("sql_url") + "?" + props.getProperty("con_settings");
        DB_URL = props.getProperty("sql_url") + DB_NAME + "?" +
                props.getProperty("con_settings");
        USER = props.getProperty("user");
        PASS = props.getProperty("pass");
    }

    private void initDatabase() {
        try (Connection tempConn = DriverManager.getConnection(SQL_URL, USER, PASS);
             Statement stmt = tempConn.createStatement()) {
            String sql = "CREATE DATABASE IF NOT EXISTS `" + DB_NAME + "` " +
                    "/*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_persian_ci */;\n";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initTables() {
        try (Statement stmt = conn.createStatement()) {
            String createSite = "CREATE TABLE IF NOT EXISTS `Sites` (\n" +
                    "  `name` varchar(45) COLLATE utf8mb4_persian_ci NOT NULL,\n" +
                    "  `link` varchar(200) COLLATE utf8mb4_persian_ci NOT NULL,\n" +
                    "  `tag` varchar(50) COLLATE utf8mb4_persian_ci NOT NULL,\n" +
                    "  `attribute` varchar(50) COLLATE utf8mb4_persian_ci DEFAULT NULL,\n" +
                    "  `attributeValue` varchar(50) COLLATE utf8mb4_persian_ci DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`name`),\n" +
                    "  UNIQUE KEY `link_UNIQUE` (`link`),\n" +
                    "  UNIQUE KEY `name_UNIQUE` (`name`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;\n";

            String createNews = "CREATE TABLE IF NOT EXISTS `News` (\n" +
                    "  `url` varchar(200) COLLATE utf8mb4_persian_ci NOT NULL,\n" +
                    "  `text` text COLLATE utf8mb4_persian_ci,\n" +
                    "  `title` varchar(300) COLLATE utf8mb4_persian_ci NOT NULL,\n" +
                    "  `pubTime` datetime NOT NULL,\n" +
                    "  `siteName` varchar(50) COLLATE utf8mb4_persian_ci NOT NULL,\n" +
                    "  PRIMARY KEY (`url`),\n" +
                    "  UNIQUE KEY `url_UNIQUE` (`url`),\n" +
                    "  KEY `siteName_idx` (`siteName`),\n" +
                    "  CONSTRAINT `siteName` FOREIGN KEY (`siteName`) REFERENCES `Sites` (`name`) " +
                    "      ON DELETE CASCADE ON UPDATE CASCADE\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;\n";

            stmt.executeUpdate(createSite);
            stmt.executeUpdate(createNews);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addSite(Site site) {
        String sql = "INSERT INTO Sites (name, link, tag, attribute, attributeValue) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, site.getAddress());
            stmt.setString(2, site.getFeedUrl());
            stmt.setString(3, site.getTag());
            stmt.setString(4, site.getAttribute());
            stmt.setString(5, site.getAttributeValue());
            stmt.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Site> allSites() {
        List<Site> sites = new ArrayList<Site>();
        try (Statement stmt = conn.createStatement()) {
            String sql = "SELECT name, link, tag, attribute, attributeValue FROM Sites";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("name");
                String link = rs.getString("link");
                String tag = rs.getString("tag");
                String attribute = rs.getString("attribute");
                String attributeValue = rs.getString("attributeValue");
                sites.add(new Site(name, link, tag, attribute, attributeValue));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return sites;
    }

    public boolean existsUrl(String url) {
        String sql = "SELECT url FROM News WHERE url=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, url);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return true;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;
    }

    public boolean addNews(News news) {
        String sql = "INSERT INTO News (url, text, title, pubTime, siteName) " +
                "VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, news.getUrl());
            stmt.setString(2, news.getText());
            stmt.setString(3, news.getTitle());
            stmt.setTimestamp(4, new java.sql.Timestamp(news.getDate().getTime()));
            stmt.setString(5, news.getSiteName());
            stmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
        return true;
    }

    public Site getSite(String name) {
        String sql = "SELECT name, link, tag, attribute, attributeValue FROM Sites " +
                "WHERE name=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String link = rs.getString("link");
                String tag = rs.getString("tag");
                String attribute = rs.getString("attribute");
                String attributeValue = rs.getString("attributeValue");
                return new Site(name, link, tag, attribute, attributeValue);
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }

    private static String escapeLikeString(String s) {
        s = s.replace("!", "!!")
            .replace("%", "!%")
            .replace("_", "!_")
            .replace("[", "![");
        return s;
    }

    public List<News> searchNews(String titleCon, String textCon) {
        List<News> news = new ArrayList<>();
        String sql = "SELECT url, text, title, pubTime, siteName FROM News " +
                "WHERE title LIKE ? ESCAPE '!'" +
                "AND text LIKE ? ESCAPE '!'";
        titleCon = "%" + escapeLikeString(titleCon) + "%";
        textCon = "%" + escapeLikeString(textCon) + "%";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titleCon);
            stmt.setString(2, textCon);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String url = rs.getString("url");
                String text = rs.getString("text");
                String title = rs.getString("title");
                java.util.Date date = rs.getTimestamp("pubTime");
                String siteName = rs.getString("siteName");
                news.add(new News(url, title, text, date, siteName));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return news;
    }

    public List<News> getNewsBySite(String siteName, int limit) {
        List<News> news = new ArrayList<>();
        String sql = "SELECT url, text, title, pubTime FROM News " +
                "WHERE siteName=? ORDER BY pubTime DESC LIMIT ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, siteName);
            stmt.setInt(2, limit);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String url = rs.getString("url");
                String text = rs.getString("text");
                String title = rs.getString("title");
                java.util.Date date = rs.getTimestamp("pubTime");
                news.add(new News(url, title, text, date, siteName));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return news;
    }

    public List<News> getNewsBySiteDate(String siteName, Date time) {
        List<News> news = new ArrayList<>();
        java.sql.Date sqlDate = new java.sql.Date(time.getTime());
        String sql = "SELECT url, text, title, pubTime FROM News " +
                "WHERE siteName=? AND DATE(pubTime)=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, siteName);
            stmt.setDate(2, sqlDate);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String url = rs.getString("url");
                String text = rs.getString("text");
                String title = rs.getString("title");
                java.util.Date date = rs.getTimestamp("pubTime");
                news.add(new News(url, title, text, date, siteName));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return news;
    }
}
