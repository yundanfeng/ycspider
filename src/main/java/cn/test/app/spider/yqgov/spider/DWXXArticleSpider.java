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
public class DWXXArticleSpider implements PageProcessor{

	private Site site = Site.me().setRetryTimes(5).setSleepTime(200).setUserAgent(Globals.USERAGENT);
	
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		page.putField("date", Globals.regexString(page.getHtml().xpath("//div[@class='author']/text()").get(), Globals.TIMEREG).get(0) + " 00:00:00");
		page.putField("title", page.getHtml().xpath("//H1[@class='title']/text()").get());
		Document content = Jsoup.parse(page.getHtml().xpath("//span[@id='BodyLabel']").get());
		Map<String, Object> result = Globals.manageContentImageAndExtractImageUrl(content.html(), "http://www.yqdj.gov.cn", "src", "/W01,/cms", "http://", "", ",");
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
		Spider.create(new DWXXArticleSpider()).addUrl("http://www.yqdj.gov.cn/W01/W01002/2017_07/28/115d86120c7b909.html").start();
	}
}
