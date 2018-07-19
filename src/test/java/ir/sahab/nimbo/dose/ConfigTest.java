package ir.sahab.nimbo.dose;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigTest {

    @Test
    public void getInstanceNewsReader() {
        Assert.assertEquals(Config.getInstance().DB_NAME, "NewsReader");
    }

    @Test
    public void getInstanceUser() {
        Assert.assertEquals(Config.getInstance().USER, "root");
    }

    @Test
    public void getInstanceSettings() {
        System.out.println(Config.getInstance().CONNECTION_SETTINGS);
        Assert.assertEquals(Config.getInstance().USER, "root");
    }
}