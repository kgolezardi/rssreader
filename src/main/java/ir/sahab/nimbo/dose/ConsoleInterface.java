package ir.sahab.nimbo.dose;

import java.util.Date;
import java.util.GregorianCalendar;
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

    private void printNews(List<News> allNews) {
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
                    printNews(allNews);

                    break;

                case "last ten":
                    System.out.print("Site name/address: ");
                    String siteName = scanner.nextLine();

                    allNews = DBHandler.getInstance().getNewsBySite(siteName, 10);
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

                        System.out.println();
                    }

                    break;

                case "count today":
                    System.out.print("Site name/address: ");
                    siteName = scanner.nextLine();

                    allNews = DBHandler.getInstance().getNewsBySiteDate(siteName, new Date());
                    System.out.println(allNews.size());

                    break;

                case "count":
                    System.out.print("Site name/address: ");
                    siteName = scanner.nextLine();

                    System.out.print("Day Month Year: ");
                    int day = scanner.nextInt();
                    int month = scanner.nextInt();
                    int year = scanner.nextInt();
                    scanner.nextLine();

                    allNews = DBHandler.getInstance().getNewsBySiteDate(siteName,
                            new GregorianCalendar(year, month - 1, day + 1).getTime());
                    System.out.println(allNews.size());

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
