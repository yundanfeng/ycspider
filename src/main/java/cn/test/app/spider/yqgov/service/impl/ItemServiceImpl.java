package cn.test.app.spider.yqgov.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import cn.test.app.spider.yqgov.HttpService;
import cn.test.app.spider.yqgov.controller.resp.ItemVO;
import cn.test.app.spider.yqgov.dao.IColumnInfoRepo;
import cn.test.app.spider.yqgov.dao.IItemInfoRepo;
import cn.test.app.spider.yqgov.entity.ColumnInfo;
import cn.test.app.spider.yqgov.entity.ItemInfo;
import cn.test.app.spider.yqgov.global.Globals;
import cn.test.app.spider.yqgov.global.MyException;
import cn.test.app.spider.yqgov.pipeline.BMXXListPipeline;
import cn.test.app.spider.yqgov.pipeline.DWXXListPipeline;
import cn.test.app.spider.yqgov.pipeline.XXGKListPipeline;
import cn.test.app.spider.yqgov.pipeline.YQGOVListPipeline;
import cn.test.app.spider.yqgov.service.IItemService;
import cn.test.app.spider.yqgov.spider.BMXXListSpider;
import cn.test.app.spider.yqgov.spider.DWXXListSpider;
import cn.test.app.spider.yqgov.spider.XXGKListSpider;
import cn.test.app.spider.yqgov.spider.YQGOVListSpider;
import cn.test.app.spider.yqgov.spider.bean.ArticleItem;
import cn.test.app.spider.yqgov.task.AsyncTask;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月27日 下午2:10:06
 */
@Service
public class ItemServiceImpl implements IItemService {

	private static Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
	
	@Resource
	private IItemInfoRepo itemRepo;
	
	@Resource
	private IColumnInfoRepo columnRepo;

	@Resource
	private HttpService httpService;
	
	@Resource
	private AsyncTask task;
	
	@Override
	public void getType1ColumnItems(Request request, List<ArticleItem> items, Integer totalPage, Integer pageNo, Integer infocount) throws MyException {
	    if (Globals.isNull(items) || items.isEmpty()) {
			logger.info("by luzh-> getType1ColumnItems: column {} columnItemTotal or List<ArticleItem> items is null!", getRequestParam(request, "key"));
			return;
		}
		int itemCount = itemRepo.countByParent(getRequestParam(request, "key"));
		// 如果为空，则直接跳转到最后一页
		if (0 == itemCount 
		        && totalPage > 1
		        && !request.getUrl().contains("index_" + (totalPage > 10 ? 9 : totalPage - 1 )) 
		        && !request.getExtras().containsKey("add")) {
            request.setUrl(request.getUrl() + "index_" + (totalPage > 10 ? 9 : totalPage - 1) + ".shtml");
            Spider.create(new YQGOVListSpider()).addPipeline(new YQGOVListPipeline()).addRequest(request).start();
            return;
        }
		
		// 如果爬取的最后一条新闻数据库中没有，则去下一页爬取，直到第十页
		if (itemRepo.findByUrl(items.get(items.size() - 1).getHref()).isEmpty()
            && !request.getUrl().contains("index_" + (totalPage > 10 ? 9 : totalPage - 1)) 
            && !request.getExtras().containsKey("add")) {
		    if (request.getUrl().contains("index")) {
		        request.setUrl(
	                    Globals.replaceText(
	                            request.getUrl(), 
	                            "index_\\d+", 
	                            "index_" + (Integer.valueOf(Globals.regexString(request.getUrl(), "index_\\d+").get(0).substring(6)) + 1)
	                    )
	                );
            } else {
                request.setUrl(request.getUrl() + "index_1.shtml");
            }
		    Spider.create(new YQGOVListSpider()).addPipeline(new YQGOVListPipeline()).addRequest(request).start();
            return;
        }
		// 如果爬到的列表的第一条已经存在在数据库中，那么该页的数据也全部存在了，爬前一页数据，并作个标记
        if (!itemRepo.findByUrl(items.get(0).getHref()).isEmpty() && !request.getUrl().contains("index_1.shtml") && request.getUrl().contains("/index")) {
            request.getExtras().put("add", "addThis");
            request.setUrl(
                Globals.replaceText(
                        request.getUrl(), 
                        "index_\\d+", 
                        "index_" + (Integer.valueOf(Globals.regexString(request.getUrl(), "index_\\d+").get(0).substring(6)) - 1)
                )
            );
            Spider.create(new YQGOVListSpider()).addPipeline(new YQGOVListPipeline()).addRequest(request).start();
            return;
        }
        if (!itemRepo.findByUrl(items.get(0).getHref()).isEmpty() && request.getUrl().contains("index_1.shtml")) {
            request.getExtras().put("add", "addThis");
            request.setUrl(request.getUrl().replace("index_1.shtml", "index.shtml"));
            Spider.create(new YQGOVListSpider()).addPipeline(new YQGOVListPipeline()).addRequest(request).start();
            return;
        }
        for (int i = items.size() - 1; i >= 0; i--) {
            if (itemRepo.findByUrl(items.get(i).getHref()).isEmpty()) {
                itemRepo.save(new ItemInfo(items.get(i).getTitle(), items.get(i).getHref(), false, getRequestParam(request, "key"), null, Globals.TYPE1, false));
                logger.info("by luzh-> column {} add item {}.", getRequestParam(request, "key"), items.get(i).getTitle());
            }
        }

	}

