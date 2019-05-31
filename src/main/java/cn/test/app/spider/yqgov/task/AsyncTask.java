package cn.test.app.spider.yqgov.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import cn.test.app.spider.yqgov.controller.req.ClipCallBackReq;
import cn.test.app.spider.yqgov.global.Globals;
import cn.test.app.spider.yqgov.pipeline.ArticlePipeline;
import cn.test.app.spider.yqgov.service.IColumnService;
import cn.test.app.spider.yqgov.service.IIMageService;
import cn.test.app.spider.yqgov.service.IItemService;
import cn.test.app.spider.yqgov.service.IPushService;
import cn.test.app.spider.yqgov.spider.BMXXArticleSpider;
import cn.test.app.spider.yqgov.spider.DWXXArticleSpider;
import cn.test.app.spider.yqgov.spider.XXGKArticleSpider;
import cn.test.app.spider.yqgov.spider.YQGOVArticleSpider;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @remark
 * @author luzh
 * @createTime 2017年6月23日 下午4:37:59
 */
@Component
public class AsyncTask {

	private static Logger logger = LoggerFactory.getLogger(AsyncTask.class);
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Resource
	private IColumnService columnService;

	@Resource
	private IItemService itemService;

	@Resource
	private IPushService pushService;
	
	@Resource
	private IIMageService imageService;

	// 设置了每隔十分钟爬取一次，所以需要算好每个不同类型一次性的爬取数量和时间间隔，不然列表后面的可能会先爬，导致注入homed之后顺序是乱的。
	@Async("myAsync")
	public void getArticle(String type, String key) {
		if (type.equals(Globals.TYPE1)) {
			getArticle(itemService, key, new YQGOVArticleSpider(), new ArticlePipeline(), 60, 4);
		}
		if (type.equals(Globals.TYPE2)) {
			getArticle(itemService, key, new DWXXArticleSpider(), new ArticlePipeline(), 60, 4);
		}
		if (type.equals(Globals.TYPE3)) {
			getArticle(itemService, key, new BMXXArticleSpider(), new ArticlePipeline(), 60, 4);
		}
		if (type.equals(Globals.TYPE4)) {
			getArticle(itemService, key, new XXGKArticleSpider(), new ArticlePipeline(), 60, 4);
		}
	}

	public static void getArticle(IItemService service, String parent, PageProcessor pageProcessor, Pipeline pipeline,
			int waitTime, Integer crawlSize) {
		logger.info("by luzh-> startCrawlColumn【{}】articles.", parent);
		try {
			service.startCrawl(parent, pageProcessor, pipeline, waitTime, crawlSize);
		} catch (Exception e) {
			logger.error("by luzh-> crawlColumn【{}】articles error {}.", parent, e.getMessage(), e);
			return;
		}
	}

	@Async("myAsync")
	public void getColumnItems() {
		logger.info("by luzh-> getColumnItems task start at time {}.", simpleDateFormat.format(new Date()));
		try {
			columnService.getColumnItems();
		} catch (Exception e) {
			logger.error("by luzh-> getColumnItems error {}.", e.getMessage(), e);
			return;
		}
	}

	@Async("myAsync")
	public void crawlArticles() {
		logger.info("by luzh-> crawlArticles task start at time {}.", simpleDateFormat.format(new Date()));
		try {
			itemService.startCrawl();
		} catch (Exception e) {
			logger.error("by luzh-> crawlArticles error {}.", e.getMessage(), e);
		}
	}

	@Async("myAsync")
	public void pushToHomed() {
		logger.info("by luzh-> pushToHomed task start at time {}.", simpleDateFormat.format(new Date()));
		try {
			pushService.push();
		} catch (Exception e) {
			logger.error("by luzh-> pushToHomed error {}.", e.getMessage(), e);
		}
	}
	
	@Async("myAsync")
	public void downloadImage(String url) {
		logger.info("by luzh-> url {} downloadImage task start at time {}.", url, simpleDateFormat.format(new Date()));
		try {
			imageService.downloadImage(url);
		} catch (Exception e) {
			logger.error("by luzh-> downloadImage error: url {} -> {}.", url, e.getMessage(), e);
		}
	}
	
	@Async("myAsync")
	public void clipCalBAck(ClipCallBackReq req) {
		logger.info("by luzh-> clipCalBAck start at time {}.", simpleDateFormat.format(new Date()));
		try {
			imageService.callBack(req);
		} catch (Exception e) {
			logger.error("by luzh-> clipCalBAck error: {}.", e.getMessage(), e);
		}
	}
}
