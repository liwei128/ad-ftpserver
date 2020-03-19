package com.sf.ftp.server.config;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.sql.DataSource;

import org.apache.ftpserver.ConnectionConfig;

/**
 * 系统配置
 * @author abner.li
 * @date 2020年2月1日下午2:17:09
 */
public interface SystemConfig {
	
	public String FTP_PATH = "ftpPath";
	public String LDAP_URL = "ldapUrl";
	public String AD_PREFIX = "adPrefix";
	public String PORT = "port";
	public String ACTIVE_PORT = "activeport";
	public String PASSIVE_PORT = "passiveport";
	
	/**
	 * 获取数据源
	 * @return
	 * DataSource
	 */
	public DataSource getDataSource();
	
	/**
	 * 获取ftp连接配置
	 * @return
	 * ConnectionConfig
	 */
	public ConnectionConfig getConnectionConfig();
	
	/**
	 * 获取定时任务线程池
	 * @return
	 * ScheduledThreadPoolExecutor
	 */
	public ScheduledThreadPoolExecutor getScheduledThreadPool();
	
	/**
	 * 获取配置参数
	 * @param key
	 * @return
	 * String
	 */
	public String getProperty(String key);

}
