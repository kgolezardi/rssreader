package com.sahab.nimbo.dose;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NewsFetcher {
    private static NewsFetcher ourInstance = new NewsFetcher();

    public static NewsFetcher getInstance() {
        return ourInstance;
    }

    private NewsFetcher() {
    }

    String fetch(String url, SiteConfig siteConfig) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements divs = doc.select(siteConfig.getTag() + "[class]");
        for (Element div : divs) {
            if (div.attr("class").contains(siteConfig.getClassName()))
                return div.text();
        }
        return null;
    }
}
