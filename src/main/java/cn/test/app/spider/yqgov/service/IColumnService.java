package cn.test.app.spider.yqgov.service;

import java.util.List;

import cn.test.app.spider.yqgov.controller.resp.ColumnVO;
import cn.test.app.spider.yqgov.global.MyException;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月26日 上午11:23:56
 */
public interface IColumnService {
	
	/**
	 * 添加基础栏目
	 * @remark
	 * @author luzh
	 * @reateTime 2017年7月26日 下午3:09:16
	 * @param @param name
	 * @param @param url
	 * @param @param type
	 * @param @param key
	 * @param @return
	 * @param @throws MyException
	 * @return: void    
	 */
	void addColumn(String name, String url, String type, String key, String hlabel, String website) throws MyException;

	/**
	 * 开始获取栏目下的内容列表
	 * @remark
	 * @author luzh
	 * @reateTime 2017年7月26日 下午3:09:57
	 * @param @throws MyException
	 * @return: void    
	 */
	void getColumnItems() throws MyException;

	/**
	 * 开始类型1列表页爬取
	 * @remark
	 * @author luzh
	 * @reateTime 2017年7月26日 下午3:30:19
	 * @param @param key
	 * @param @throws MyException
	 * @return: void    
	 */
	void getType1ColumnItems(String key, String url) throws MyException;

	void getType2ColumnItems(String key, String url) throws MyException;

	void getType3ColumnItems(String key, String url) throws MyException;
	
	// type3和type4 逻辑相同，但仍然分开写
	void getType4ColumnItems(String key, String url) throws MyException;

	List<ColumnVO> getAllColumns();

}
