package cn.test.app.spider.yqgov.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.test.app.spider.yqgov.controller.req.AddColumnReq;
import cn.test.app.spider.yqgov.controller.resp.BaseResp;
import cn.test.app.spider.yqgov.controller.resp.ColumnVO;
import cn.test.app.spider.yqgov.global.Globals;
import cn.test.app.spider.yqgov.global.MyException;
import cn.test.app.spider.yqgov.service.IColumnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月26日 上午11:40:18
 */
@Api("栏目")
@RestController
@RequestMapping("/column")
public class ColumnController {

	@Resource
	private IColumnService columnService;
	
	@RequestMapping(value = "/type", method = RequestMethod.GET)
	public Map<String, String> queryColumnType() {
		Map<String, String> map = new HashMap<>();
		map.put("type", "yqgov,yqdjgovw01,yqdtv,pp045000");
		return map;
	}
	
	@ApiOperation(value = "新增栏目")
	@RequestMapping(value = "/add", method = RequestMethod.PUT)
	public BaseResp addColumn(@ModelAttribute AddColumnReq req) throws MyException{
		columnService.addColumn(req.getName(), req.getUrl(), req.getType(), req.getKey(), req.getHlabel(), req.getWebsite());
		return new BaseResp(Globals.CODE_1, Globals.MSG_1);
	}
	
	@ApiOperation(value = "获取所有栏目信息")
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<ColumnVO> getAllColumns() {
		return columnService.getAllColumns();
	}
}
