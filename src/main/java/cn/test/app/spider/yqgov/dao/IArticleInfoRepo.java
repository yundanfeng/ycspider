package cn.test.app.spider.yqgov.dao;

import java.util.List;

import org.springframework.data.repository.Repository;

import cn.test.app.spider.yqgov.entity.ArticleInfo;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月27日 下午2:59:11
 */
public interface IArticleInfoRepo extends Repository<ArticleInfo, Integer>{
	
	void save(ArticleInfo info);
	
	List<ArticleInfo> findByIdIn(List<Integer> ids);

	List<ArticleInfo> findByUrl(String url);
	
	ArticleInfo findById(Integer id);
}
