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
import us.codecraft.webmagic.processor.PageProcessor;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月24日 上午10:28:02
 */
public class XXGKListSpider implements PageProcessor{
	
	private Site site = Site.me().setRetryTimes(5).setSleepTime(200).setUserAgent(Globals.USERAGENT);

	@Override
	public Site getSite() {
		return site;
	}
	
	@Override
	public void process(Page page) {
		Elements span = Jsoup.parse(page.getHtml().xpath("//span[@id='fd_page_bottom']").get()).select("label").select("span");
		page.putField("pages", Globals.regexString(span.html(), "\\d+").get(0));
		Document table = Jsoup.parse(page.getHtml().xpath("//table[@id='threadlisttableid']").get());
		Elements a = table.select("a");
		List<ArticleItem> items = new ArrayList<>();
		for (Element e : a) {
			if (e.attr("onclick").equals("atarget(this)")) {
				items.add(new ArticleItem(e.ownText(), e.attr("href")));
			}
		}
		page.putField("items", items);
	}
}
