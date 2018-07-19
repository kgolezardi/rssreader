package ir.sahab.nimbo.dose.database;

import ir.sahab.nimbo.dose.News;
import ir.sahab.nimbo.dose.Site;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class DbHandlerTest {

    private List<News> getTestNews() {
        List<News> testNews = new ArrayList<>();
        testNews.add(new News("www.example.com/news/1",
                "Trump is bad", "Obama is good", new Date(), "TestSite"));
        testNews.add(new News("www.example.com/news/2",
                "Oil is all over the place ", "Iran's oil is good", new Date(), "TestSite"));
        testNews.add(new News("www.example.com/news/3",
                "Are you suffering from that disease?", "Me too", new Date(), "TestSite"));
        return testNews;
    }

    private Site getTestSite() {
        return new Site("TestSite", "example.com/rss", "tag",
                "att", "val");
    }

    @Before
    public void cleanDatabase() throws SQLException {
        DbHandler.getInstance().cleanDatabase();
    }

    @Test
    public void testAddSeveralSitesAndGetAllSites() {
        List<Site> mySites = new ArrayList<>();
        mySites.add(new Site("TestAllSites1", "example1.com", "div",
                "att", "value"));
        mySites.add(new Site("TestAllSites2", "example2.com", "p",
                "att", "value"));
        mySites.add(new Site("TestAllSites3", "example3.com", "article",
                "att", "value"));
        mySites.add(new Site("TestAllSites4", "example4.com", "table",
                "att", "value"));

        DbHandler.getInstance().addSite(mySites.get(0));
        DbHandler.getInstance().addSite(mySites.get(1));
        DbHandler.getInstance().addSite(mySites.get(2));
        DbHandler.getInstance().addSite(mySites.get(3));

        List<Site> resSites = DbHandler.getInstance().allSites();

        for (Site site : mySites)
            assertTrue(resSites.contains(site));

        for (Site site : resSites)
            assertTrue(mySites.contains(site));
    }


    @Test
    public void testNewsShouldNotExistAtFirst() {
        assertFalse(DbHandler.getInstance().existsUrl("www.example.com/news/1"));
    }

    @Test
    public void testNewsShouldExistAfterAddition() {
        DbHandler.getInstance().addSite(getTestSite());
        DbHandler.getInstance().addNews(new News("www.example.com/news/1",
                "title", "text", new Date(), "TestSite"));
        assertTrue(DbHandler.getInstance().existsUrl("www.example.com/news/1"));
    }

    @Test
    public void testAddAndGetSite() {
        Site site = getTestSite();
        DbHandler.getInstance().addSite(site);
        Site resSite = DbHandler.getInstance().getSite("TestSite");
        assertEquals(site, resSite);
    }

    @Test
    public void testSearchingWithEmptyValuesShouldReturnAll() {
        DbHandler.getInstance().addSite(getTestSite());
        List<News> testNews = getTestNews();

        for (News news : testNews)
            DbHandler.getInstance().addNews(news);

        List<News> resNews = DbHandler.getInstance().searchNews("", "");

        for (News news : testNews)
            assertTrue(resNews.contains(news));
    }

    @Test
    public void testSearchingByTitle() {
        DbHandler.getInstance().addSite(getTestSite());
        List<News> testNews = getTestNews();

        for (News news : testNews)
            DbHandler.getInstance().addNews(news);

        List<News> resNews = DbHandler.getInstance().searchNews("Are", "");

        assertEquals(1, resNews.size());
        assertEquals(testNews.get(2), resNews.get(0));
    }

    @Test
    public void testSearchingByText() {
        DbHandler.getInstance().addSite(getTestSite());
        List<News> testNews = getTestNews();

        for (News news : testNews)
            DbHandler.getInstance().addNews(news);

        List<News> resNews = DbHandler.getInstance().searchNews("", "good");

        assertEquals(2, resNews.size());
        assertTrue(resNews.contains(testNews.get(0)));
        assertTrue(resNews.contains(testNews.get(1)));
    }

    @Test
    public void testSearchingByTitleAndText() {
        DbHandler.getInstance().addSite(getTestSite());
        List<News> testNews = getTestNews();

        for (News news : testNews)
            DbHandler.getInstance().addNews(news);

        List<News> resNews = DbHandler.getInstance().searchNews("is", "good");

        assertEquals(2, resNews.size());
        assertTrue(resNews.contains(testNews.get(0)));
        assertTrue(resNews.contains(testNews.get(1)));
    }

    @Test
    public void testSearchingByTitleAndTextThatShouldReturnEmpty() {
        DbHandler.getInstance().addSite(getTestSite());
        List<News> testNews = getTestNews();

        for (News news : testNews)
            DbHandler.getInstance().addNews(news);

        List<News> resNews = DbHandler.getInstance().searchNews("hello", "all");

        assertEquals(0, resNews.size());
    }

    @Test
    public void getNewsBySite() {
    }

    @Test
    public void getNewsBySiteDate() {
    }
}