package com.sf.ftp.server.core;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.filesystem.nativefs.NativeFileSystemFactory;
import org.apache.ftpserver.ftplet.FileSystemFactory;
import org.apache.ftpserver.ftplet.FtpStatistics;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.impl.DefaultFtpServerContext;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;

import com.sf.ftp.server.config.SystemConfig;
import com.sf.ftp.server.dao.FtpRecordDao;

/**
 * 构建ftp服务工厂
 * @author abner.li
 * @date 2020年2月6日下午7:58:23
 */
public class SfFtpServerFactory {
	
	private SystemConfig systemConfig;
	
	private FtpRecordDao ftpRecordDao;
	
	private FtpStatistics ftpStatistics;

	public SfFtpServerFactory(SystemConfig systemConfig, FtpRecordDao ftpRecordDao, FtpStatistics ftpStatistics) {
		super();
		this.systemConfig = systemConfig;
		this.ftpRecordDao = ftpRecordDao;
		this.ftpStatistics = ftpStatistics;
	}
	
	public FtpServer createServer() {
		
		DefaultFtpServerContext serverContext = new DefaultFtpServerContext();
		//用户管理
		serverContext.setUserManager(new SfUserManager(systemConfig).getUserManager());
		//连接配置
		serverContext.setConnectionConfig(systemConfig.getConnectionConfig());
		//服务监听
		serverContext.addListener("default", defaultListener());
		//文件系统
		serverContext.setFileSystemManager(fileSystem());
		//记录用户操作-增强逻辑
		serverContext.getFtpletContainer().getFtplets().put("default", new SfFtplet(ftpRecordDao));
		//增强统计逻辑
		serverContext.setFtpStatistics(ftpStatistics);
		//创建
		return new DefaultFtpServer(serverContext);
	}
	
	/**
	 * 目录不存在时，自动创建
	 * 
	 * @return
	 */
	private FileSystemFactory fileSystem() {
		NativeFileSystemFactory fileSystem = new NativeFileSystemFactory();
		fileSystem.setCreateHome(true);
		return fileSystem;
	}

	/**
	 * 监听ftp
	 * 
	 * @return
	 */
	private Listener defaultListener() {
		ListenerFactory listenerFactory = new ListenerFactory();
		int port = Integer.parseInt(systemConfig.getProperty(SystemConfig.PORT));
		listenerFactory.setPort(port);
		return listenerFactory.createListener();
	}

   
}
