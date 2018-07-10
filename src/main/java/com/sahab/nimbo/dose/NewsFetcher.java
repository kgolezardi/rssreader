package com.sahab.nimbo.dose;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NewsFetcher {

    public static void main(String[] args) throws IOException {
        String url = "https://www.vice.com/en_uk/article/ev84zp/people-told-us-the-wildest-things-theyve-seen-while-tripping";

        Document doc = Jsoup.connect(url).get();

        Elements divs = doc.select("div[class]");
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        print("\nDivs: (%d)", divs.size());
        for (Element div : divs) {
            // System.out.println(div.toString());
            if (div.attr("class").contains("article__body dsp-block-xs"))
            print(" * %s: <%s>", div.tagName(), div.text());
        }
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }

}