	@Override
	public void getType2ColumnItems(Request request, List<ArticleItem> items, Integer totalPage, Integer pageNo, Integer infocount) throws MyException {
	    if (Globals.isNull(items) || items.isEmpty()) {
			logger.info("by luzh-> getType2ColumnItems: column {} List<ArticleItem> items is null!", getRequestParam(request, "key"));
			return;
		}
		int itemCount = itemRepo.countByParent(getRequestParam(request, "key"));
		// 如果为空，直接跳转到第20页
		if (0 == itemCount 
				&& totalPage > 1 
				&& !request.getUrl().contains("list_" + (totalPage > 10 ? 10 : totalPage)) 
				&& !request.getExtras().containsKey("add")) {
			 request.setUrl(Globals.replaceText(request.getUrl(), "list_\\d+", "list_" + (totalPage > 10 ? 10 : totalPage)));
			 Spider.create(new DWXXListSpider()).addRequest(request).addPipeline(new DWXXListPipeline()).start();
			 return;
		}
		// 如果爬取到的列表的最后一条不存在在数据库中，则去下一页查找,直到第10页
		if (itemRepo.findByUrl(items.get(items.size() - 1).getHref()).isEmpty()
				&& !request.getUrl().contains("list_" + (totalPage > 10 ? 10 : totalPage)) 
				&& !request.getExtras().containsKey("add")) {
			request.setUrl(
				Globals.replaceText(
						request.getUrl(), 
						"list_\\d+", 
						"list_" + (Integer.valueOf(Globals.regexString(request.getUrl(), "list_\\d+").get(0).substring(5)) + 1)
				)
			);
			Spider.create(new DWXXListSpider()).addRequest(request).addPipeline(new DWXXListPipeline()).start();
			return;
		}
		// 如果爬到的列表的第一条已经存在在数据库中，那么该页的数据也全部存在了，爬前一页数据，并作个标记
        if (!itemRepo.findByUrl(items.get(0).getHref()).isEmpty() && !request.getUrl().contains("list_1.html")) {
            request.getExtras().put("add", "addThis");
            request.setUrl(
                Globals.replaceText(
                        request.getUrl(), 
                        "list_\\d+", 
                        "list_" + (Integer.valueOf(Globals.regexString(request.getUrl(), "list_\\d+").get(0).substring(5)) - 1)
                )
            );
            Spider.create(new DWXXListSpider()).addRequest(request).addPipeline(new DWXXListPipeline()).start();
            return;
        }
//        if (!itemRepo.findByUrl(items.get(0).getHref()).isEmpty() && request.getUrl().contains("list_1.html")) {
//            return;
//        }
		for (int i = items.size() - 1; i >= 0; i--) {
			if (itemRepo.findByUrl(items.get(i).getHref()).isEmpty()) {
				itemRepo.save(new ItemInfo(items.get(i).getTitle(), items.get(i).getHref(), false, getRequestParam(request, "key"), null, Globals.TYPE2, false));
				logger.info("by luzh-> column {} add item {}.", getRequestParam(request, "key"), items.get(i).getTitle());
			}
		}
	}

