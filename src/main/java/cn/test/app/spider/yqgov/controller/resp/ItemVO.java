package cn.test.app.spider.yqgov.controller.resp;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.test.app.spider.yqgov.entity.ItemInfo;
import cn.test.app.spider.yqgov.global.Globals;

/**
 * @remark
 * @author luzh
 * @createTime 2017年8月7日 下午2:04:16
 */
public class ItemVO {

	private String title; // 文章标题
	private String url; // 文章地址
	private String finish; // 爬取是否完成
	private String status; // 地址是否存在
	private String spiderDate; // 爬取该文章的时间
	private String pushHomed; // 是否推送homed
	private String pushHomedDate; // 是否推送homed

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSpiderDate() {
		return spiderDate;
	}

	public void setSpiderDate(String spiderDate) {
		this.spiderDate = spiderDate;
	}

	public String getPushHomed() {
		return pushHomed;
	}

	public void setPushHomed(String pushHomed) {
		this.pushHomed = pushHomed;
	}

	public String getPushHomedDate() {
		return pushHomedDate;
	}

	public void setPushHomedDate(String pushHomedDate) {
		this.pushHomedDate = pushHomedDate;
	}

	public ItemVO(ItemInfo info) {
		super();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.title = info.getTitle();
		this.url = info.getUrl();
		this.finish = info.getFinish() ? "已爬取" : "未爬取";
		this.status = Globals.isNull(info.getStatus()) ? null : info.getStatus().toString();
		this.spiderDate = Globals.isNull(info.getSpiderDate()) ? "" : sdf.format(new Date(info.getSpiderDate()));
		this.pushHomed = info.getPushHomed()? "已推送" : "未推送";;
		this.pushHomedDate = Globals.isNull(info.getPushHomedDate()) ? "" : sdf.format(new Date(info.getPushHomedDate()));
	}
}
