package com.sf.ftp.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSON;

/**
 * 线程池配置
 * @author abner.li
 * @date 2020年3月19日下午1:54:21
 */
@Configuration
@ConfigurationProperties(prefix = ThreadPoolConfig.CONF_PREFIX)
public class ThreadPoolConfig {
	
	public static final String CONF_PREFIX = "threadpool";
	
	/**
	 * 核心线程数
	 */
	private int corePoolSize;

	/**
	 * 最大线程数
	 */
	private int maxPoolSize;

	/**
	 * 线程允许的空闲时间
	 */
	private int keepAliveSeconds;

	/**
	 * 缓冲队列的容量
	 */
	private int queueCapacity;
	
	/**
	 * 定时任务线程池的大小
	 */
	private int schedulerSize;
	

	public int getSchedulerSize() {
		return schedulerSize;
	}

	public void setSchedulerSize(int schedulerSize) {
		this.schedulerSize = schedulerSize;
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}

	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	

}
