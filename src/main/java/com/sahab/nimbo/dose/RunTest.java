package com.sahab.nimbo.dose;

import java.io.IOException;
import java.util.List;

public class RunTest {
    public static void main(String[] args) throws IOException {
        String url = "http://www.varzesh3.com/news/1537417/بارسلونا-و-شروع-تمرینات-پیش-فصل-با-چهارده-بازیکن";
        List<SiteConfig> Configs = ConfigCreator.getInstance().createConfigs();
        SiteConfig siteConfig = ConfigCreator.getInstance().find_config(url, Configs);
        System.out.println(NewsFetcher.getInstance().fetch(url, siteConfig));
    }
}
