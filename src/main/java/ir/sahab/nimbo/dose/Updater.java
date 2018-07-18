package ir.sahab.nimbo.dose;

import ir.sahab.nimbo.dose.rss.RssFeed;
import ir.sahab.nimbo.dose.rss.RssFeedMessage;
import ir.sahab.nimbo.dose.rss.RssFeedParser;

import java.io.IOException;
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
        RssFeed rssFeed = parser.readFeed();
        for (RssFeedMessage message : rssFeed.getMessages()) {
            News news = new News(message, site.getAddress());
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Site site = DBHandler.getInstance().getSite(rssFeed.getLink());
                        news.fetch(site);
                        news.addToDb();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}