package com.sahab.nimbo.dose;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in) ;
        int command = scanner.nextInt();
        switch (command){
            case 1:
                String address = scanner.next();
                String feedURL = scanner.next();
                String tag = scanner.next();
                String attribute = scanner.next();
                String attributeValue = scanner.next();
                Site site = new Site(address, feedURL, tag, attribute, attributeValue);
                Add(site);
                break;
            case 2:
                for(Site site: DBHandler.getInstance().allSites())
                    site.FEU();
        }
    }

    private void Add(Site site){

    }
}
