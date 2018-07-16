package com.sahab.nimbo.dose;

public class ThreadHandler {
    public static void main(String[] args) {
        Thread userInput = new Thread();

        Thread updateFeedsEveryDay;
        Runnable fetchFeeds = new Runnable() {
            @Override
            public void run() {
                Main.getInstance().fetchAllNews();
            }
        };
        userInput.start();
        fetchFeeds.run();
    }
}
