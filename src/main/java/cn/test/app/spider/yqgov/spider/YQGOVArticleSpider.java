package cn.test.app.spider.yqgov.spider;

import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;

import cn.test.app.spider.yqgov.global.Globals;
import cn.test.app.spider.yqgov.spider.bean.ImageTmp;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @Description: TODO
 * @author luzh
 * @createTime 2017年12月25日 上午10:28:39
 */
public class YQGOVArticleSpider implements PageProcessor {

    private Site site = Site.me().setRetryTimes(5).setSleepTime(200).setUserAgent(Globals.USERAGENT);
    
    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        if(StringUtils.isEmpty(page.getHtml().xpath("//meta[@name='SiteDomain']/@content").get())
                && StringUtils.isEmpty(page.getHtml().xpath("//meta[@name='ArticleTitle']/@content").get())
                && StringUtils.isEmpty(page.getHtml().xpath("//meta[@name='PubDate']/@content").get())) {
            page.putField("nometa", "no SiteDomain、ArticleTitle、PubDate.");
            return;
        }
        String date = page.getHtml().xpath("//meta[@name='PubDate']/@content").get();
        String yearAndMonth = date.split("-")[0] + date.split("-")[1];
        String urlPerfix = page.getRequest().getUrl().split(yearAndMonth)[0] + yearAndMonth;
        page.putField("date", date);
        page.putField("title", page.getHtml().xpath("//meta[@name='ArticleTitle']/@content").get());
        Document content = Jsoup.parse(page.getHtml().xpath("//div[@class='content-body']").get());
        page.putField("content", content.html());
        Map<String, Object> result = Globals.manageContentImageAndExtractImageUrl(content.html().replaceAll("\\./", ""), urlPerfix + "/", "src", "W0", "http://", "", ",");
        content = (Document) result.get("content");
        @SuppressWarnings("unchecked")
        List<ImageTmp> imageTmps = (List<ImageTmp>) result.get("images");
        String contentStr = content.html();
        StringBuffer images = new StringBuffer();
        for (ImageTmp tmp : imageTmps) {
            if (tmp.getHasHost()) {
                contentStr = contentStr.replaceAll("=\""+tmp.getOld(), "=\""+tmp.getRight());
            }
            images.append(Globals.IMAGE_SPLIT).append(tmp.getRight());
        }
        page.putField("content", Jsoup.parse(contentStr).html());
        page.putField("images", images.toString().length() > Globals.IMAGE_SPLIT.length() ? images.toString().substring(Globals.IMAGE_SPLIT.length()) : images.toString());
        
    }

    public static void main(String[] args) {
        Spider.create(new YQGOVArticleSpider()).addUrl(new String[]{
                "http://xxgk.yq.gov.cn/zfbgt/fgwj/gfxwj/201712/t20171208_509292.shtml"
        }).start();
    }
}
