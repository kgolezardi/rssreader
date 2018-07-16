package com.sahab.nimbo.dose;


import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateParser {
    private List<String> formats = new ArrayList<>();
    private static DateParser ourInstance = new DateParser();

    public static DateParser getInstance() {
        return ourInstance;
    }

    private void getConifgs() {
        String resourceName = "dateParseFormat.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        formats = Arrays.asList(props.getProperty("formats").split("\",\""));
    }

    public Date parseDate(String date) {
        ParseException parseException = null;
        Date util_sdate;
        boolean flag = false;

        for(String format: formats){
            try{
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false);
                util_sdate = sdf.parse(date);
                return util_sdate;
            } catch (ParseException pe){
                parseException = pe;
            }
        }

        parseException.printStackTrace();

        return null;
    }

    private DateParser() {
        getConifgs();
    }
}
