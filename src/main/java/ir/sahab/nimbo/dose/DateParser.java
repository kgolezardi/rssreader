package ir.sahab.nimbo.dose;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateParser {
    private List<String> formats;
    private static DateParser ourInstance = new DateParser();

    public List<String> getFormats() { return formats; }

    public static DateParser getInstance() {
        return ourInstance;
    }

    public Date parseDate(String date) {
        ParseException parseException = null;

        for (String format : formats) {
            try{
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false);
                return sdf.parse(date);
            } catch (ParseException pe) {
                parseException = pe;
            }
        }

        parseException.printStackTrace();

        return null;
    }

    private DateParser() {
        formats = Arrays.asList(Config.getInstance().FORMATS.split("\",\""));
    }
}
