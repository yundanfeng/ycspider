package cn.test.app.spider.yqgov.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import cn.test.app.spider.yqgov.entity.ColumnInfo;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月26日 上午11:21:25
 */
public interface IColumnInfoRepo extends Repository<ColumnInfo, Integer>{
	
	/**
	 * 添加信息
	 * @remark
	 * @author luzh
	 * @reateTime 2017年7月26日 上午11:23:15
	 * @param @param info
	 * @return: void    
	 */
	void save(ColumnInfo info);
	
	ColumnInfo findByKey(String ckey);
	
	/**
	 * 查询所有
	 * @remark
	 * @author luzh
	 * @reateTime 2017年7月26日 下午3:11:14
	 * @param @return
	 * @return: List<ColumnInfo>    
	 */
	List<ColumnInfo> findAll();
	
	@Transactional
	@Modifying
	@Query(value = "update ColumnInfo set others = :others where ckey = :ckey")
	void updateOthersByCkey(@Param("others")String others, @Param("ckey")String ckey);
}
