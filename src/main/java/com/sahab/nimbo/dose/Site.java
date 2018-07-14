package com.sahab.nimbo.dose;

import com.sahab.nimbo.dose.rss.Feed;
import com.sahab.nimbo.dose.rss.FeedMessage;
import com.sahab.nimbo.dose.rss.RSSFeedParser;


public class Site {
    Feed feed;
    private String address;
    private String feedUrl;
    private String tag;
    private String attribute;
    private String attributeValue;

    public String getTag() {
        return tag;
    }

    public void FEU(){
        for (FeedMessage message: feed.getMessages()) {
            News news = new News(message, this);
            news.addToDB();
        }
    }

    Site (String address, String feedUrl, String tag, String attribute, String attributeValue){
        this.address = address;
        this.feedUrl = feedUrl;
        this.tag = tag;
        this.attribute = attribute;
        this.attributeValue = attributeValue;
        RSSFeedParser parser = new RSSFeedParser(feedUrl);
        this.feed = parser.readFeed();
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public String getAddress() {
        return address;
    }
}
