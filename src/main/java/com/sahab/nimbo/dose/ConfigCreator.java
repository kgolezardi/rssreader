package com.sahab.nimbo.dose;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ConfigCreator {

    private static List<SiteConfig> createConfigs() throws IOException{
        try(Reader reader = new InputStreamReader(ConfigCreator.class.getResourceAsStream("/sites.json"), "UTF-8")){
            Gson gson = new GsonBuilder().create();
            TypeToken<List<SiteConfig>> token = new TypeToken<List<SiteConfig>>() {};
            List<SiteConfig> Configs = gson.fromJson(reader, token.getType());
            return Configs;
        }
    }

    private static boolean matches(String url, String RSS){
        if(url.contains("//"))
            url = url.substring(url.indexOf("//") + 2);
        if(url.startsWith("www."))
            url = url.substring(4);
        if(url.contains("/"))
            url = url.substring(0, url.indexOf('/'));
        if(RSS.contains(url)){
            return true;
        }
        return false;
    }

    private static SiteConfig find_config(String url, List<SiteConfig> siteConfigs){
        for(SiteConfig siteConfig: siteConfigs){
            if(matches(url, siteConfig.getLink()))
                return siteConfig;
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        String url = "http://www.varzesh3.com/news/1537417/بارسلونا-و-شروع-تمرینات-پیش-فصل-با-چهارده-بازیکن";
        List<SiteConfig> Configs = createConfigs();
        SiteConfig siteConfig = find_config(url, Configs);
        System.out.println(NewsFetcher.fetch(url, siteConfig));
    }
}