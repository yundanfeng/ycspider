package cn.test.app.spider.yqgov.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @remark
 * @author luzh
 * @createTime 2017年5月2日 下午2:09:36
 */
@Entity
@Table(name = "articleInfo")
public class ArticleInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "title")
	private String title;

	@Column(name = "url")
	private String url;

	@Column(name = "date")
	private Long date;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "longText", name = "images")
	private String images;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "longText", name = "content")
	private String content;

	@Column
	private Integer imageCount;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "longText", name = "clipImages")
	private String clipImages;

	public String getClipImages() {
		return clipImages;
	}

	public void setClipImages(String clipImages) {
		this.clipImages = clipImages;
	}

	public Integer getImageCount() {
		return imageCount;
	}

	public void setImageCount(Integer imageCount) {
		this.imageCount = imageCount;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArticleInfo(String title, String url, Long date, String content, String images) {
		super();
		this.url = url;
		this.title = title;
		this.date = date;
		this.content = content;
		this.images = images;
	}

	public ArticleInfo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ArticleInfo [id=" + id + ", title=" + title + ", url=" + url + ", date=" + date + ", images=" + images
				+ ", content=" + content.substring(0, 10) + "]";
	}
}
