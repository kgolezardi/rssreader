package com.sahab.nimbo.dose;

public class ThreadHandler {
    public static void main(String[] args) {
        Main m = new Main();
        Thread updateFeedsEveryDay;
        Runnable fetchFeeds = new Runnable() {
            @Override
            public void run() {
                m.fetchAllNews();
            }
        };

    }
}
