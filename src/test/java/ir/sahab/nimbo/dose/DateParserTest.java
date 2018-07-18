package ir.sahab.nimbo.dose;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class DateParseTest {
    @Test
    public void dateParseFormat1Test() {
        Date d = DateParser.getInstance().parseDate("July 17, 2018, 9:34 AM");
        Assert.assertEquals(d.toString(), "Tue Jul 17 09:34:00 IRDT 2018");
    }

    @Test
    public void dateParseFormat2Test() {
        Date d = DateParser.getInstance().parseDate("Tue, 17 Jul 2018 05:04:07 GMT");
        Assert.assertEquals(d.toString(), "Tue Jul 17 09:34:07 IRDT 2018");
    }

    @Test
    public void dateParseFormat3Test() {
        Date d = DateParser.getInstance().parseDate("July 17, 2018, 9:34:07 AM");
        Assert.assertEquals(d.toString(), "Tue Jul 17 09:34:07 IRDT 2018");
    }

    @Test
    public void dateParseFormat4Test() {
        Date d = DateParser.getInstance().parseDate("17 Jul 2018 09:34:07 +0430");
        Assert.assertEquals(d.toString(), "Tue Jul 17 09:34:07 IRDT 2018");
    }

    @Test
    public void dateParseFormat5Test() {
        Date d = DateParser.getInstance().parseDate("Tue, 17 Jul 2018 05:04 GMT");
        Assert.assertEquals(d.toString(), "Tue Jul 17 09:34:00 IRDT 2018");
    }

}
