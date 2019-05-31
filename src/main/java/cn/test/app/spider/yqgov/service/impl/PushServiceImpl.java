package cn.test.app.spider.yqgov.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.UriEncoder;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import cn.test.app.spider.yqgov.HttpService;
import cn.test.app.spider.yqgov.dao.IArticleInfoRepo;
import cn.test.app.spider.yqgov.dao.IColumnInfoRepo;
import cn.test.app.spider.yqgov.dao.IItemInfoRepo;
import cn.test.app.spider.yqgov.entity.ArticleInfo;
import cn.test.app.spider.yqgov.entity.ColumnInfo;
import cn.test.app.spider.yqgov.entity.ItemInfo;
import cn.test.app.spider.yqgov.global.Globals;
import cn.test.app.spider.yqgov.global.MyException;
import cn.test.app.spider.yqgov.global.YQGovSpiderConfig;
import cn.test.app.spider.yqgov.service.IPushService;
import cn.test.app.spider.yqgov.service.bean.HttpResult;
import cn.test.app.spider.yqgov.service.bean.NewsBean;
import cn.test.app.spider.yqgov.service.bean.PushBean;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月27日 下午6:47:35
 */
@Service
public class PushServiceImpl implements IPushService{
	
	private static Logger logger = LoggerFactory.getLogger(PushServiceImpl.class);
	
	private static Gson gson = new Gson();
	
	@Resource
	private IItemInfoRepo itemRepo;
	
	@Resource
	private IArticleInfoRepo articleRepo;
	
	@Resource 
	private IColumnInfoRepo columnRepo;
	
	@Resource
	private HttpService httpService;
	
	@Resource
	private YQGovSpiderConfig config;
	
	public static Calendar cal = Calendar.getInstance();
	
	@Override
	public void push() throws MyException {
		List<ColumnInfo> columns = columnRepo.findAll();
		logger.info("by luzh-> size {}, {}", columns.size(), gson.toJson(columns));
		for (ColumnInfo info : columns) {
			try {
				logger.info("by luzh-> pushToHomed: start push {}", info.getKey());
				push(info.getKey(), info.getWebsite(), info.getLabel());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("by luzh-> pushError: {}.", e.getMessage(), e);
				break;
			}
			Globals.waitAMoment(300);
		}
	}
	
	@Override
	public void push(String url) {
		List<ItemInfo> items = itemRepo.findByUrl(url);
		if (items.isEmpty()) {
			logger.info("by luzh-> pushToHomed: url {} not exists.", url);
			return;
		}
		ColumnInfo columnInfo = columnRepo.findByKey(items.get(0).getParent());
		if (null == columnInfo) {
			return;
		}
		try {
			push(columnInfo.getKey(), items, columnInfo.getWebsite(), columnInfo.getLabel());
		} catch (Exception e) {
			logger.error("by luzh-> pushToHomed error: push url {} error, {}.", url, e.getMessage(), e);
		}
	}
	

	@Override
	public void push(String url, String parent, String website, String hlabel) {
		List<ItemInfo> items = itemRepo.findByUrl(url);
		if (items.isEmpty()) {
			logger.info("by luzh-> pushToHomed: url {} not exists.", url);
			return;
		}
		List<Integer> ids = new ArrayList<>();
		for (ItemInfo itemInfo : items) {
			ids.add(itemInfo.getId());
		}
		try {
			push(parent, items, website, hlabel);
		} catch (Exception e) {
			logger.error("by luzh-> pushToHomed error: push url {} error, {}.", url, e.getMessage(), e);
		}
		itemRepo.updatePushByIds(true, System.currentTimeMillis(), ids);
	}
	
