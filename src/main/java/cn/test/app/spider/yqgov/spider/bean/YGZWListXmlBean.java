package cn.test.app.spider.yqgov.spider.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月24日 上午11:16:18
 */
@XStreamAlias("datastore")
public class YGZWListXmlBean {
	private String totalrecord;
	private String totalpage;
	@XStreamAlias("recordset")  
	private RecordSet recordset;
	public String getTotalrecord() {
		return totalrecord;
	}
	public void setTotalrecord(String totalrecord) {
		this.totalrecord = totalrecord;
	}
	public String getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(String totalpage) {
		this.totalpage = totalpage;
	}
		
	public RecordSet getRecordset() {
		return recordset;
	}
	public void setRecordset(RecordSet recordset) {
		this.recordset = recordset;
	}

	public static class RecordSet {
		@XStreamImplicit(itemFieldName = "record")  
		private List<Record> record;

		public List<Record> getRecord() {
			return record;
		}

		public void setRecord(List<Record> record) {
			this.record = record;
		}  
	}
	
	@XStreamAlias("record")  
	@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "content" })  
	public static class Record {
		 private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}  
	}
}