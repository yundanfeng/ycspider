package cn.test.app.spider.yqgov.service;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月27日 下午2:51:32
 */
public interface IArticleService {

	/**
	 * 添加文章信息
	 * @remark
	 * @author luzh
	 * @reateTime 2017年7月27日 下午3:11:20
	 * @param @param url
	 * @param @param title
	 * @param @param date
	 * @param @param content
	 * @param @param images
	 * @return: void    
	 */
	void addArticle(String url, String title, String date, String content, String images);

}
