package com.sf.ftp.server.core;

import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.impl.DefaultFtpStatistics;
import org.apache.ftpserver.impl.FtpIoSession;

import com.sf.ftp.server.dao.FtpRecordDao;

/**
 * 自定义统计信息
 * @author abner.li
 * @date 2020年2月6日下午8:50:43
 */
public class SfServerFtpStatistics extends DefaultFtpStatistics{
	
	private FtpRecordDao ftpRecordDao;
	
	public SfServerFtpStatistics(FtpRecordDao ftpRecordDao) {
		this.ftpRecordDao = ftpRecordDao;
	}

	@Override
	public synchronized void setLogin(FtpIoSession session) {
		super.setLogin(session);
		User user = session.getUser();
		int currentUserLoginNumber = getCurrentUserLoginNumber(user);
		ftpRecordDao.updateUserLoginNumber(user.getName(),currentUserLoginNumber);
	}

	@Override
	public synchronized void setLogout(FtpIoSession session) {
		super.setLogout(session);
		User user = session.getUser();
        if (user == null) {
            return;
        }
		int currentUserLoginNumber = getCurrentUserLoginNumber(user);
		ftpRecordDao.updateUserLoginNumber(user.getName(),currentUserLoginNumber);
	}

}