	@Override
	public void getType3ColumnItems(Request request, List<ArticleItem> items, Integer pages) throws MyException {
		if (Globals.isNull(items) || items.isEmpty()) {
			logger.info("by luzh-> getType3ColumnItems: column {} List<ArticleItem> items is null!", getRequestParam(request, "key"));
			return;
		}
		int itemCount = itemRepo.countByParent(getRequestParam(request, "key"));
		// 第一次爬取，并且存在多于一页的情况，直接跳转到最后一页
		if (0 == itemCount && pages > 1 && !request.getExtras().containsKey("add")) {
			request.getExtras().put("add", "addThis");
			request.setUrl(Globals.replaceText(request.getUrl(), "&page=1", "&page="+pages));
			Spider.create(new BMXXListSpider()).addPipeline(new BMXXListPipeline()).addRequest(request).start();
			return;
		}
		// 当前列表中的最后一条数据已经存在在数据库中
		if (itemRepo.findByUrl(items.get(items.size() - 1).getHref()).isEmpty() 
				&& pages > 1 
				&& Integer.valueOf(request.getUrl().split("page=")[1]) < pages
				&& !request.getExtras().containsKey("add")) {
			request.setUrl(Globals.replaceText(request.getUrl(), "&page=\\d+", "&page=" + (Integer.valueOf(request.getUrl().split("page=")[1]) + 1)));
			Spider.create(new BMXXListSpider()).addPipeline(new BMXXListPipeline()).addRequest(request).start();
			return;
		}
		// 如果爬到的列表的第一条已经存在在数据库中，那么该页的数据也全部存在了，爬前一页数据，并作个标记
		if (!itemRepo.findByUrl(items.get(0).getHref()).isEmpty() && !request.getUrl().contains("page=1")) {
			request.getExtras().put("add", "addThis");
			request.setUrl(Globals.replaceText(request.getUrl(), "&page=\\d+", "&page=" + (Integer.valueOf(request.getUrl().split("page=")[1]) - 1)));
			Spider.create(new BMXXListSpider()).addPipeline(new BMXXListPipeline()).addRequest(request).start();
			return;
		}
		if (!itemRepo.findByUrl(items.get(0).getHref()).isEmpty() && request.getUrl().split("page=")[1].equals("1")) {
			return;
		}
		for (int i = items.size() - 1; i >=0; i--) {
			if (itemRepo.findByUrl(items.get(i).getHref()).isEmpty()) {
				itemRepo.save(new ItemInfo(items.get(i).getTitle(), items.get(i).getHref(), false, getRequestParam(request, "key"), null, Globals.TYPE3, false));
				logger.info("by luzh-> column {} add item {}.", getRequestParam(request, "key"), items.get(i).getTitle());
			}
		}
	}

	@Override
	public void getType4ColumnItems(Request request, List<ArticleItem> items, Integer pages) throws MyException {
	    if (Globals.isNull(items) || items.isEmpty()) {
			logger.info("by luzh-> getType4ColumnItems: column {} List<ArticleItem> items is null!", getRequestParam(request, "key"));
			return;
		}
		int itemCount = itemRepo.countByParent(getRequestParam(request, "key"));
		// 第一次爬取，并且存在多于一页的情况，直接跳转到最后一页
		if (0 == itemCount && pages > 1 && !request.getExtras().containsKey("add")) {
			request.getExtras().put("add", "addThis");
			request.setUrl(Globals.replaceText(request.getUrl(), "&page=1", "&page="+pages));
			Spider.create(new XXGKListSpider()).addPipeline(new XXGKListPipeline()).addRequest(request).start();
			return;
		}
		// 当前列表中的最后一条数据已经存在在数据库中
		if (itemRepo.findByUrl(items.get(items.size() - 1).getHref()).isEmpty()
				&& pages > 1 
				&& Integer.valueOf(request.getUrl().split("page=")[1]) < pages
				&& !request.getExtras().containsKey("add")) {
			request.setUrl(Globals.replaceText(request.getUrl(), "&page=\\d+", "&page=" + (Integer.valueOf(request.getUrl().split("page=")[1]) + 1)));
			Spider.create(new XXGKListSpider()).addPipeline(new XXGKListPipeline()).addRequest(request).start();
			return;
		}
		// 如果爬到的列表的第一条已经存在在数据库中，那么该页的数据也全部存在了，爬前一页数据，并作个标记
		if (!itemRepo.findByUrl(items.get(0).getHref()).isEmpty() && !request.getUrl().contains("page=1")) {
			request.getExtras().put("add", "addThis");
			request.setUrl(Globals.replaceText(request.getUrl(), "&page=\\d+", "&page=" + (Integer.valueOf(request.getUrl().split("page=")[1]) - 1)));
			Spider.create(new XXGKListSpider()).addPipeline(new XXGKListPipeline()).addRequest(request).start();
			return;
		}
		if (!itemRepo.findByUrl(items.get(0).getHref()).isEmpty() && request.getUrl().split("page=")[1].equals("1")) {
			return;
		}
		for (int i = items.size() - 1; i >=0; i--) {
			if (itemRepo.findByUrl(items.get(i).getHref()).isEmpty()) {
				itemRepo.save(new ItemInfo(items.get(i).getTitle(), items.get(i).getHref(), false, getRequestParam(request, "key"), null, Globals.TYPE3, false));
				logger.info("by luzh-> column {} add item {}.", getRequestParam(request, "key"), items.get(i).getTitle());
			}
		}
	}
	
