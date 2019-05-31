package cn.test.app.spider.yqgov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月25日 上午10:53:52
 */
@Entity
@Table(name = "columnInfo")
public class ColumnInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "name")  // 栏目的名称
	private String name;
	@Column(name = "url")  // 栏目地址
	private String url;
	@Column(name = "type")  // 栏目类型
	private String type;
	@Column(name = "ckey")  // 栏目的其他标识信息
	private String key;
	@Column(name = "others") // 每个栏目实际爬取的情况不一样，用gson字段存取可能需要的其他信息
	private String others;
	@Column(name = "hLabel")
	private String label;   // homed栏目label值
	@Column(name = "website")
	private String website; // 网站名称

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ColumnInfo(Integer id, String name, String url, String type, String key) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.type = type;
		this.key = key;
	}

	public ColumnInfo() {
		//
	}
}
