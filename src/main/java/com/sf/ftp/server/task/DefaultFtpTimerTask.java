package com.sf.ftp.server.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.ftpserver.ftplet.FtpStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sf.ftp.server.config.SystemConfig;
import com.sf.ftp.server.dao.FtpRecordDao;

/**
 * ftp定时任务
 * 
 * @author abner.li
 * @date 2020年1月30日下午5:59:21
 */
public class DefaultFtpTimerTask implements FtpTimerTask {
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultFtpTimerTask.class);
	
	private SystemConfig systemConfig;

	private FtpRecordDao ftpRecordDao;
	
	private FtpStatistics ftpStatistics;

	public DefaultFtpTimerTask(SystemConfig systemConfig, FtpRecordDao ftpRecordDao,FtpStatistics ftpStatistics) {
		this.systemConfig = systemConfig;
		this.ftpRecordDao = ftpRecordDao;
		this.ftpStatistics = ftpStatistics;
	}

	@Override
	public void timerUpdateStatistics() {
		systemConfig.getScheduledThreadPool().scheduleAtFixedRate(() -> {
			try {
				ftpRecordDao.updateStatistics(ftpStatistics);
			}catch (Exception e) {
				logger.error("timerUpdateStatistics exception",e);
			}
		}, 1, 5, TimeUnit.SECONDS);
	}


	@Override
	public void timerCleanExpiresUser() {
		int initialDelay = 24 - currentTimeByHour();
		systemConfig.getScheduledThreadPool().scheduleAtFixedRate(() -> {
			try {
				ftpRecordDao.disabledUserByDate(formatTimeByDay(0));
				ftpRecordDao.deleteUserByDate(formatTimeByDay(-7));
			}catch (Exception e) {
				logger.error("timerCleanExpiresUser exception",e);
			}
		}, initialDelay, 24, TimeUnit.HOURS);

	}
	
	private String formatTimeByDay(int day){
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, day);
			return dateFormat.format(calendar.getTime());
		}catch(Exception e){
			return "";
		}
	}
	
	
	private int currentTimeByHour(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
		return Integer.parseInt(dateFormat.format(new Date()));
	}

}
