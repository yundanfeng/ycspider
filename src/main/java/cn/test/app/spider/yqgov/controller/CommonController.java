package cn.test.app.spider.yqgov.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.test.app.spider.yqgov.HttpService;
import cn.test.app.spider.yqgov.controller.resp.BaseResp;
import cn.test.app.spider.yqgov.global.Globals;
import cn.test.app.spider.yqgov.global.MyException;
import cn.test.app.spider.yqgov.global.YQGovSpiderConfig;
import cn.test.app.spider.yqgov.service.IColumnService;
import cn.test.app.spider.yqgov.service.IItemService;
import cn.test.app.spider.yqgov.service.IPushService;
import io.swagger.annotations.Api;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月27日 下午2:47:25
 */
@Api("Common")
@RestController
@RequestMapping("/common")
public class CommonController {

	@Resource
	private IColumnService columnService;
	
	@Resource
	private IItemService itemService;
	
	@Resource
	private IPushService pushService;
	
	@Resource 
	private HttpService httpService;
	
	@Resource
	private YQGovSpiderConfig config;

	@RequestMapping(value = "/startCrawlColumnItems", method = RequestMethod.GET)
	public BaseResp startCrawlColumn() throws MyException {
		columnService.getColumnItems();
		return new BaseResp(Globals.CODE_1, Globals.MSG_1);
	}
	
	@RequestMapping(value = "/startCrawlArticle", method = RequestMethod.GET)
	public BaseResp startCrawlArticle() throws MyException {
		//itemService.startCrawl();
		return new BaseResp(Globals.CODE_1, Globals.MSG_1);
	}
	
	@RequestMapping(value = "/startPush", method = RequestMethod.GET)
	public BaseResp startPush() throws MyException {
		//pushService.push();
		return new BaseResp(Globals.CODE_1, Globals.MSG_1);
	}
	
	@RequestMapping(value = "/startPushUrl", method = RequestMethod.GET)
	public BaseResp startPush(@RequestParam(name = "url") String url) throws MyException {
		//pushService.push(url);
		return new BaseResp(Globals.CODE_1, Globals.MSG_1);
	}
	
	@RequestMapping(value = "/startPushUrl1", method = RequestMethod.GET)
	public BaseResp startPush(@RequestParam(name = "url") String url, @RequestParam(name = "parent") String parent, @RequestParam(name = "website") String website, @RequestParam(name = "label") String label) throws MyException {
		//pushService.push(url, parent, website, label);
		return new BaseResp(Globals.CODE_1, Globals.MSG_1);
	}
	
	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public BaseResp getConfig() {
		return new BaseResp(Globals.CODE_0, "" + config.getHomedAddress() + " | " + config.getAccesstoken());
	}
}
