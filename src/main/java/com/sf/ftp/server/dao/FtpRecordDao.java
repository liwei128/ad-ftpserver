package com.sf.ftp.server.dao;

import org.apache.ftpserver.ftplet.FtpStatistics;

import com.sf.ftp.server.bean.FtpRecord;
/**
 * 操作记录保存入库
 * @author abner.li
 * @date 2020年1月30日下午8:37:59
 */
public interface FtpRecordDao {
	/**
	 * 保存用户操作记录
	 * @param ftpRecord
	 */
	public void saveRecord(FtpRecord ftpRecord);
	
	/**
	 * 更新用户在线情况
	 * @param currentUserLoginNumber 
	 * @param userid 
	 */
	public void updateUserLoginNumber(String userid, int currentUserLoginNumber);
	
	/**
	 * 重置用户登录状态
	 * 
	 * void
	 */
	public void resetUserLoginStatus();
	
	/**
	 * 更新统计信息
	 * @param ftpStatistics
	 * void
	 */
	public void updateStatistics(FtpStatistics ftpStatistics);
	
	/**
	 * 获取累计统计信息
	 * @param ftpStatistics
	 * @return
	 * FtpStatistics
	 */
	public FtpStatistics getFtpStatistics(FtpStatistics ftpStatistics);

	/**
	 * 根据时间禁用用户
	 * @param expiresDate yyyy-MM-dd
	 */
	public void disabledUserByDate(String expiresDate);

	/**
	 * 根据时间删除用户
	 * @param expiresDate yyyy-MM-dd
	 */
	public void deleteUserByDate(String expiresDate);

}
