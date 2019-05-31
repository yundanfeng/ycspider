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
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月31日 下午3:36:18
 */
public class DWXXListSpider implements PageProcessor {

	private Site site = Site.me().setRetryTimes(5).setSleepTime(200).setCharset("UTF-8").setUserAgent(Globals.USERAGENT);

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		Element table = Jsoup.parse(page.getHtml().get()).select("table").get(0);
		Elements elements = table.select("TR").select("A");
		List<ArticleItem> items = new ArrayList<>();
		for (Element e : elements) {
			if (!e.ownText().contains("下一页") && !e.ownText().contains("上一页") && !e.ownText().contains("尾页") && !e.ownText().contains("首页")) {
				items.add(new ArticleItem(e.ownText(), e.attr("href")));
			}
		}
		String totalPage = table.select("TD").get(1).data().split(";")[1].split("=")[1];
		String pageNo = table.select("TD").get(1).data().split(";")[2].split("=")[1];
		String infocount = table.select("TD").get(1).data().split(";")[3].split("=")[1];
		page.putField("totalPage", totalPage.trim());
		page.putField("pageNo", pageNo.trim());
		page.putField("infocount", infocount.trim());
		page.putField("items", items);
	}
}
