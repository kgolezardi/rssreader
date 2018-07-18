package ir.sahab.nimbo.dose.rss;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class RssFeedParserTest {
    private RssFeed rssFeed;
    @Before
    public void loadRssParser() {
        try {
            RssFeedParser parser = new RssFeedParser(
                    "https://www.isna.ir/rss");
            rssFeed = parser.readFeed();
        } catch (Exception E) {
            Assert.fail();
        }
    }
    @Test
    public void feedNumberTest() {
        Assert.assertEquals(rssFeed.getMessages().size(), 30);
    }

    @Test
    public void messagesUrlTest() {
        for (RssFeedMessage message: rssFeed.getMessages()) {
            try {
                new URL(message.getLink());
            } catch (MalformedURLException m) {
                Assert.fail();
            }
        }
    }
}
