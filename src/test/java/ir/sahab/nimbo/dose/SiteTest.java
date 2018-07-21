package ir.sahab.nimbo.dose;

import ir.sahab.nimbo.dose.database.DbHandler;
import ir.sahab.nimbo.dose.rss.RssFeed;
import ir.sahab.nimbo.dose.rss.RssFeedMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class SiteTest {

    Site site;

    @Before
    public void cleanDatabase() throws SQLException {
        DbHandler.getInstance().cleanDatabase();
    }

    @Before
    public void setUp() {
        site = new Site("ISNA", "https://www.isna.ir/rss", "div", "itemprop", "articleBody");
    }
    
    @Test
    public void addToDb() {
        site.addToDb();
        Assert.assertTrue(DbHandler.getInstance().allSites().contains(site));
    }

    @Test
    public void addNewsToDb() {
        RssFeed feed = new RssFeed("ایسنا", "https://www.isna.ir",
                "جدیدترین اخبار ورزشی، حوادث، سیاسی و اقتصادی ایران و سایر مناطق جهان را در خبرگزاری ایسنا بخوانید",
                "", "© 2016 isna.ir. All rights reserved", "");
        RssFeedMessage message = new RssFeedMessage();
        message.setLink("https://www.isna.ir/news/97042715109/%D9%85%D8%A7-%D8%A7%D8%B5%D9%84%D8%A7%D8%AD-%D8%B7%D9%84%D8%A8%D8%A7%D9%86-%D8%B3%D9%88%D9%BE%D8%A7%D9%BE-%D8%A7%D8%B7%D9%85%DB%8C%D9%86%D8%A7%D9%86-%D9%86%D8%B8%D8%A7%D9%85%DB%8C%D9%85");
        message.setDescription("«اصلاح\u200Cطلبان ابایی از این ندارند که بگویند ما سوپاپ اطمینان نظامیم. \u200Cما معتقدیم تغییر نظام بر مشکلات و مشقات کشور خواهد افزود.»");
        message.setGuid("https://www.isna.ir/news/97042715109/%D9%85%D8%A7-%D8%A7%D8%B5%D9%84%D8%A7%D8%AD-%D8%B7%D9%84%D8%A8%D8%A7%D9%86-%D8%B3%D9%88%D9%BE%D8%A7%D9%BE-%D8%A7%D8%B7%D9%85%DB%8C%D9%86%D8%A7%D9%86-%D9%86%D8%B8%D8%A7%D9%85%DB%8C%D9%85");
        message.setTitle("ما اصلاح\u200Cطلبان سوپاپ اطمینان نظامیم");
        message.setPubDate("Wed, 18 Jul 2018 14:29:03 GMT");
        feed.getMessages().add(message);
        site.addToDb();
        site.addNewsToDb(feed);
        // TODO:  set limit to -1
        Assert.assertTrue(DbHandler.getInstance().getNewsBySite("ISNA", 1)
                .contains(new News(message, site.getAddress())));
    }

    @Test
    public void getRssFeedUrl() {
    }
    @Test
    public void update() {
        site.addToDb();
        site.update();
    }
}