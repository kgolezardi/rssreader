package com.sahab.nimbo.dose;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class DBHandler {
    private static DBHandler ourInstance = new DBHandler();

    static DBHandler getInstance() {
        return ourInstance;
    }

    static final private String DB_URL = "jdbc:mysql://localhost/NewsReader" +
            "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&" +
            "autoReconnect=true&useSSL=false";

    static final private String USER = "root";
    static final private String PASS = "";
    // TODO: use this values as properties
    // TODO: prepared statements

    private Connection conn = null;
    private Statement stmt = null;

    private DBHandler() {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean addSite(Site site) {
        try {
            String sql = "INSERT INTO Sites (name, link, tag, attribute, attributeValue) " +
                    "VALUES ('" +
                    site.getAddress() + "', '" +
                    site.getFeedUrl() + "', '" +
                    site.getTag() + "', '" +
                    site.getAttribute() + "', '" +
                    site.getAttributeValue() + "')";
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Site> allSites() {
        List<Site> sites = new ArrayList<Site>();
        try {
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

    public boolean existsURL(String url) {
        try {
            String sql = "SELECT url FROM News WHERE url='" + url + "'";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
                return true;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;
    }

    public boolean addNews(News news) {
        try {
            String sql = "INSERT INTO News (url, text, title, date) " +
                    "VALUES ('" +
                    news.getUrl() + "', '" +
                    news.getText() + "', '" +
                    news.getTitle() + "', '" +
                    news.getDate() + "')";
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
        return true;
    }

    public Site getSite(String name) {
        try {
            String sql = "SELECT name, link, tag, attribute, attributeValue FROM Sites " +
                    "WHERE name='" + name + "'";
            ResultSet rs = stmt.executeQuery(sql);

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

}
