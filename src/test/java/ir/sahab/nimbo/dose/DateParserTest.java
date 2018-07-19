package ir.sahab.nimbo.dose;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DateParserTest {
    @Test
    public void constructorTester() {
        List<String> formats = DateParser.getInstance().getFormats();
        List<String> expectedFormats = Arrays.asList("MMMM dd, yyyy, hh:mm a\",\"EEE, dd MMM yyyy HH:mm:ss zzz\",\"EEE dd MMM yyyy HH:mm:ss Z\",\"dd MMM yyyy hh:mm:ss zzzz\",\"MMMM dd, yyyy, hh:mm:ss a\",\"EEE, dd MMM yyyy HH:mm zzz\",\"EEEE, dd MMMM yyyy HH:mm".split("\",\""));
        for(String format : formats)
            if(!expectedFormats.contains(format))
                Assert.fail();

        for(String expectedFormat : expectedFormats)
            if(!formats.contains(expectedFormat))
                Assert.fail();
    }


    @Test
    public void parseDateFormat1Test() {
        Date d = DateParser.getInstance().parseDate("July 17, 2018, 9:34 AM");
        Assert.assertEquals(d.toString(), "Tue Jul 17 09:34:00 IRDT 2018");
    }

    @Test
    public void parseDateFormat2Test() {
        Date d = DateParser.getInstance().parseDate("Tue, 17 Jul 2018 05:04:07 GMT");
        Assert.assertEquals(d.toString(), "Tue Jul 17 09:34:07 IRDT 2018");
    }

    @Test
    public void parseDateFormat3Test() {
        Date d = DateParser.getInstance().parseDate("July 17, 2018, 9:34:07 AM");
        Assert.assertEquals(d.toString(), "Tue Jul 17 09:34:07 IRDT 2018");
    }

    @Test
    public void parseDateFormat4Test() {
        Date d = DateParser.getInstance().parseDate("17 Jul 2018 09:34:07 +0430");
        Assert.assertEquals(d.toString(), "Tue Jul 17 09:34:07 IRDT 2018");
    }

    @Test
    public void parseDateFormat5Test() {
        Date d = DateParser.getInstance().parseDate("Tue, 17 Jul 2018 05:04 GMT");
        Assert.assertEquals(d.toString(), "Tue Jul 17 09:34:00 IRDT 2018");
    }

    @Test
    public void parseDateFormat6Test() {
        Date d = DateParser.getInstance().parseDate("Tuesday, 17 July 2018 09:34");
        Assert.assertEquals(d.toString(), "Tue Jul 17 09:34:00 IRDT 2018");
    }

}
