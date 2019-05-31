package cn.test.app.spider.yqgov.service;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import cn.test.app.spider.yqgov.entity.ItemInfo;
import cn.test.app.spider.yqgov.global.MyException;
import cn.test.app.spider.yqgov.spider.bean.ArticleItem;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月27日 下午2:10:25
 */
public interface IItemService {

	Gson gson = new Gson();

	void getType1ColumnItems(Request request, List<ArticleItem> items, Integer totalPage, Integer pageNo, Integer infocount) throws MyException;

	void getType2ColumnItems(Request request, List<ArticleItem> items, Integer totalPage, Integer pageNo, Integer infocount) throws MyException;

	void getType3ColumnItems(Request request, List<ArticleItem> items, Integer pages) throws MyException;

	void getType4ColumnItems(Request request, List<ArticleItem> items, Integer pages) throws MyException;

	List<ItemInfo> findByFinishAndParent(String parent, Boolean finish, Integer size);

	/**
	 * 根据数据库中的新闻列表进行逐条爬取
	 * @remark
	 * @author luzh
	 * @reateTime 2017年7月27日 下午2:50:08
	 * @param
	 * @return: void
	 */
	void startCrawl();

	/**
	 * @remark  通用的爬取文章方法
	 * @author luzh
	 * @reateTime 2017年8月3日 下午7:09:35
	 * @param @param parent 栏目key
	 * @param @param pageProcessor 爬虫主程序
	 * @param @param pipeline 处理数据pipeline
	 * @param @param waitTime 连续爬取等待时间（总共等待时间为该时间的两倍）
	 * @param @param crawlSize 一次性需要爬多少条数据
	 * @return: void    
	 */
	void startCrawl(String parent, PageProcessor pageProcessor, Pipeline pipeline, int waitTime, Integer crawlSize);

	Map<String, Object> getItems(String key);

    void setStatusAndFinish(String url, int statusCode, boolean finish);

}