package ir.sahab.nimbo.dose;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10,
                r -> {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                });
        Thread consoleInterfaceThread = new Thread(ConsoleInterface.getInstance());
        executor.scheduleWithFixedDelay(new Updater(executor), 1, 10, TimeUnit.SECONDS);

        consoleInterfaceThread.start();
        try {
            consoleInterfaceThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
