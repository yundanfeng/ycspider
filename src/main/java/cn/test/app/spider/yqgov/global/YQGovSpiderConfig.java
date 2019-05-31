package cn.test.app.spider.yqgov.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月5日 下午4:51:11
 */
@Component
@PropertySource("classpath:yqconfig.properties")
public class YQGovSpiderConfig {
	// homed接口地址
	@Value("${homedAddress}")
	private String homedAddress;

	@Value("${accesstoken}")
	private String accesstoken;

	@Value("${clipPicAddress}")
	private String clipPicAddress;
	
	@Value("${clipPicUserName}")
	private String clipPicUserName;
	
	@Value("${clipPicAccesskey}")
	private String clipPicAccesskey;

	@Value("${urlPath}")
	private String urlPath;

	@Value("${imageFolderPath}")
	private String imageFolderPath;

	@Value("${imageUrl}")
	private String imageUrl;

	public String getClipPicAccesskey() {
		return clipPicAccesskey;
	}

	public void setClipPicAccesskey(String clipPicAccesskey) {
		this.clipPicAccesskey = clipPicAccesskey;
	}

	public String getClipPicUserName() {
		return clipPicUserName;
	}

	public void setClipPicUserName(String clipPicUserName) {
		this.clipPicUserName = clipPicUserName;
	}

	public String getImageFolderPath() {
		return imageFolderPath;
	}

	public void setImageFolderPath(String imageFolderPath) {
		this.imageFolderPath = imageFolderPath;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getClipPicAddress() {
		return clipPicAddress;
	}

	public void setClipPicAddress(String clipPicAddress) {
		this.clipPicAddress = clipPicAddress;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public String getHomedAddress() {
		return homedAddress;
	}

	public void setHomedAddress(String homedAddress) {
		this.homedAddress = homedAddress;
	}

	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
}
