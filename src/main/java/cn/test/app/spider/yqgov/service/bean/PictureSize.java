package cn.test.app.spider.yqgov.service.bean;

/**
 * @remark
 * @author luzh
 * @createTime 2017年8月17日 下午5:15:20
 */
public class PictureSize {
	private String size;
	private String saveName;

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

	public PictureSize(String size, String saveName) {
		super();
		this.size = size;
		this.saveName = saveName;
	}
}
