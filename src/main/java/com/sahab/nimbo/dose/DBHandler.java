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
            "autoReconnect=true&useSSL=false&characterEncoding=UTF-8";

    static final private String USER = "root";
    static final private String PASS = "";
    // TODO: use this values as properties
    // TODO: prepared statements

    private Connection conn = null;

    private DBHandler() {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeStatement(Statement stmt) {
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addSite(Site site) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
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
        } finally {
            closeStatement(stmt);
        }
        return true;
    }

    public List<Site> allSites() {
        List<Site> sites = new ArrayList<Site>();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
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
        } finally {
            closeStatement(stmt);
        }
        return sites;
    }

    public boolean existsURL(String url) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT url FROM News WHERE url='" + url + "'";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
                return true;
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            closeStatement(stmt);
        }
        return false;
    }

    public boolean addNews(News news) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "INSERT INTO News (url, text, title, date, siteName) " +
                    "VALUES ('" +
                    news.getUrl() + "', '" +
                    news.getText() + "', '" +
                    news.getTitle() + "', '" +
                    news.getDate() + "', '" +
                    news.getSiteName() + "')";
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        } finally {
            closeStatement(stmt);
        }
        return true;
    }

    public Site getSite(String name)  {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
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
        } finally {
            closeStatement(stmt);
        }
        return null;
    }

    public List<News> searchNews(String titleCon, String textCon) {
        List<News> news = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT url, text, title, date, siteName FROM News " +
                    "WHERE title LIKE '%" + titleCon + "' " +
                    "AND text LIKE '%" + textCon + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String url = rs.getString("url");
                String text = rs.getString("text");
                String title = rs.getString("title");
                String date = rs.getString("date");
                String siteName = rs.getString("siteName");
                news.add(new News(url, title, text, date, siteName));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            closeStatement(stmt);
        }
        return news;
    }

    public List<News> getNewsBySite(String siteName, int limit) {
        List<News> news = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT url, text, title, date FROM News " +
                    "WHERE siteName='" + siteName + "' " +
                    "ORDER BY date DESC LIMIT " + limit;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String url = rs.getString("url");
                String text = rs.getString("text");
                String title = rs.getString("title");
                String date = rs.getString("date");
                news.add(new News(url, title, text, date, siteName));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            closeStatement(stmt);
        }
        return news;
    }
}
