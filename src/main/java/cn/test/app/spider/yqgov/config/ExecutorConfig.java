package cn.test.app.spider.yqgov.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年6月23日 下午5:10:45
 */
@Configuration
//@EnableAsync
public class ExecutorConfig {
	private int corePoolSize = 100;
	private int maxPoolSize = 1000;
	private int queueCapacity = 500;
	
	
	@Bean
	public Executor myAsync() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("MyExecutor-");
		
		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		// CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		
		executor.initialize();
		return executor;
	}
}
