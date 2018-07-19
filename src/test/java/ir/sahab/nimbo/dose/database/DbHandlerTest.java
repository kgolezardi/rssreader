package ir.sahab.nimbo.dose.database;

import ir.sahab.nimbo.dose.Site;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DbHandlerTest {

    @Before
    public void cleanDatabase() throws SQLException {
        DbHandler.getInstance().cleanDatabase();
    }

    @Test
    public void addSeveralSitesAndGetAllSites() {
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
    public void existsUrl() {

    }

    @Test
    public void addAndGetSite() {
        Site site = new Site("TestSite", "example.com", "tag",
                "att", "value");
        DbHandler.getInstance().addSite(site);
        Site resSite = DbHandler.getInstance().getSite("TestAllSites");
        assertEquals(site, resSite);
    }

    @Test
    public void searchNews() {
    }

    @Test
    public void getNewsBySite() {
    }

    @Test
    public void getNewsBySiteDate() {
    }
}