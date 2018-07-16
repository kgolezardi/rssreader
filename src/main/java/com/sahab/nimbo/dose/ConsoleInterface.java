package com.sahab.nimbo.dose;

import java.util.List;
import java.util.Scanner;

public class ConsoleInterface implements Runnable {
    private static ConsoleInterface ourInstance = new ConsoleInterface();
    private Scanner scanner;

    private ConsoleInterface() {
        scanner = new Scanner(System.in);
    }

    static ConsoleInterface getInstance() {
        return ourInstance;
    }

    @Override
    public void run() {
        // TODO: Determine when to exit
        while (true) {
            System.out.print("Command: ");
            String command = scanner.nextLine();

            switch (command) {
                case "add site":
                    System.out.print("Site name/address: ");
                    String address = scanner.nextLine();

                    System.out.print("RSS feed URL: ");
                    String feedUrl = scanner.nextLine();

                    System.out.print("News tag: ");
                    String tag = scanner.nextLine();

                    System.out.print("News tag attribute: ");
                    String attribute = scanner.nextLine();

                    System.out.print("Attribute value: ");
                    String attributeValue = scanner.nextLine();

                    new Site(address, feedUrl, tag, attribute, attributeValue).addToDb();
                    // TODO: get exception when duplicated
                    break;

                case "search":
                    System.out.print("Title contains: ");
                    String titleCon = scanner.nextLine();

                    System.out.print("Text contains: ");
                    String textCon = scanner.nextLine();

                    List<News> allNews = DBHandler.getInstance().searchNews(titleCon, textCon);

                    int num = 0;
                    for (News news : allNews) {
                        num++;
                        System.out.println("#" + num + "          ***");

                        System.out.print("Title: ");
                        System.out.println(news.getTitle());

                        System.out.print("Date: ");
                        System.out.println(news.getDate());

                        System.out.print("Text: ");
                        System.out.println(news.getText());

                        System.out.print("URL: ");
                        System.out.println(news.getUrl());

                        System.out.println();
                    }

                    break;

                case "last ten":
                    System.out.print("Site name/address: ");
                    String siteName = scanner.nextLine();

                    allNews = DBHandler.getInstance().getNewsBySite(siteName, 10);
                    num = 0;
                    for (News news : allNews) {
                        num++;
                        System.out.println("#" + num + "          ***");

                        System.out.print("Title: ");
                        System.out.println(news.getTitle());

                        System.out.print("Date: ");
                        System.out.println(news.getDate());

                        System.out.print("Text: ");
                        System.out.println(news.getText());

                        System.out.println();
                    }

                    break;

                case "exit":
                    System.exit(0);
                    // TODO: Shutdown the executor
                    break;

                default:
                    System.out.println("Command '" + command + "' not found!");
            }
        }
    }
}
