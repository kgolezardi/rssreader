package com.sahab.nimbo.dose;

import com.sahab.nimbo.dose.rss.Feed;
import com.sahab.nimbo.dose.rss.FeedMessage;
import com.sahab.nimbo.dose.rss.RssFeedParser;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class Updater implements Runnable {
    private ExecutorService executorService;

    public Updater(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void run() {
        List<Site> sites = DBHandler.getInstance().allSites();
        for (Site site : sites) {
            executorService.submit(new SiteUpdater(site, executorService));
        }
    }
}

class SiteUpdater implements Runnable {
    private Site site;
    private ExecutorService executorService;

    public SiteUpdater(Site site, ExecutorService executorService) {
        this.site = site;
        this.executorService = executorService;
    }

    @Override
    public void run() {
        RssFeedParser parser = new RssFeedParser(site.getFeedUrl());
        Feed feed = parser.readFeed();
        for (FeedMessage message : feed.getMessages()) {
            News news = new News(message, site.getAddress());
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    news.addToDb();
                }
            });
        }
    }
}