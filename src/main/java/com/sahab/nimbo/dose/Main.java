package com.sahab.nimbo.dose;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        executor.submit(ConsoleInterface.getInstance());
        executor.scheduleWithFixedDelay(new Updater(executor), 1, 10, TimeUnit.SECONDS);
    }
}
