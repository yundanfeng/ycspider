package cn.test.app.spider.yqgov.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.test.app.spider.yqgov.service.IItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年8月7日 下午2:37:18
 */
@Api("文章条目")
@RequestMapping("/item")
@RestController
public class ItemController {

	@Resource
	private IItemService service;
	
	@ApiOperation(value = "查询栏目新闻列表")
	@ApiImplicitParam(name = "key", value = "栏目标识", dataType = "String", paramType = "query", required = true)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Map<String, Object> getItems(@PathParam(value = "key")String key) {
		return service.getItems(key);
	}
}
