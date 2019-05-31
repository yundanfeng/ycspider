package cn.test.app.spider.yqgov.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import cn.test.app.spider.yqgov.SpringUtil;
import cn.test.app.spider.yqgov.service.IArticleService;
import cn.test.app.spider.yqgov.service.IItemService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月27日 下午2:08:39
 */
public class ArticlePipeline implements Pipeline {

	private IArticleService articleService = SpringUtil.getBean(IArticleService.class);
	private IItemService itemService = SpringUtil.getBean(IItemService.class);
	
	private static Logger logger = LoggerFactory.getLogger(ArticlePipeline.class);

	@Override
	public void process(ResultItems resultItems, Task task) {
		try {
		    // nometa  表示不是正确的新闻页面，标记非新闻页状态
		    if (!StringUtils.isEmpty(resultItems.get("nometa"))) {
                itemService.setStatusAndFinish(resultItems.getRequest().getUrl(), 201, true);
                return;
            }
			articleService.addArticle(
					resultItems.getRequest().getUrl(), 
					resultItems.get("title").toString(),
					resultItems.get("date").toString(), 
					resultItems.get("content").toString(),
					resultItems.get("images").toString()
			);
		} catch (Exception e) {
			logger.error("by luzh-> ArticlePipeline: article 【{}】error." , resultItems.getRequest().getUrl(),e);
			return;
		}
	}

}
