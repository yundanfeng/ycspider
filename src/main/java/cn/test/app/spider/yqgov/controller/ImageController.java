package cn.test.app.spider.yqgov.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import cn.test.app.spider.yqgov.controller.req.ClipCallBackReq;
import cn.test.app.spider.yqgov.controller.resp.BaseResp;
import cn.test.app.spider.yqgov.global.Globals;
import cn.test.app.spider.yqgov.service.IIMageService;
import cn.test.app.spider.yqgov.task.AsyncTask;
import io.swagger.annotations.Api;

/**
 * @remark
 * @author luzh
 * @createTime 2017年8月17日 下午6:37:57
 */
@Api("图片")
@RestController
@RequestMapping(value = "/image")
public class ImageController {

	private static Logger logger = LoggerFactory.getLogger(ImageController.class);

	private Gson gson = new Gson();

	@Resource
	private IIMageService service;
	
	@Resource
	private AsyncTask task;

	@RequestMapping(value = "/clip", method = RequestMethod.GET)
	public BaseResp clip(@RequestParam(name = "url") String url) {
		service.clip(url);
		return new BaseResp(Globals.CODE_0, Globals.MSG_0);
	}

	@RequestMapping(value = "/clipCallBack", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public synchronized @ResponseBody ResponseEntity<String> callback(HttpServletRequest servletRequest){
		Map<String, String[]> map = servletRequest.getParameterMap();
		String data = map.keySet().iterator().next();
		logger.info("by luzh-> clipCallBack data: {}.", data);
		try {
			ClipCallBackReq clipReq = gson.fromJson(data, ClipCallBackReq.class);
			//task.clipCalBAck(clipReq);
			service.callBack(clipReq);
		} catch (Exception e) {
			logger.error("by luzh-> clipCallBack data: {}.", data);
		} 
		return  ResponseEntity.accepted().body("{\"retCode\": 0,\"retMsg\": \"success\"}");
	}
}
