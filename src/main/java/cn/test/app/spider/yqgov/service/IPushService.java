package cn.test.app.spider.yqgov.service;

import java.util.List;

import cn.test.app.spider.yqgov.entity.ItemInfo;
import cn.test.app.spider.yqgov.global.MyException;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月27日 下午6:47:57
 */
public interface IPushService {

	void push() throws MyException;
	
	void push(String url);
	
	void push(String url, String parent, String website, String hlabel);

	void push(String parent, String website, String hlabel) throws MyException, Exception;

	void homedAdjust(List<Object> newsIds, String label, String website) throws MyException, Exception;

	void push(String parent, List<ItemInfo> items, String website, String hlabel) throws Exception;

}
