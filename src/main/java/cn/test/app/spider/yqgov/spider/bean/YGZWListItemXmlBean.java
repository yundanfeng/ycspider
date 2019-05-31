package cn.test.app.spider.yqgov.spider.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * @remark
 * @author luzh
 * @createTime 2017年7月24日 下午4:31:05
 */
@XStreamAlias("table")
public class YGZWListItemXmlBean {
	@XStreamAlias("tr")
	private TableTr tr;

	@Override
	public String toString() {
		return "YGZWListItemXmlBean [tr=" + tr + "]";
	}

	public TableTr getTr() {
		return tr;
	}

	public void setTr(TableTr tr) {
		this.tr = tr;
	}

	public static class TableTr {
		@XStreamImplicit(itemFieldName = "td")
		public List<TableTd> td;

		public List<TableTd> getTd() {
			return td;
		}

		public void setTd(List<TableTd> td) {
			this.td = td;
		}
	}

	public static class TableTd {
		public HtmlA a;
		public HtmlSpan span;

		public HtmlA getA() {
			return a;
		}

		public void setA(HtmlA a) {
			this.a = a;
		}

		public HtmlSpan getSpan() {
			return span;
		}

		public void setSpan(HtmlSpan span) {
			this.span = span;
		}

	}

	@XStreamAlias("a")
	@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "content" })
	public static class HtmlA {
		public String href;
		public String title;
		public String content;

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
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
	}

	@XStreamAlias("span")
	@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "content" })
	public static class HtmlSpan {
		public String content;
	}
}
