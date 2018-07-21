package ir.sahab.nimbo.dose;

import ir.sahab.nimbo.dose.database.DbHandler;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SiteUpdateScheduler implements Runnable {
    private static SiteUpdateScheduler ourInstance = new SiteUpdateScheduler();

    public static SiteUpdateScheduler getInstance() {
        return ourInstance;
    }

    private SiteUpdateScheduler() {

    }

    @Override
    public void run() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(Config.getInstance().THREAD_POOL,
                r -> {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                });
        executor.scheduleWithFixedDelay(() -> {
            List<Site> sites = DbHandler.getInstance().allSites();
            for (Site site : sites) {
                executor.submit(site::update);
            }
        }, Config.getInstance().INITIAL_DELAY, Config.getInstance().DELAY, TimeUnit.SECONDS);
    }
}
