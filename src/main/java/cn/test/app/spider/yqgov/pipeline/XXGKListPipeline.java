package cn.test.app.spider.yqgov.pipeline;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.test.app.spider.yqgov.SpringUtil;
import cn.test.app.spider.yqgov.service.IItemService;
import cn.test.app.spider.yqgov.spider.bean.ArticleItem;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月26日 下午4:50:32
 */
public class XXGKListPipeline implements Pipeline {

	private IItemService service = SpringUtil.getBean(IItemService.class);

	private static Logger logger = LoggerFactory.getLogger(XXGKListPipeline.class);

	@Override
	public void process(ResultItems items, Task task) {
		try {
			List<ArticleItem> articleItems = items.get("items");
			service.getType4ColumnItems(
					items.getRequest(), 
					articleItems, 
					Integer.valueOf(items.get("pages").toString())
			);
		} catch (Exception e) {
			logger.error("by luzh-> XXGKListPipelineError  {}.", items.getRequest().getUrl(), e);
		}
	}
}
