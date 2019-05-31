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
 * @Description: TODO
 * @author luzh
 * @createTime 2017年12月25日 上午11:30:23
 */
public class YQGOVListPipeline implements Pipeline {

    private IItemService service = SpringUtil.getBean(IItemService.class);

    private static Logger logger = LoggerFactory.getLogger(YQGOVListPipeline.class);

    @Override
    public void process(ResultItems items, Task task) {
        try {
            List<ArticleItem> articleItems = items.get("items");
            service.getType1ColumnItems(
                    items.getRequest(),
                    articleItems,
                    Integer.parseInt(items.get("totalPage").toString()),
                    Integer.parseInt(items.get("pageNo").toString() + 1),
                    articleItems.size()
            );
        } catch (Exception e) {
            logger.error("by luzh-> YGZWListPipelineError  {}.", items.getRequest().getUrl(), e);
        }
    }
}
