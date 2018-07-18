package ir.sahab.nimbo.dose;

import ir.sahab.nimbo.dose.database.DbHandler;
import ir.sahab.nimbo.dose.rss.RssFeedMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

public class News {
    private static final Object MUTEX = new Object();

    private Date date;
    private String title;
    private String url;
    private String siteName;
    private String text;

    public News(RssFeedMessage message, String siteName) {
        this(message.getLink(), message.getTitle(), null, message.getPubDate(), siteName);
    }

    public News(String url, String title, String date, String siteName) {
        this(url, title, null, date, siteName);
    }

    public News(String url, String title, Date date, String siteName) {
        this(url, title, null, date, siteName);
    }


    public News(String url, String title, String text, Date date, String siteName) {
        this.url = url;
        this.title = title;
        this.text = text;
        this.date = date;
        this.siteName = siteName;
    }

    public News(String url, String title, String text, String date, String siteName) {
        this(url, title, text, parseDate(date), siteName);
    }

    public static Date parseDate(String date) {
        return DateParser.getInstance().parseDate(date);
    }

    public boolean addToDb() {
        synchronized (MUTEX) {
            if (!DbHandler.getInstance().existsUrl(url))
                if (this.text != null) {
                    DbHandler.getInstance().addNews(this);
                    return true;
                }
            return false;
        }
    }

    public String fetch() {
        try {
            Document doc = Jsoup.connect(url).get();
            Site site = DbHandler.getInstance().getSite(siteName);
            Elements divs = doc.select(site.getTag() + "[" + site.getAttribute() + "]");
            for (Element div : divs) {
                if (div.attr(site.getAttribute()).contains(site.getAttributeValue())){
                    this.text = div.text();
                    return div.text();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Site name: " + siteName);
        }
        return null;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    public String getSiteName() {
        return siteName;
    }

}
