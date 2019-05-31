package cn.test.app.spider.yqgov.service;

import cn.test.app.spider.yqgov.controller.req.ClipCallBackReq;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年8月17日 下午5:10:47
 */
public interface IIMageService {
	
	void downloadImage(String url) throws Exception;

	void clip(String url);
	
	void callBack(ClipCallBackReq req);
}
