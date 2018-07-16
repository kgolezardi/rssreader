package com.sahab.nimbo.dose;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static Main ourInstance = new Main();
    private Scanner scanner;

    private Main() {
        scanner = new Scanner(System.in);
    }

    static Main getInstance() {
        return ourInstance;
    }

    public void getInput() {
        boolean running = true;

        while (running) {
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

                    DBHandler.getInstance().searchNews(titleCon, textCon);

                    break;

                case "last ten":
                    System.out.print("Site name/address: ");
                    String siteName = scanner.nextLine();

                    List<News> allNews = DBHandler.getInstance().getNewsBySite(siteName, 10);
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
                    }

                    break;

                case "update":
                    fetchAllNews();
                    break;

                case "exit":
                    running = false;
                    break;

                default:
                    System.out.println("Command '" + command + "' not found!");
            }
        }
    }

    public void fetchAllNews() {
        for (Site site : DBHandler.getInstance().allSites())
            site.update();
    }
}
