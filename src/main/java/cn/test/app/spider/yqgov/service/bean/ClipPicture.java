package cn.test.app.spider.yqgov.service.bean;

import java.util.List;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年8月17日 下午5:13:41
 */
public class ClipPicture {
	private String url;
	private String picId;
	private Boolean isUploadSrcPic;
	private Boolean isNeedSuccNotify;
	private String picSaveName;
	private String callbackUrl;
	private String callbackKey;
	private List<PictureSize> clibSizes;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPicId() {
		return picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public Boolean getIsUploadSrcPic() {
		return isUploadSrcPic;
	}

	public void setIsUploadSrcPic(Boolean isUploadSrcPic) {
		this.isUploadSrcPic = isUploadSrcPic;
	}

	public Boolean getIsNeedSuccNotify() {
		return isNeedSuccNotify;
	}

	public void setIsNeedSuccNotify(Boolean isNeedSuccNotify) {
		this.isNeedSuccNotify = isNeedSuccNotify;
	}

	public String getPicSaveName() {
		return picSaveName;
	}

	public void setPicSaveName(String picSaveName) {
		this.picSaveName = picSaveName;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getCallbackKey() {
		return callbackKey;
	}

	public void setCallbackKey(String callbackKey) {
		this.callbackKey = callbackKey;
	}

	public List<PictureSize> getClibSizes() {
		return clibSizes;
	}

	public void setClibSizes(List<PictureSize> clibSizes) {
		this.clibSizes = clibSizes;
	}

	public ClipPicture(String url, String picId, Boolean isUploadSrcPic, Boolean isNeedSuccNotify, String picSaveName,
			String callbackUrl, String callbackKey, List<PictureSize> clibSizes) {
		super();
		this.url = url;
		this.picId = picId;
		this.isUploadSrcPic = isUploadSrcPic;
		this.isNeedSuccNotify = isNeedSuccNotify;
		this.picSaveName = picSaveName;
		this.callbackUrl = callbackUrl;
		this.callbackKey = callbackKey;
		this.clibSizes = clibSizes;
	}
}