	@Override
	public void push(String parent, String website, String hlabel) throws MyException, Exception {
		PageRequest pageable = new PageRequest(0, 20);
		List<ItemInfo> items = itemRepo.findByPushHomedAndFinishAndParentAndStatus(false, pageable, true, parent, 200);
		if (null == items || items.isEmpty()) {
			logger.info("by luzh-> pushToHomed: there has no articles added at column {}, stop push.", parent);
			return;
		}
		push(parent, items, website, hlabel);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void push(String parent, List<ItemInfo> items, String website, String hlabel) throws Exception {
		PushBean pushBean = new PushBean();
		pushBean.setAccesstoken(config.getAccesstoken());
		pushBean.setTotal(items.size()+"");
		List<NewsBean> newsList = new ArrayList<>();
		ArticleInfo article = new ArticleInfo();
		List<Integer> ids = new ArrayList<>();
		for (ItemInfo item : items) {
			NewsBean bean = new NewsBean();
			bean.setProvider_name(website);
			bean.setAuthor_name(website);
			article = articleRepo.findByUrl(item.getUrl()).isEmpty() ? null : articleRepo.findByUrl(item.getUrl()).get(0);
			if (null == article) {
				continue;
			}
			bean.setKey(Globals.IMPORT_KEY + article.getDate() + article.getId());
			bean.setTitle(article.getTitle());
			bean.setLink_url(article.getUrl());
			bean.setRelease_time(null == article.getDate() ? "" : (article.getDate() / 1000) + "");
			bean.setContent(article.getContent().replaceAll("\n", ""));
			bean.setContent_pic(null == article.getClipImages() ? new String[]{} : article.getClipImages().split(Globals.IMAGE_SPLIT)); 
			bean.setCopyrightdevice("");
			bean.setCopyrighttime(new Object[]{});
			bean.setSynopsis("");
			bean.setCategory(new String[]{});
			bean.setSubtype(new Integer[]{});
			bean.setThumbnail_pic(new Object());
			bean.setAudio(new Object());
			bean.setKeywords("");
			bean.setExtra_descr(new Object());
			newsList.add(bean);
			ids.add(item.getId());
			continue;
		}
		if (newsList.isEmpty()) {
			logger.info("by luzh-> pushToHomed: there has no articles added, stop push.");
			return;
		}
		pushBean.setList(newsList);
		pushBean.setTotal(newsList.size()+"");
		try {
			HttpResult result = httpService.doPostJson(config.getHomedAddress() + "/news/import", UriEncoder.encode(JSONObject.toJSONString(pushBean)).replaceAll("%20", " "));
			logger.info("req:{} \n result:{}",UriEncoder.encode(JSONObject.toJSONString(pushBean)).replaceAll("%20", " "), result.toString());
			Map<String, Object> oMap = JSONObject.parseObject(result.getBody());
			if (!Globals.isNull(oMap.get("ret")) && oMap.get("ret").toString().equals("0")) {
				homedAdjust((List<Object>)oMap.get("newsid_list"), hlabel, website);
				itemRepo.updatePushByIds(true, System.currentTimeMillis(), ids);
				logger.info("by luzh-> pushToHomed: column {} {} itmes push to homed success.", parent, ids.size());
			}
		} catch (Exception e) {
			logger.error("by luzh-> pushToHomed error {}.", e.getMessage(), e);
			return;
		}
	}
	
	@Override
	public void homedAdjust(List<Object> newsIds, String label, String website) throws Exception {
		Map<String, String> map = new HashMap<>();
		Map<String, Object> oMap = new HashMap<>();
		map.put("accesstoken", config.getAccesstoken());
		for (Object id : newsIds) {
			if ("0".equals(id.toString())) {
				continue;
			}
			map.put("newsid", id.toString());
			map.put("label", label);
			HttpResult result = new HttpResult();
			result.setBody("{\"ret\":1}");
			oMap = JSONObject.parseObject(result.getBody());
			while (!Globals.isNull(oMap.get("ret")) && !oMap.get("ret").toString().equals("0")) {
				try {
					result = httpService.doPostJson(config.getHomedAddress() + "/news/adjust_info", gson.toJson(map));
					logger.info("req:{}, \n result:{}", gson.toJson(map), result.toString());
					oMap = JSONObject.parseObject(result.getBody());
				} catch (Exception e) {
					logger.error("by luzh-> pushToHomed: adjust error {}.", e.getMessage(), e);
					return;
				}
			}
			String release = "{\"ret\":\"1\"}";
			String url = config.getHomedAddress() + "/news/release/release?accesstoken="+config.getAccesstoken()+"&newsid="+id.toString();
			oMap = JSONObject.parseObject(release);
			while (!Globals.isNull(oMap.get("ret")) && !oMap.get("ret").toString().equals("0")) {
				try {
					release = httpService.doGet(url);
					logger.info("req:{},result{}",url,release);
					oMap = JSONObject.parseObject(release);
				} catch (Exception e) {
					logger.error("by luzh-> pushToHomed: release error {}.", e.getMessage(), e);
					return;
				}
			}
		}
	}
}
