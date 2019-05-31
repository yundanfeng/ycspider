package cn.test.app.spider.yqgov.spider;

import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
public class BMXXArticleSpider implements PageProcessor{

	private Site site = Site.me().setRetryTimes(5).setSleepTime(200).setUserAgent(Globals.USERAGENT);
	
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		if (!Globals.isNull(page.getHtml().xpath("//div[@class='showMsg']").get())) {
			page.putField("date", "");
			page.putField("title", "");
			page.putField("content", "404");
			page.putField("images", "");
			return;
		}
		page.putField("date", Globals.regexString(page.getHtml().xpath("//dd[@class='listinfo']/text()").get(), Globals.FULLTIMEREG).get(0));
		page.putField("title", page.getHtml().xpath("//dd[@class='title']/text()").get());
		Document content = Jsoup.parse(page.getHtml().xpath("//div[@class='showcon']").$(".content").get());
		Map<String, Object> result = Globals.manageContentImageAndExtractImageUrl(content.html().replaceAll("/statics/js/kindeditor/php/../../../..", ""), "http://www.yqdtv.com", "src", "/uploadfile", "http://", "file://", ",");
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
		Spider.create(new BMXXArticleSpider()).addUrl("http://www.yqdtv.com/index.php?m=content&c=index&a=show&catid=40&id=1498").start();
	}
}
