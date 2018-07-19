package ir.sahab.nimbo.dose.database;

import ir.sahab.nimbo.dose.Config;
import ir.sahab.nimbo.dose.News;
import ir.sahab.nimbo.dose.Site;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbHandler {

    private static DbHandler ourInstance = new DbHandler();

    private DbHandler() {
        initDatabase();
        initTables();
    }

    public static DbHandler getInstance() {
        return ourInstance;
    }

    private void initDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection tempConn = DriverManager.getConnection(Config.getInstance().SQL_URL + "?" +
                            Config.getInstance().CONNECTION_SETTINGS, Config.getInstance().USER,
                    Config.getInstance().PASS);
                 Statement stmt = tempConn.createStatement()) {
                String sql = "CREATE DATABASE IF NOT EXISTS `" + Config.getInstance().DB_NAME + "` " +
                        "/*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_persian_ci */;\n";
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initTables() {
        try (Connection conn = DataSource.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            String createSite = "CREATE TABLE IF NOT EXISTS `Sites` (\n" +
                    "  `name` varchar(45) COLLATE utf8mb4_persian_ci NOT NULL,\n" +
                    "  `link` varchar(200) COLLATE utf8mb4_persian_ci NOT NULL,\n" +
                    "  `tag` varchar(50) COLLATE utf8mb4_persian_ci NOT NULL,\n" +
                    "  `attribute` varchar(50) COLLATE utf8mb4_persian_ci DEFAULT NULL,\n" +
                    "  `attributeValue` varchar(200) COLLATE utf8mb4_persian_ci DEFAULT NULL,\n" +
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
        } catch (SQLException | PropertyVetoException | IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addSite(Site site) {
        String sql = "INSERT INTO Sites (name, link, tag, attribute, attributeValue) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, site.getAddress());
            stmt.setString(2, site.getRssFeedUrl());
            stmt.setString(3, site.getTag());
            stmt.setString(4, site.getAttribute());
            stmt.setString(5, site.getAttributeValue());
            stmt.executeUpdate();

        } catch (SQLException | PropertyVetoException | IOException se) {
            se.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Site> allSites() {
        List<Site> sites = new ArrayList<Site>();
        try (Connection conn = DataSource.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
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
        } catch (SQLException | IOException | PropertyVetoException se) {
            se.printStackTrace();
        }
        return sites;
    }

    public boolean existsUrl(String url) {
        String sql = "SELECT url FROM News WHERE url=?";
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, url);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return true;
        } catch (SQLException | PropertyVetoException | IOException se) {
            se.printStackTrace();
        }
        return false;
    }

    public boolean addNews(News news) {
        String sql = "INSERT INTO News (url, text, title, pubTime, siteName) " +
                "VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, news.getUrl());
            stmt.setString(2, news.getText());
            stmt.setString(3, news.getTitle());
            stmt.setTimestamp(4, new java.sql.Timestamp(news.getDate().getTime()));
            stmt.setString(5, news.getSiteName());
            stmt.executeUpdate();
        } catch (SQLException | PropertyVetoException | IOException se) {
            se.printStackTrace();
            return false;
        }
        return true;
    }

    public Site getSite(String name) {
        String sql = "SELECT name, link, tag, attribute, attributeValue FROM Sites " +
                "WHERE name=?";
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String link = rs.getString("link");
                String tag = rs.getString("tag");
                String attribute = rs.getString("attribute");
                String attributeValue = rs.getString("attributeValue");
                return new Site(name, link, tag, attribute, attributeValue);
            }

        } catch (SQLException | PropertyVetoException | IOException se) {
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
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titleCon);
            stmt.setString(2, textCon);

            ResultSet rs = stmt.executeQuery();
            addNewsToList(rs, news);
        } catch (SQLException | PropertyVetoException | IOException se) {
            se.printStackTrace();
        }
        return news;
    }

    private void addNewsToList(ResultSet rs, List<News> news) throws SQLException {
        while (rs.next()) {
            String url = rs.getString("url");
            String text = rs.getString("text");
            String title = rs.getString("title");
            java.util.Date date = rs.getTimestamp("pubTime");
            String siteName = rs.getString("siteName");
            news.add(new News(url, title, text, date, siteName));
        }
    }

    public List<News> getNewsBySite(String siteName, int limit) {
        List<News> news = new ArrayList<>();
        String sql = "SELECT url, text, title, pubTime, siteName FROM News " +
                "WHERE siteName=? ORDER BY pubTime DESC LIMIT ?";
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, siteName);
            stmt.setInt(2, limit);

            ResultSet rs = stmt.executeQuery();

            addNewsToList(rs, news);

        } catch (SQLException | IOException | PropertyVetoException se) {
            se.printStackTrace();
        }
        return news;
    }

    public List<News> getNewsBySiteDate(String siteName, Date time) {
        List<News> news = new ArrayList<>();
        java.sql.Date sqlDate = new java.sql.Date(time.getTime());
        String sql = "SELECT url, text, title, pubTime FROM News " +
                "WHERE siteName=? AND DATE(pubTime)=?";

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, siteName);
            stmt.setDate(2, sqlDate);

            ResultSet rs = stmt.executeQuery();

            addNewsToList(rs, news);
        } catch (SQLException | IOException | PropertyVetoException se) {
            se.printStackTrace();
        }
        return news;
    }

    public void cleanDatabase() throws SQLException {
        String sql = "DELETE FROM Sites WHERE TRUE";

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (PropertyVetoException | IOException se) {
            se.printStackTrace();
        }

        sql = "DELETE FROM News WHERE TRUE";

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (PropertyVetoException | IOException se) {
            se.printStackTrace();
        }
    }
}
