package cn.test.app.spider.yqgov.controller.req;

import java.util.List;

/**
 * @remark
 * @author luzh
 * @createTime 2017年8月17日 下午7:09:51
 */
public class ClipPicture {
	private String picId;
	private String clipPicUrl;
	private Integer resultCode;
	private String resultMsg;
	private List<ClipSize> clibSizes;

	public String getPicId() {
		return picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public String getClipPicUrl() {
		return clipPicUrl;
	}

	public void setClipPicUrl(String clipPicUrl) {
		this.clipPicUrl = clipPicUrl;
	}

	public Integer getResultCode() {
		return resultCode;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public List<ClipSize> getClibSizes() {
		return clibSizes;
	}

	public void setClibSizes(List<ClipSize> clibSizes) {
		this.clibSizes = clibSizes;
	}
}
