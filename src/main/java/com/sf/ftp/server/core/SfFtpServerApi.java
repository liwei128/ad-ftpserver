package com.sf.ftp.server.core;

import org.apache.ftpserver.ftplet.FtpStatistics;

import com.sf.ftp.common.FtpServerApi;
import com.sf.ftp.server.dao.FtpRecordDao;

/**
 * ftp对外提供api接口操作
 * @author abner.li
 * @date 2020年2月7日上午10:49:06
 */
public class SfFtpServerApi implements FtpServerApi{
	
	private static FtpRecordDao ftpRecordDao;
	
	private static FtpStatistics ftpStatistics;
	
	private static volatile boolean initFinish = false;
	
	public static void init(FtpRecordDao ftpRecordDao,FtpStatistics ftpStatistics) {
		SfFtpServerApi.ftpRecordDao = ftpRecordDao;
		SfFtpServerApi.ftpStatistics = ftpStatistics;
		initFinish = true;
	}

	@Override
	public FtpStatistics getFtpStatistics() {
		if(initFinish) {
			return ftpRecordDao.getFtpStatistics(ftpStatistics);
		}
		return null;
	}

}
