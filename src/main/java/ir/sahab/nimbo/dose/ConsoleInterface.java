package ir.sahab.nimbo.dose;

import ir.sahab.nimbo.dose.database.DbHandler;

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

        if (num == 0)
            System.out.println("No news found!");
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            System.out.print("\nCommand: ");
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
                    break;
                    // TODO: get exception when duplicated

                case "search":
                    System.out.print("Title contains: ");
                    String titleCon = scanner.nextLine();

                    System.out.print("Text contains: ");
                    String textCon = scanner.nextLine();

                    List<News> allNews = DbHandler.getInstance().searchNews(titleCon, textCon);
                    printNews(allNews);

                    break;

                case "last ten":
                    System.out.print("Site name/address: ");
                    String siteName = scanner.nextLine();

                    allNews = DbHandler.getInstance().getNewsBySite(siteName, 10);
                    printNews(allNews);

                    break;

                case "count today":
                    System.out.print("Site name/address: ");
                    siteName = scanner.nextLine();

                    allNews = DbHandler.getInstance().getNewsBySiteDate(siteName, new Date());
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

                    allNews = DbHandler.getInstance().getNewsBySiteDate(siteName,
                            new GregorianCalendar(year, month - 1, day + 1).getTime());
                    System.out.println(allNews.size());

                    break;

                case "help":
                    System.out.println("You can enter one of these commands:\n" +
                            "  > add site\n" +
                            "  > search\n" +
                            "  > last ten\n" +
                            "  > count today\n" +
                            "  > count\n" +
                            "  > exit");
                    break;

                case "exit":
                    running = false;
                    break;

                default:
                    System.out.println("Command '" + command + "' not found!");
            }
        }
    }
}
