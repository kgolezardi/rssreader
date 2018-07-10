package com.sahab.nimbo.dose.rss;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ReadTest {
    public static void main(String[] args) throws IOException {
                RSSFeedParser parser = new RSSFeedParser(
                "https://www.isna.ir/rss");
        Feed feed = parser.readFeed();
        System.out.println(feed);
        for (FeedMessage message: feed.getMessages()) {
            System.out.println(message);

            URL url = new URL(message.link);
            String out = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
            InputStream in = url.openStream();
            System.out.println(out);
        }
    }
}