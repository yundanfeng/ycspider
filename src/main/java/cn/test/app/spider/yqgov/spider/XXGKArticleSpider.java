package cn.test.app.spider.yqgov.spider;

import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.test.app.spider.yqgov.global.Globals;
import cn.test.app.spider.yqgov.spider.bean.ImageTmp;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月27日 上午9:53:40
 */
public class XXGKArticleSpider implements PageProcessor{

	private Site site = Site.me().setRetryTimes(5).setSleepTime(200).setUserAgent(Globals.USERAGENT);
	
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		if (!Globals.isNull(page.getHtml().xpath("//div[@id='messagetext']").get())) {
			page.putField("date", "");
			page.putField("title", "");
			page.putField("content", "404");
			page.putField("images", "");
			return;
		}
		page.putField("date", Globals.regexString(Jsoup.parse(page.getHtml().get()).select("em[id]").toString(), Globals.FULLTIMEREG_HM).get(0) + ":00");
		page.putField("title", page.getHtml().xpath("//span[@id='thread_subject']/text()").get());
		Document content = Jsoup.parse(page.getHtml().xpath("//div[@class='t_fsz']").get());
		content.select(".tip_4").remove();
		content.select(".tip_c").remove();
		content.select("div.modact").remove();
		content.select("p.mbn").remove();
		Elements imgs = content.select("img");
		for (Element e : imgs) {
			e.attr("src", e.attr("zoomfile"));
			e.removeAttr("zoomfile");
			e.removeAttr("file");
			e.removeAttr("onclick");
			e.removeAttr("onmouseover");
		}
		Map<String, Object> result = Globals.manageContentImageAndExtractImageUrl(content.html(), "http://www.pp045000.com/", "src", "data/", "http://", "", ",");
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
		Spider.create(new XXGKArticleSpider()).addUrl("http://www.pp045000.com/forum.php?mod=viewthread&tid=46197&extra=page%3D2&_dsign=7223e0ce").start();
	}
}
