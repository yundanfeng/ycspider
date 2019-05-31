package cn.test.app.spider.yqgov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * 栏目列表中的单个项目实体
 * @remark
 * @author luzh 
 * @createTime 2017年4月28日 下午5:33:57
 */
@Entity
@Table(name = "itemInfo")
public class ItemInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "title")
	private String title;  // 文章标题
	
	@Column(name = "url")
	private String url;  // 文章地址
	
	@Column(name = "type")
	private String type;  // 所属网址类型
	
	@Column(name = "finish")
	private Boolean finish;  // 爬取是否完成
	
	@Column(name = "status") 
	private Integer status;  // 地址是否存在
	
	@Column(name = "parent")  
	private String parent;  // 所在栏目信息
	
	@Column(name = "spiderDate")
	private Long spiderDate; // 爬取该文章的时间
	
	@Column(name = "pushHomed") 
	private Boolean pushHomed;  // 是否推送homed
	
	@Column(name = "pushHomedDate") 
	private Long pushHomedDate;  // 是否推送homed

	public Boolean getPushHomed() {
		return pushHomed;
	}

	public void setPushHomed(Boolean pushHomed) {
		this.pushHomed = pushHomed;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSpiderDate() {
		return spiderDate;
	}

	public void setSpiderDate(Long spiderDate) {
		this.spiderDate = spiderDate;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getFinish() {
		return finish;
	}

	public void setFinish(Boolean finish) {
		this.finish = finish;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Long getPushHomedDate() {
		return pushHomedDate;
	}

	public void setPushHomedDate(Long pushHomedDate) {
		this.pushHomedDate = pushHomedDate;
	}

	@Override
	public String toString() {
		return "ListItemInfo [id=" + id + ", title=" + title + ", url=" + url + ", finish=" + finish + ", status="
				+ status + ", parent=" + parent + ", spiderDate=" + spiderDate + "]";
	}

	public ItemInfo() {
		super();
	}

	public ItemInfo(String title, String url, boolean finish, String parent, Integer status, String type, boolean pushHomed) {
		super();
		this.title = title;
		this.url = url;
		this.finish = finish;
		this.parent = parent;
		this.status = status;
		this.type = type;
		this.pushHomed = pushHomed;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		ItemInfo info = (ItemInfo) obj;
		if (url == null) {
			if (info.url != null) {
				return false;
			}
		} else if (!url.equals(info.url)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int primer = 31; 
		int result = 1; 
		result = primer * result + url == null ? 0 : url.hashCode();
		return result;
	}
}
