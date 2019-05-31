package cn.test.app.spider.yqgov.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import cn.test.app.spider.yqgov.entity.ItemInfo;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月26日 下午3:47:02
 */
public interface IItemInfoRepo extends Repository<ItemInfo, Integer>{
	
	void save(ItemInfo info);

	@Query("select count(*) from ItemInfo where parent = :parent")
	int countByParent(@Param("parent")String parent);
	
	List<ItemInfo> findByUrl(String url);
	
	List<ItemInfo> findByFinishAndParent(Boolean finish, String parent, Pageable pageable);
	
	List<ItemInfo> findByPushHomedAndFinishAndParentAndStatus(Boolean push, Pageable pageable, Boolean finish, String parent, Integer status);
	
	List<ItemInfo> findByParent(String parent);
	
	@Transactional
	@Modifying
	@Query(value = "update ItemInfo set status = :status, finish = :finish, spiderDate = :spiderDate where id = :id or url = :url")
	void updateItemInfosByIdOrUrl(@Param("status")Integer status, @Param("finish")Boolean finish, @Param("spiderDate") Long spiderDate, @Param("id")Integer id, @Param("url")String url);
	
	@Transactional
	@Modifying
	@Query(value = "update ItemInfo set finish = :finish where url = :url")
	void updateFinishByUrl(@Param("finish")Boolean finish, @Param("url")String url);
	
	@Transactional
	@Modifying
	@Query(value = "update ItemInfo set pushHomed = :pushHomed, pushHomedDate = :pushHomedDate where id in :id")
	void updatePushByIds(@Param("pushHomed")Boolean pushHomed, @Param("pushHomedDate")Long pushHomedDate, @Param("id")List<Integer> ids);
}
