package ir.sahab.nimbo.dose;

public class Main {
    public static void main(String[] args) {
        Thread consoleInterfaceThread = new Thread(ConsoleInterface.getInstance());
        Thread schedulerThread = new Thread(SiteUpdateScheduler.getInstance());

        consoleInterfaceThread.start();
        schedulerThread.start();
        try {
            consoleInterfaceThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