	public static String getRequestParam(Request request, String key) {
		return request.getExtra(key).toString();
	}
	
	@Override
	public List<ItemInfo> findByFinishAndParent(String parent, Boolean finish, Integer size) {
		// 从item表里面取出为未爬取的十条数据进行爬取
		PageRequest page = new PageRequest(0, size, new Sort(Direction.ASC, "id"));
		return itemRepo.findByFinishAndParent(finish, parent, page);
	}
	
	@Override
	public void startCrawl() {
		List<ColumnInfo> columns = columnRepo.findAll();
		for (ColumnInfo info : columns) {
			task.getArticle(info.getType(), info.getKey());
		}
	}
	
	@Override
	public void startCrawl(String parent, PageProcessor pageProcessor, Pipeline pipeline, int waitTime, Integer crawlSize) {
		List<ItemInfo> itemInfos = findByFinishAndParent(parent, false, crawlSize);
		for (ItemInfo item : itemInfos) {
			Globals.waitAMoment(waitTime);
			int status = 0;
			try {
				// 检测一次状态，如果检测状态出错，则为网络不通，退出此次爬取。
				status = checkStatus(httpService, item, itemRepo);
			} catch (MyException e) {
				logger.error("by luzh-> startCrawl: check item {} status error {}.", item.getTitle(), e.getMessage(), e);
				continue;
			}
			if (status != 200) {
				continue;
			}
			Globals.waitAMoment(waitTime);
			try {
				logger.info("by luzh-> startCrawl: carwl atricle {} start.", item.getTitle());
				Spider.create(pageProcessor).addPipeline(pipeline).addUrl(item.getUrl()).start();
			} catch (Exception e) {
				logger.error("by luzh-> startCrawl: crawl article {} error {}.", item.getTitle(), e.getMessage(), e);
				return;
			}
		}
	}
	
	public static int checkStatus (HttpService httpService, ItemInfo item, IItemInfoRepo itemRepo) throws MyException {
		int status = 0;
		boolean flag = false;
		try {
			status = httpService.getStatus(item.getUrl());
			if (200 != status) {
				flag = true;
			}
		} catch (Exception e) {
			// 报错说明出现问题，停止爬取
			throw new MyException(e.getMessage());
		}
		itemRepo.updateItemInfosByIdOrUrl(status, flag, System.currentTimeMillis(), item.getId(), null);
		logger.info("by luzh-> startCrawl: item {} url {} status is {}.", item.getTitle(), item.getUrl(), status);
		return status;
	}
	
	@Override
	public Map<String, Object> getItems(String key) {
		Map<String, Object> result = new HashMap<>();
		result.put("type", key);
		List<ItemInfo> items = itemRepo.findByParent(key);
		if (items.isEmpty()) {
			result.put("total", 0);
			return result;
		}
		List<ItemVO> vos = new ArrayList<>();
		for (ItemInfo info : items) {
			vos.add(new ItemVO(info));
		}
		result.put("total", items.size());
		result.put("rows", vos);
		return result;
	}
	
	@Override
	public void setStatusAndFinish(String url, int statusCode, boolean finish) {
	    ItemInfo itemInfo = itemRepo.findByUrl(url).get(0);
	    itemInfo.setStatus(statusCode);
	    itemInfo.setFinish(finish);
	    itemRepo.save(itemInfo);
	}
}
