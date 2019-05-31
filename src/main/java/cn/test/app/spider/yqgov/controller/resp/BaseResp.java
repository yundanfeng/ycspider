package cn.test.app.spider.yqgov.controller.resp;

import io.swagger.annotations.ApiModelProperty;

/**
 * @remark
 * @author luzh
 * @createTime 2017年6月16日 下午5:27:48
 */
public class BaseResp {
	@ApiModelProperty(value = "返回结果值，1成功，0失败，-1无数据")
	private String code;
	@ApiModelProperty(value = "返回结果说明")
	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public BaseResp(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public BaseResp() {
		super();
	}
}
