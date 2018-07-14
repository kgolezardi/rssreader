package com.sahab.nimbo.dose;

import com.sahab.nimbo.dose.rss.FeedMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class News {
    private String date; // TODO: java date
    private String title;
    private String url;
    private String link;
    private String description;
    private String text;

    public News(FeedMessage message, String link) {
        this.link = link;
        this.url = message.getLink();
        this.date = message.getPubDate();
        this.description = message.getDescription();
        this.title = message.getTitle();
    }

    public News(String url, String title, String text, String date, String link){
        this.url = url;
        this.title = title;
        this.text = text;
        this.date = date;
        this.link = link;

    }

    boolean addToDB() {
        if (!DBHandler.getInstance().existsURL(url)) {
            try {
                this.text = fetch();
                if (this.text != null)
                    DBHandler.getInstance().addNews(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private String fetch() throws IOException {
            Document doc = Jsoup.connect(url).get();
            Site site = DBHandler.getInstance().getSite(link);

            Elements divs = doc.select(site.getTag() + "[" + site.getAttribute() + "]");
            for (Element div : divs) {
                if (div.attr(site.getAttribute()).contains(site.getAttributeValue()))
                    return div.text();
            }
            return null;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }
}
