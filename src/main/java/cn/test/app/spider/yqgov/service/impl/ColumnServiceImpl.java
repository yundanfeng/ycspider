package cn.test.app.spider.yqgov.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.test.app.spider.yqgov.controller.resp.ColumnVO;
import cn.test.app.spider.yqgov.dao.IColumnInfoRepo;
import cn.test.app.spider.yqgov.dao.IItemInfoRepo;
import cn.test.app.spider.yqgov.entity.ColumnInfo;
import cn.test.app.spider.yqgov.global.Globals;
import cn.test.app.spider.yqgov.global.MyException;
import cn.test.app.spider.yqgov.pipeline.BMXXListPipeline;
import cn.test.app.spider.yqgov.pipeline.DWXXListPipeline;
import cn.test.app.spider.yqgov.pipeline.XXGKListPipeline;
import cn.test.app.spider.yqgov.pipeline.YQGOVListPipeline;
import cn.test.app.spider.yqgov.service.IColumnService;
import cn.test.app.spider.yqgov.spider.BMXXListSpider;
import cn.test.app.spider.yqgov.spider.DWXXListSpider;
import cn.test.app.spider.yqgov.spider.XXGKListSpider;
import cn.test.app.spider.yqgov.spider.YQGOVListSpider;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.utils.HttpConstant;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月26日 上午11:24:20
 */
@Service
public class ColumnServiceImpl implements IColumnService {

	private static Logger logger = LoggerFactory.getLogger(ColumnServiceImpl.class);

	@Resource
	private IColumnInfoRepo columnRepo;

	@Resource
	private IItemInfoRepo itemRepo;

	@Override
	public void addColumn(String name, String url, String type, String key, String hlabel, String website)
			throws MyException {
		ColumnInfo info = new ColumnInfo(null, name, url, type, key);
		info.setLabel(hlabel);
		info.setWebsite(website);
		columnRepo.save(info);
	}

	@Override
	public void getColumnItems() throws MyException {
		List<ColumnInfo> columns = columnRepo.findAll();
		for (ColumnInfo info : columns) {
			if (info.getType().equals(Globals.TYPE1)) {
				try {
					getType1ColumnItems(
							info.getKey(), 
							info.getUrl());
					logger.info("by luzh-> getType1ColumnItems: start crawl {} items task.", info.getName());
				} catch (Exception e) {
					logger.error("by luzh-> getType1ColumnItemsError {}.", e.getMessage(), e);
				}   
				continue;
			}
			if (info.getType().equals(Globals.TYPE2)) {
				try {
					getType2ColumnItems(info.getKey(), info.getUrl());
					logger.info("by luzh-> getType2ColumnItems: start crawl {} items task.", info.getName());
				} catch (Exception e) {
					logger.error("by luzh-> getType2ColumnItemsError {}.", e.getMessage(), e);
				}
				continue;
			}
			if (info.getType().equals(Globals.TYPE3)) {
				try {
					getType3ColumnItems(info.getKey(), info.getUrl());
					logger.info("by luzh-> getType3ColumnItems: start crawl {} items task.", info.getName());
				} catch (Exception e) {
					logger.error("by luzh-> getType3ColumnItemsError {}.", e.getMessage(), e);
				}
				continue;
			}
			if (info.getType().equals(Globals.TYPE4)) {
				try {
					getType4ColumnItems(info.getKey(), info.getUrl());
					logger.info("by luzh-> getType4ColumnItems: start crawl {} items task.", info.getName());
				} catch (Exception e) {
					logger.error("by luzh-> getType4ColumnItemsError {}.", e.getMessage(), e);
				}
				continue;
			}
		}
	}

	@Override
	public void getType1ColumnItems(String key, String url) throws MyException {
	    Map<String, String> param = new HashMap<>();
        param.put("key", key);
		Spider.create(new YQGOVListSpider()).addPipeline(new YQGOVListPipeline()).addRequest(createRequest(null, param, url, HttpConstant.Method.GET)).start();
	}

	@Override
	public void getType2ColumnItems(String key, String url) throws MyException {
		Map<String, String> param = new HashMap<>();
		param.put("key", key);
		Spider.create(new DWXXListSpider()).addPipeline(new DWXXListPipeline()).addRequest(createRequest(null, param, url, HttpConstant.Method.GET)).start();
	}
	
	@Override
	public void getType3ColumnItems(String key, String url) throws MyException{
		Map<String, String> param = new HashMap<>();
		param.put("key", key);
		Spider.create(new BMXXListSpider()).addPipeline(new BMXXListPipeline()).addRequest(createRequest(null, param, url+"&page=1", HttpConstant.Method.GET)).start();
	}
	
	@Override
	public void getType4ColumnItems(String key, String url) throws MyException{
		Map<String, String> param = new HashMap<>();
		param.put("key", key);
		Spider.create(new XXGKListSpider()).addPipeline(new XXGKListPipeline()).addRequest(createRequest(null, param, url+"&page=1", HttpConstant.Method.GET)).start();
	}
	
	public static Request createRequest(Map<String, String> nameValuePair, Map<String, String> param, String url, String method) {
		// 设置get请求
		Request request = new Request(url);
		request.setMethod(method);
		Map<String, Object> params = new HashMap<>();
		// 设置post参数
		if (null != nameValuePair) {
			List<NameValuePair> nvp = new ArrayList<>();
			for (Map.Entry<String, String> entry : nameValuePair.entrySet()) {
				nvp.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			// 转换为键值对数组
			NameValuePair[] values = nvp.toArray(new NameValuePair[] {});
			// 将键值对数组添加到map中
			// key必须是：nameValuePair
			params.put("nameValuePair", values);
		}
		if (null != param) {
			for (Map.Entry<String, String> entry : param.entrySet()) { 
				params.put(entry.getKey(), entry.getValue());
			}
		}
		// 设置request参数
		request.setExtras(params);
		return request;
	}
	
	@Override
	public List<ColumnVO> getAllColumns() {
		List<ColumnInfo> infos = columnRepo.findAll();
		List<ColumnVO> vos = new ArrayList<>();
		for (ColumnInfo info : infos) {
			vos.add(new ColumnVO(info.getName(), info.getKey()));
		}
		return vos;
	}
}
