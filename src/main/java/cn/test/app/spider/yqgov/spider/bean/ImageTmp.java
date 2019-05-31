package cn.test.app.spider.yqgov.spider.bean;

/**
 * @remark
 * @author luzh
 * @createTime 2017年8月18日 上午11:38:24
 */
public class ImageTmp {
	private Boolean hasHost;
	private String old;
	private String right;

	public Boolean getHasHost() {
		return hasHost;
	}

	public void setHasHost(Boolean hasHost) {
		this.hasHost = hasHost;
	}

	public String getOld() {
		return old;
	}

	public void setOld(String old) {
		this.old = old;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}
	
	public ImageTmp() {
	}

	@Override
	public String toString() {
		return "ImageTmp [hasHost=" + hasHost + ", old=" + old + ", right=" + right + "]";
	}
}
