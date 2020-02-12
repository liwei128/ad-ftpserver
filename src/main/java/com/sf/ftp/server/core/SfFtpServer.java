package com.sf.ftp.server.core;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpStatistics;

import com.sf.ftp.server.config.SfSystemConfig;
import com.sf.ftp.server.config.SystemConfig;
import com.sf.ftp.server.dao.SfFtpRecordDao;
import com.sf.ftp.server.dao.FtpRecordDao;
import com.sf.ftp.server.task.SfFtpTimerTask;
import com.sf.ftp.server.task.FtpTimerTask;

/**
 * ftp服务
 * 
 * @author abner.li
 * @date 2020年1月21日上午11:30:54
 */
public class SfFtpServer {
	
	private FtpServer ftpServer;

	private FtpTimerTask ftpTimerTask;

	public SfFtpServer() {
		SystemConfig systemConfig = new SfSystemConfig();
		FtpRecordDao ftpRecordDao = new SfFtpRecordDao(systemConfig);
		FtpStatistics ftpStatistics = new SfServerFtpStatistics(ftpRecordDao);
		SfFtpServerApi.init(ftpRecordDao, ftpStatistics);
		this.ftpTimerTask = new SfFtpTimerTask(systemConfig, ftpRecordDao,ftpStatistics);
		this.ftpServer = new SfFtpServerFactory(systemConfig, ftpRecordDao, ftpStatistics).createServer();
	}

	public void start() throws FtpException {
		// 启动服务
		ftpServer.start();
		// 启动定时任务
		ftpTimerTask.timerUpdateStatistics();
		ftpTimerTask.timerCleanExpiresUser();
	}

	public static void run() throws FtpException {
		new SfFtpServer().start();
	}
}
