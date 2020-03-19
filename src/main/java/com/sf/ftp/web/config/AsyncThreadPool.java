package com.sf.ftp.web.config;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 异步线程池
 * 可配合注解使用@Async("asyncThreadPool")
 * @author abner.li
 * @date 2020年3月19日下午1:54:38
 */
@Component("asyncThreadPool")
public class AsyncThreadPool extends ThreadPoolTaskExecutor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2305730842681731330L;
	
	@Autowired
	private ThreadPoolConfig threadPoolConfig;

	/**
	 * 初始化线程池
	 */
	@Override
	public void initialize() {
		setCorePoolSize(threadPoolConfig.getCorePoolSize());
		setMaxPoolSize(threadPoolConfig.getMaxPoolSize());
		setQueueCapacity(threadPoolConfig.getQueueCapacity());
		setKeepAliveSeconds(threadPoolConfig.getKeepAliveSeconds());
		setThreadNamePrefix("AsyncThreadPool-");
		setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		super.initialize();
	}
	

}
