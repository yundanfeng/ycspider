package cn.test.app.spider.yqgov.spider;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.test.app.spider.yqgov.global.Globals;
import cn.test.app.spider.yqgov.spider.bean.ArticleItem;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月27日 上午9:53:40
 */
public class BMXXListSpider implements PageProcessor{

	private Site site = Site.me().setRetryTimes(5).setSleepTime(200).setCharset("UTF-8").setUserAgent(Globals.USERAGENT);
	
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		page.putField(
			"pages", 
			getPage(Jsoup.parse(page.getHtml().xpath("//div[@id='pages']").get()).select("a"))
		);
		List<ArticleItem> items = new ArrayList<>();
		Elements elements = Jsoup.parse( page.getHtml().xpath("//div[@class='col-left']").get()).select(".title");
		for (Element e : elements) {
			items.add(new ArticleItem(e.select("a").text(), e.select("a").attr("href")));
		}
		page.putField("items", items);
	}
	
	public static int getPage(Elements elements) {
		return elements.size() > 0 ? elements.size() - 2 : 1;
	}
	
	public static void main(String[] args) {
			Spider.create(new BMXXListSpider()).addUrl(new String[]{
					"http://www.yqdtv.com/index.php?m=content&c=index&a=lists&catid=62&page=1"
			}).thread(5).start();
	}

}
