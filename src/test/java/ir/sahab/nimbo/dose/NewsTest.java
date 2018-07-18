package ir.sahab.nimbo.dose;

import org.junit.Before;
import org.junit.Test;

public class NewsFetchTest {
    @Before
    public void loadSite() {
        Site site = new Site("ISNA", "http://isna.ir/rss", "div", "class", "item-body content-full-news");
    }
    @Test
    public void newsFetchTest() {
        News news = new News("");
    }
}
