package cn.test.app.spider.yqgov.service.bean;

import java.util.List;

/**
 * @remark
 * @author luzh
 * @createTime 2017年8月17日 下午5:12:51
 */
public class ClipReq {
	private String accesskey;
	private String usrName;
	private List<ClipPicture> pictures;

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

	public List<ClipPicture> getPictures() {
		return pictures;
	}

	public void setPictures(List<ClipPicture> pictures) {
		this.pictures = pictures;
	}
}
