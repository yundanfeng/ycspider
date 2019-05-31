package cn.test.app.spider.yqgov.controller.req;

/**
 * @remark
 * @author luzh
 * @createTime 2017年8月17日 下午7:12:49
 */
public class ClipSize {
	private String size;
	private String saveName;
	private String clipPicUrl;
	private Integer resultCode;
	private String resultMsg;

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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSaveName() {
		return saveName;
	}

	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}

	public String getClipPicUrl() {
		return clipPicUrl;
	}

	public void setClipPicUrl(String clipPicUrl) {
		this.clipPicUrl = clipPicUrl;
	}
}
