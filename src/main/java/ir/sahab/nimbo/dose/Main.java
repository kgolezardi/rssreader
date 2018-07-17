package ir.sahab.nimbo.dose;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // TODO: how to do this in a config file?
        System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "WARNING");
        System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        executor.submit(ConsoleInterface.getInstance());
        executor.scheduleWithFixedDelay(new Updater(executor), 1, 10, TimeUnit.SECONDS);
    }
}
