package cn.test.app.spider.yqgov.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.test.app.spider.yqgov.dao.IArticleInfoRepo;
import cn.test.app.spider.yqgov.dao.IItemInfoRepo;
import cn.test.app.spider.yqgov.entity.ArticleInfo;
import cn.test.app.spider.yqgov.global.Globals;
import cn.test.app.spider.yqgov.service.IArticleService;
import cn.test.app.spider.yqgov.task.AsyncTask;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月27日 下午2:51:49
 */
@Service
public class ArticleServieImpl implements IArticleService{

	private static Logger logger = LoggerFactory.getLogger(ArticleServieImpl.class);
	
	@Resource
	private IArticleInfoRepo articleInfoRepo;
	
	@Resource 
	private IItemInfoRepo itemRepo;
	
	@Resource
	private AsyncTask task;
	
	@Override
	public void addArticle(String url, String title, String date, String content, String images) {
		if ("404".equals(content)) {
			itemRepo.updateItemInfosByIdOrUrl(404, true, System.currentTimeMillis(), null, url);
			logger.info("by luzh-> addArticle: url {} status is 404.", url);
			return;
		}
		ArticleInfo articleInfo = new ArticleInfo();
		List<ArticleInfo> infos = articleInfoRepo.findByUrl(url);
		if (null != infos && infos.size() > 0) {
			articleInfo = infos.get(0);
		}
		if (infos.size() > 1) {
			
		}
		Long dateL;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		articleInfo.setUrl(url);
		articleInfo.setTitle(title);
		articleInfo.setDate(null);
		articleInfo.setImages(Globals.isNull(images) ? null : images);
		articleInfo.setContent(content);
		try {
			dateL = sdf.parse(date).getTime();
			articleInfo.setDate(dateL);
			articleInfoRepo.save(articleInfo);
		} catch (Exception e) {
			articleInfoRepo.save(articleInfo);
			logger.error("by luzh-> article 【{}】 parse date {} error {}.", title, date, e.getMessage(), e);
		}
		logger.info("by luzh-> addArticle: title {}, url {}.", title, url);
		if (!Globals.isNull(images)) {
			task.downloadImage(url);
		} else {
			itemRepo.updateFinishByUrl(true, url);
		}
	}
}
