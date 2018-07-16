package com.sahab.nimbo.dose;

import com.sahab.nimbo.dose.rss.FeedMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class News {
    private String toBeParsedDate;
    private Date date;// TODO: java date
    private String title;
    private String url;
    private String siteName;
    private String text;

    public News(FeedMessage message, String siteName) {
        this(message.getLink(), message.getTitle(), null, message.getPubDate(), siteName);
    }

//    private
    private Date parseDate(String date){
        String format1 = "MMMM dd, YYYY, hh:mm a";
        String format2 = "EEE, ";

        Date util_sdate = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format1);
            sdf.setLenient(false);
            util_sdate = sdf.parse(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return util_sdate;
    }

    public News(String url, String title, String text, String date, String siteName){
        this.url = url;
        this.title = title;
        this.text = text;
        this.toBeParsedDate = date;
        this.siteName = siteName;
        this.date = parseDate(toBeParsedDate);

    }

    public boolean addToDb() {
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
            Site site = DBHandler.getInstance().getSite(siteName);

            Elements divs = doc.select(site.getTag() + "[" + site.getAttribute() + "]");
            for (Element div : divs) {
                if (div.attr(site.getAttribute()).contains(site.getAttributeValue()))
                    return div.text();
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
