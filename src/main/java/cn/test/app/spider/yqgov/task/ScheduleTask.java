package cn.test.app.spider.yqgov.task;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月5日 下午4:16:44
 */
@Component
public class ScheduleTask {
	
	@Resource
	private AsyncTask task;
	
	
	// 每个小时更新一次新闻列表
	@Scheduled(cron = "0 0 * * * *")
	public void getColumnItems() {
		task.getColumnItems();
	}
	
	@Scheduled(cron = "0 0/10 * * * *")
	public void crawlArticles () {
		task.crawlArticles();
	}
	
	@Scheduled(cron = "0 0/10 * * * ?")
	public void pushToHomed() {
		task.pushToHomed();
	}
}
