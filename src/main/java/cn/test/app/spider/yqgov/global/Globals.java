package cn.test.app.spider.yqgov.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.test.app.spider.yqgov.spider.bean.ImageTmp;

/** 
 * @remark
 * @author  yezm 
 * @createTime 2016年1月13日 18:26:11
 */
public class Globals {
	
	public static final String CODE_1 = "1"; //成功code
	public static final String MSG_1 = "成功"; //成功msg
	
	public static final String CODE_0 = "0";
	public static final String MSG_0 = "失败";
	
	public static final String CODE_NULL = "-1"; 
	public static final String MSG_NULL = "无记录";
	
	public static final String CODE_101 = "101"; //缺少必填参数
	public static final String MSG_101 = "缺少必填参数"; //缺少必填参数
	
	public static final String CODE_102 = "102"; //参数不合法
	public static final String MSG_102 = "参数不合法"; //参数不合法
	
	public static final String CODE_500 = "500"; //失败code
	public static final String MSG_500 = "未知错误"; //失败msg
	
	public static final String TYPE1 = "yqgov";
	public static final String TYPE2 = "yqdjgovw01";
	public static final String TYPE3 = "yqdtv";
	public static final String TYPE4 = "pp045000";
	
	public static final String TIMEREG = "\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}";
	
	public static final String FULLTIMEREG = "\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2} (([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9]):([0-5]?[0-9])?";
	
	public static final String FULLTIMEREG_HM = "\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2} (([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])";
	
	public static final String USERAGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";
	
	public static final String IMAGE_SPLIT = "##QAQ##";
	
	public static final String IMPORT_KEY = "homed-yqgov-";
	
	public static final String PROJECTNAME = "YQGovSpider";
	
	
	/**
	 * 判断参数是否为空， 空返回true ，不为空返回false
	 * @remark
	 * @author yezm
	 * @createTIme 2016-6-15 上午11:42:42
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj){
		if(obj == null) return true;
		if("".equals(obj)) return true;
		return false;
	}
	
	/**
	 * 通用的组建map的方法
	 * @remark
	 * @author luzh
	 * @reateTime 2017年6月16日 上午11:25:32
	 * @param @param names
	 * @param @param values
	 * @param @return
	 * @return: Map<String,Object>    
	 */
	public static Map<String, Object> createReqMap(String[] names, Object[] values) {
		if (names.length == 0 || values.length == 0 || names.length != values.length) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int len = names.length;
		for (int i = 0; i < len; i++) {
			map.put(names[i], values[i]);
		}
		return map;
	}
	
	/**
	 * List<String> 进行排序
	 * @remark
	 * @author luzh
	 * @reateTime 2017年6月26日 上午10:16:21
	 * @param @param list
	 * @param @return
	 * @return: List<String>    
	 */
	public static List<String> listStringSort(List<String > list) {
		Collections.sort(list, new Comparator<String>() {

			@Override
			public int compare(String str1, String str2) {
				return str1.compareTo(str2);
			}
			
		});
		return list;
	}
	
	/**
	 * 字符串中提取符合正则表达式的字符串
	 * @remark
	 * @author luzh
	 * @reateTime 2017年4月28日 上午11:01:03
	 * @param @param content
	 * @param @param regex
	 * @param @return
	 * @return: List<String>    
	 */
	public static List<String> regexString(String content, String regex) {
		List<String> list = new ArrayList<>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}
	
	/**
	 * 替换文本
	 * @remark
	 * @author luzh
	 * @reateTime 2017年4月28日 上午11:15:06
	 * @param @param text
	 * @param @param replaced
	 * @param @param toBe
	 * @param @return
	 * @return: String    
	 */
	public static String replaceText(String text, String replaced, String toBe) {
		String resultT = "";
		Pattern p = Pattern.compile(replaced);
		// 用Pattern类的matcher()方法生成一个Matcher对象
		Matcher m = p.matcher(text);
		StringBuffer sb = new StringBuffer();
		// 使用find()方法查找第一个匹配的对象
		boolean result = m.find();
		// 使用循环将句子里所有的kelvin找出并替换再将内容加到sb里
		while (result) {
			m.appendReplacement(sb, toBe);
			// 继续查找下一个匹配对象
			result = m.find();
		}
		// 最后调用appendTail()方法将最后一次匹配后的剩余字符串加到sb里；
		m.appendTail(sb);
		resultT = sb.toString();
		return resultT;
	}
	
	/**
	 * 从内容中提取图片地址，地址之间用,分隔
	 * @remark
	 * @author luzh
	 * @reateTime 2017年5月9日 下午5:50:18
	 * @param @param text
	 * @param @return
	 * @return: String    
	 */
	public static Map<String, Object> manageContentImageAndExtractImageUrl(String html, String host, String attr, String needHost, String notNeedHost, String needRemove, String split){
		Map<String, Object> result = new HashMap<>();
		Document document = Jsoup.parse(html);
		Elements elements = document.select("img");
		List<ImageTmp> imgs = new ArrayList<>();
		if (elements.size() > 0) {
			for (Element e : elements) {
				if (!isNull(e.attr(attr)) && !isNull(needHost) && !isNull(host)) {
					imgs = getImagesUrlUtil(imgs, true, needHost, split, e.attr(attr), host);
				}
				if (!isNull(e.attr(attr)) && !isNull(notNeedHost)) {
					imgs = getImagesUrlUtil(imgs, false, notNeedHost, split, e.attr(attr), host);
				}
				if (!isNull(e.attr(attr)) && !isNull(needRemove)) {
					for (String s : needRemove.split(split)) {
						if (e.attr(attr).startsWith(s)) {
							e.remove();
						}
					}
				}
			}
			//return imgs;
		}
		result.put("content", document);
		result.put("images", imgs);
		return result;
	}
	
	// 这个方法参数名字比较乱，凑合看吧
	private static List<ImageTmp> getImagesUrlUtil(List<ImageTmp> list, boolean needHost, String str, String split, String attr, String host) {
		for (String t : str.split(split)) {
			if (attr.startsWith(t)) {
				ImageTmp tmp = new ImageTmp();
				tmp.setOld(attr);
				tmp.setRight(needHost ? host+attr : attr);
				tmp.setHasHost(needHost);
				list.add(tmp);
			}
		}
		return list;
	}
	
	public static void waitAMoment(int waitTime) {
		if (waitTime > 0) {
			try {
				Thread.sleep(waitTime * 1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
