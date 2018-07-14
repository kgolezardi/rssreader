package com.sahab.nimbo.dose;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in) ;
        int command = scanner.nextInt();
        Main m = new Main();
        switch (command){
            case 1:
                String address = scanner.next();
                String feedURL = scanner.next();
                String tag = scanner.next();
                String attribute = scanner.next();
                String attributeValue = scanner.next();
                Site site = new Site(address, feedURL, tag, attribute, attributeValue);
                DBHandler.getInstance().addSite(site);
                break;
            case 2:
                m.fetchAllNews();
                break;
        }
    }

    public void fetchAllNews(){
        for(Site site: DBHandler.getInstance().allSites())
            site.FEU();
    }
}
