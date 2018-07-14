package com.sahab.nimbo.dose;

import com.sahab.nimbo.dose.rss.FeedMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class News {
    private Site site;
    private String date;
    private String title;
    private String URL;
    private String description;
    private String text;

    public News(FeedMessage message, Site site) {
        this.site = site;
        this.URL = message.getLink();
        this.date = message.getPubDate();
        this.description = message.getDescription();
        this.title = message.getTitle();

        if(DBHandler.getInstance().existsURL(message.getLink())){
            try {
                text = fetch();
                if(text != null)
                    DBHandler.getInstance().addNews(this);
            } catch(IOException E){}
        }
    }

    private String fetch() throws IOException {
            Document doc = Jsoup.connect(URL).get();

            Elements divs = doc.select(site.getTag() + "[" +site.getAttribute() + "]");
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

    public String getURL() {
        return URL;
    }

    public String getText() {
        return text;
    }
}
