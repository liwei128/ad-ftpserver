package com.sf.ftp.server.config;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.apache.ftpserver.ConnectionConfig;
import org.apache.ftpserver.impl.DefaultConnectionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 系统配置项管理
 * 
 * @author abner.li
 * @date 2020年1月21日上午11:30:40
 */
public class SfSystemConfig implements SystemConfig{

	private static final Logger logger = LoggerFactory.getLogger(SfSystemConfig.class);
	
	private final String configPath = "ftp.properties";
	
	private final String dataSourcePrefix = "dataSource.";

	private Properties properties;

	private DataSource dataSource;
	
	private ConnectionConfig connectionConfig;
	
	private ScheduledThreadPoolExecutor scheduledThreadPool;

	public SfSystemConfig() {
		this.properties = initProperties();
		this.dataSource = initDataSource();
		this.connectionConfig = initConnectionConfig();
		this.scheduledThreadPool = initScheduledThreadPool();
	}

	private Properties initProperties() {
		Properties newProperties = new Properties();
		try (InputStream in = SfSystemConfig.class.getClassLoader().getResourceAsStream(configPath);) {
			newProperties.load(in);
		} catch (Exception e) {
			logger.error("getProperties read exception", e);
		}
		return newProperties;
	}

	private DataSource initDataSource() {
		Properties dbProperties = new Properties();
		properties.forEach((key,value)->{
			if(key.toString().startsWith(dataSourcePrefix)) {
				dbProperties.put(key.toString().substring(dataSourcePrefix.length()), value);
			}
		});
		HikariConfig conf = new HikariConfig(dbProperties);
		return new HikariDataSource(conf);
	}

	private ConnectionConfig initConnectionConfig() {
		boolean anonymousLoginEnabled = Boolean.valueOf(properties.getProperty("anonymousLoginEnabled"));
		int loginFailureDelay = Integer.valueOf(properties.getProperty("loginFailureDelay"));
		int maxLogins = Integer.valueOf(properties.getProperty("maxLogins"));
		int maxAnonymousLogins = Integer.valueOf(properties.getProperty("maxAnonymousLogins"));
		int maxLoginFailures = Integer.valueOf(properties.getProperty("maxLoginFailures"));
		int maxThreads = Integer.valueOf(properties.getProperty("maxThreads"));
		return new DefaultConnectionConfig(anonymousLoginEnabled, loginFailureDelay, maxLogins, maxAnonymousLogins,
				maxLoginFailures, maxThreads);
	}
	
	private ScheduledThreadPoolExecutor initScheduledThreadPool() {
		int timerPoolSize = Integer.parseInt(properties.getProperty("timerPoolSize"));
		ThreadFactory threadFactory = new ThreadFactory() {
			private final AtomicInteger threadNumber = new AtomicInteger(1);
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "ftpTimerPool-Thread-"+threadNumber.getAndIncrement());
			}
			
		};
		return  new ScheduledThreadPoolExecutor(timerPoolSize,threadFactory);
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public ConnectionConfig getConnectionConfig() {
		return connectionConfig;
	}

	@Override
	public ScheduledThreadPoolExecutor getScheduledThreadPool() {
		return scheduledThreadPool;
	}

	@Override
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	

}
