package ir.sahab.nimbo.dose;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigTest {

    @Test
    public void getInstanceDbName() {
        Assert.assertEquals(Config.getInstance().DB_NAME, "NewsReaderTest");
    }

    @Test
    public void getInstanceUser() {
        Assert.assertEquals(Config.getInstance().USER, "root");
    }

    @Ignore
    public void getInstanceSettings() {
        System.out.println(Config.getInstance().CONNECTION_SETTINGS);
        Assert.assertEquals(Config.getInstance().USER, "root");
    }
}