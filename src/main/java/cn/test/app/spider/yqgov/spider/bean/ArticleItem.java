package cn.test.app.spider.yqgov.spider.bean;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月24日 下午6:20:54
 */
public class ArticleItem {
	private String title;
	private String href;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public ArticleItem(String title, String href) {
		super();
		this.title = title;
		this.href = href;
	}

	@Override
	public String toString() {
		return "[title=" + title + ", href=" + href + "]";
	}
}
