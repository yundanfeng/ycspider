package cn.test.app.spider.yqgov.service.bean;

import java.util.List;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月27日 下午6:55:14
 */
public class PushBean {
	private String accesstoken;
	private String total;
	private List<NewsBean> list;

	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<NewsBean> getList() {
		return list;
	}

	public void setList(List<NewsBean> list) {
		this.list = list;
	}
}
