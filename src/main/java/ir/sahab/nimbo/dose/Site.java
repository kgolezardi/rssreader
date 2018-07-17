package ir.sahab.nimbo.dose;


import ir.sahab.nimbo.dose.database.DbHandler;

public class Site {
    private String address;
    private String feedUrl;
    private String tag;
    private String attribute;
    private String attributeValue;

    public String getTag() {
        return tag;
    }

    public Site (String address, String feedUrl, String tag, String attribute, String attributeValue){
        this.address = address;
        this.feedUrl = feedUrl;
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

    public String getFeedUrl() {
        return feedUrl;
    }

    public String getAddress() {
        return address;
    }

    public void addToDb() {
        DbHandler.getInstance().addSite(this);
    }
}
