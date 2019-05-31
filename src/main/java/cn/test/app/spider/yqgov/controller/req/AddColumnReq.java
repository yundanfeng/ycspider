package cn.test.app.spider.yqgov.controller.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @remark
 * @author luzh
 * @createTime 2017年8月17日 下午6:57:59
 */
@ApiModel
public class AddColumnReq {

	@ApiModelProperty(value = "名称", required = true)
	private String name;
	@ApiModelProperty(value = "地址", required = true)
	private String url;
	@ApiModelProperty(value = "类型", required = true)
	private String type;
	@ApiModelProperty(value = "栏目其他标识信息", required = true)
	private String key;
	@ApiModelProperty(value = "homed新闻系统该栏目label", required = true)
	private String hlabel;
	@ApiModelProperty(value = "网站名称", required = true)
	private String website;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getHlabel() {
		return hlabel;
	}

	public void setHlabel(String hlabel) {
		this.hlabel = hlabel;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
}
