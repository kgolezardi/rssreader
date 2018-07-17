package ir.sahab.nimbo.dose;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class DateParseTest {
    @Test
    public void dateParseFormat1Test() {
        Date d = DateParser.getInstance().parseDate("July 17, 2018, 9:34 AM");
    }

    @Test
    public void dateParseFormat2Test() {
        Date d = DateParser.getInstance().parseDate("Tue, 17 Jul 2018 05:06:07 GMT");
    }

    @Test
    public void dateParseFormat3Test() {
        Date d = DateParser.getInstance().parseDate("2018-07-17T05:32:17");
    }

}
