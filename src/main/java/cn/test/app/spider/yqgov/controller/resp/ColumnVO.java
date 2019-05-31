package cn.test.app.spider.yqgov.controller.resp;

/**
 * @remark
 * @author luzh
 * @createTime 2017年8月16日 下午5:29:14
 */
public class ColumnVO {
	private String name;
	private String key;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ColumnVO(String name, String key) {
		this.name = name;
		this.key = key;
	}
}
