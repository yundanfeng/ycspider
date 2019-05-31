package cn.test.app.spider.yqgov.service.bean;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月27日 下午6:59:44
 */
public class NewsBean {
	private String key;
	private String release_time;
	private String provider_name;
	private String author_name;
	private String title;
	private String copyrightdevice;
	private Object copyrighttime;
	private String synopsis;
	private String[] category;
	private Integer[] subtype;
	private String link_url;
	private Object thumbnail_pic;
	private String[] content_pic;
	private String content;
	private Object audio;
	private String keywords;
	private Object extra_descr;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRelease_time() {
		return release_time;
	}

	public void setRelease_time(String release_time) {
		this.release_time = release_time;
	}

	public String getProvider_name() {
		return provider_name;
	}

	public void setProvider_name(String provider_name) {
		this.provider_name = provider_name;
	}

	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCopyrightdevice() {
		return copyrightdevice;
	}

	public void setCopyrightdevice(String copyrightdevice) {
		this.copyrightdevice = copyrightdevice;
	}

	public Object getCopyrighttime() {
		return copyrighttime;
	}

	public void setCopyrighttime(Object copyrighttime) {
		this.copyrighttime = copyrighttime;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String[] getCategory() {
		return category;
	}

	public void setCategory(String[] category) {
		this.category = category;
	}

	public Integer[] getSubtype() {
		return subtype;
	}

	public void setSubtype(Integer[] subtype) {
		this.subtype = subtype;
	}

	public String getLink_url() {
		return link_url;
	}

	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}

	public String[] getContent_pic() {
		return content_pic;
	}

	public void setContent_pic(String[] content_pic) {
		this.content_pic = content_pic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Object getThumbnail_pic() {
		return thumbnail_pic;
	}

	public void setThumbnail_pic(Object thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}

	public Object getAudio() {
		return audio;
	}

	public void setAudio(Object audio) {
		this.audio = audio;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Object getExtra_descr() {
		return extra_descr;
	}

	public void setExtra_descr(Object extra_descr) {
		this.extra_descr = extra_descr;
	}
}
