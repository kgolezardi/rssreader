package ir.sahab.nimbo.dose;


import ir.sahab.nimbo.dose.database.DbHandler;
import ir.sahab.nimbo.dose.rss.RssFeed;
import ir.sahab.nimbo.dose.rss.RssFeedMessage;
import ir.sahab.nimbo.dose.rss.RssFeedParser;

public class Site {
    private String address;
    private String rssFeedUrl;
    private String tag;
    private String attribute;
    private String attributeValue;

    public String getTag() {
        return tag;
    }

    public Site (String address, String rssFeedUrl, String tag, String attribute, String attributeValue){
        this.address = address;
        this.rssFeedUrl = rssFeedUrl;
        this.tag = tag;
        this.attribute = attribute;
        this.attributeValue = attributeValue;

    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getRssFeedUrl() {
        return rssFeedUrl;
    }

    public String getAddress() {
        return address;
    }

    public void addToDb() {
        DbHandler.getInstance().addSite(this);
    }

    public void update() {
        RssFeedParser parser = new RssFeedParser(rssFeedUrl);
        RssFeed feed = parser.readFeed();
        for (RssFeedMessage message : feed.getMessages()) {
            News news = new News(message, address);
            news.fetch();
            news.addToDb();
        }
    }
}
