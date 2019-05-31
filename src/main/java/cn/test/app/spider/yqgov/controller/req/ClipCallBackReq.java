package cn.test.app.spider.yqgov.controller.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @remark
 * @author luzh
 * @createTime 2017年8月17日 下午7:08:06
 */
@ApiModel
public class ClipCallBackReq {
	@ApiModelProperty(value = "accesskey")
	private String accesskey;
	@ApiModelProperty(value = "usrName")
	private String usrName;
	@ApiModelProperty(value = "picture")
	private ClipPicture picture;

	public String getAccesskey() {
		return accesskey;
	}

	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}

	public ClipPicture getPicture() {
		return picture;
	}

	public void setPicture(ClipPicture picture) {
		this.picture = picture;
	}

	@Override
	public String toString() {
		return "ClipCallBackReq [accesskey=" + accesskey + ", usrName=" + usrName + ", picture=" + picture + "]";
	}
}
