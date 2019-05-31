package cn.test.app.spider.yqgov.spider;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.test.app.spider.yqgov.global.Globals;
import cn.test.app.spider.yqgov.spider.bean.ArticleItem;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @Description: TODO
 * @author luzh
 * @createTime 2017年12月25日 上午10:01:12
 */
public class YQGOVListSpider implements PageProcessor {
    
    private Site site = Site.me().setRetryTimes(5).setSleepTime(200).setUserAgent(Globals.USERAGENT).setTimeOut(60 * 1000);

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        Document document = Jsoup.parse(page.getHtml().xpath("//div[@class='nrlb-right right']").get());
        String script = document.select("script").html();
        page.putField("totalPage", script.split("\n")[3].split("=")[1].split("//")[0].trim());
        page.putField("pageNo", script.split("\n")[0].split("=")[1].split(";")[0].trim());
        Elements ulLs = document.select("ul.nrlb-text-list");
        List<ArticleItem> items = new ArrayList<>();
        for (Element ul : ulLs) {
           Elements aLs = ul.select("a");
           for (Element a : aLs) {
               items.add(new ArticleItem(a.attr("title"), a.attr("href")));
           }
        }
        page.putField("items", items);
    }
    
    public static void main(String[] args) {
        Spider.create(new YQGOVListSpider()).addUrl(new String[] {
                "http://www.yq.gov.cn/zwgk/zfwj/index_21.shtml",
                "http://www.yq.gov.cn/zwgk/gsgg/index_12.shtml",
                "http://www.yq.gov.cn/ywdt/bmgz/index_18.shtml"}).start();
    }

}
