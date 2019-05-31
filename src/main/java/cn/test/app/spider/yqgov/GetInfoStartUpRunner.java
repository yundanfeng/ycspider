package cn.test.app.spider.yqgov;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.test.app.spider.yqgov.service.IColumnService;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年7月5日 下午4:08:10
 */
@Component
@Order(value = 1)
public class GetInfoStartUpRunner implements CommandLineRunner {
	
	private static Logger logger = LoggerFactory.getLogger(GetInfoStartUpRunner.class);

	@Resource
	private IColumnService columnService;
	
	@Override
	public void run(String... arg0) {
		try {
			logger.info("by luzh-> 阳泉政务网站数据爬取工程启动，初始化数据。");
			columnService.getColumnItems();
		} catch (Exception e) {
			logger.error("by luzh-> 初始化数据出错，{}", e.getMessage());
		}
	}
}
