package com.sahab.nimbo.dose;

public class ThreadHandler {
    public static void main(String[] args) {
//        Main m = new Main();
//        Thread userInput = new Thread();
//
//        Thread updateFeedsEveryDay;
//        Runnable fetchFeeds = new Runnable() {
//            @Override
//            public void run() {
//                m.fetchAllNews();
//            }
//        };
//        userInput.start();
//        fetchFeeds.run();
        News n = new News("a", "b", "c", "July 15, 2018, 4:39 AM", "e");
        System.out.println(n.parseDate("July 15, 2018, 4:39 AM").toString());
    }
}
