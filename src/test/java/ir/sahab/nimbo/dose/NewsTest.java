package ir.sahab.nimbo.dose;

import ir.sahab.nimbo.dose.database.DbHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class NewsTest {
    private Site site;
    private News news;

    private String text = "ارتش سوریه دایره کنترل خود را بر مناطق غربی درعا و در حومه استان قنیطره گسترش داده و یک روستا و ارتفاعات استراتژیک آنجا را آزاد کرد. به گزارش ایسنا، به نقل از خبرگزاری رسمی سوریه (سانا)، ارتش سوریه به عملیات نظامی خود علیه پایگاهها و مناطق استقرار گروههای شبهنظامی در حومه شمال غربی درعا واقع در مجاورت استان قنیطره ادامه میدهد. یک منبع نظامی به این خبرگزاری گزارش داد، نیروهای سوریه دایره کنترل خود را با آزادسازی روستای المال و ارتفاعات این روستا گسترش دادند. به گفته این منبع نظامی، یگانهایی از ارتش این کشور با همکاری نیروهای همپیمان خود عملیاتی نظامی را در حومه شمال غربی درعا آغاز کرده و پس از نابودی تعدادی از تروریستها و مصادره و انهدام سلاح و تجهیزاتشان، کنترل ارتفاعات استراتژیک الحاره و روستاهای کفر شمس، ام العوسج، الطیحه و زمرین در حومه شمال غربی درعا را به دست گرفتند. تازهترین نقشه میدانی جنوب سوریه/ نقاط سبز تنها مناطقی هستند که همچنان در کنترل شورشیان سوری باقی مانده است با کنترل ارتش سوریه بر ارتفاعات استراتژیک الحاره دستاورد مهمی برای پیشروی در جبهه قنیطره در مجاورت جولان اشغالی به دست آمده است. همچنین ارتش کنترل روستای العالیه واقع در غرب شهر جاسم در حومه شمال غربی درعا را به دست گرفت. همزمان با پیشروی ارتش سوریه در حومه قنیطره تعدادی از گروههای شبهنظامی به همراه خانوادههایشان مجبور به حرکت به سمت مرزهای جولان اشغالی شدند. رسانههای عبری زبان نیز اعلام کردند: ارتش اسرائیل از این افراد خواسته است که به عقب بازگردند اما دهها تن از آنها با تجمع در نزدیکی مرزهای سوریه با جولان اشغالی پرچم سفید در دست داشته و در فاصله ۲۰۰ متری حصار مرزی با اسرائیل توقف کردهاند. انتهای پیام لینک کوتاه";

    @Before
    public void cleanDatabase() throws SQLException {
        DbHandler.getInstance().cleanDatabase();
    }

    @Before
    public void loadSite() {

        String url = "https://www.isna.ir/news/97042714764/%DA%AF%D8%B3%D8%AA%D8%B1%D8%B4-%D8%AF%D8%A7%DB%8C%D8%B1%D9%87-%D9%86%D9%81%D9%88%D8%B0-%D8%A7%D8%B1%D8%AA%D8%B4-%D8%B3%D9%88%D8%B1%DB%8C%D9%87-%D8%A8%D8%B1-%D8%A7%D8%B3%D8%AA%D8%A7%D9%86-%D9%87%D8%A7%DB%8C-%D8%AC%D9%86%D9%88%D8%A8%DB%8C-%D9%BE%DB%8C%D8%B4%D8%B1%D9%88%DB%8C-%D8%A8%D9%87-%D8%B3%D9%85%D8%AA";
        String title = "گسترش دایره نفوذ ارتش سوریه بر استان\u200Cهای جنوبی/ پیشروی به سمت جولان اشغالی ادامه دارد";
        String date = "Wed, 18 Jul 2018 04:58:53 GMT";
        String siteName = "ISNA";
        String rssFeedUrl = "http://isna.ir/rss";

        site = new Site(siteName, rssFeedUrl, "div", "class", "item-body content-full-news");
        news = new News(url, title, date, siteName);
    }

    @Test
    public void fetch() {
        site.addToDb();
        news.fetch();
        Assert.assertEquals(news.getText(), text);
    }


    @Test
    public void addToDb() {
        site.addToDb();
        news.fetch();
        news.addToDb();
        //TODO: set limit to -1
        List<News> newsBySiteDate = DbHandler.getInstance().getNewsBySite(news.getSiteName(), 1);
        Assert.assertTrue(newsBySiteDate.contains(news));
    }
}
