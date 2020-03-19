package com.sf.ftp.server.task;

/**
 * 定时任务
 * @author abner.li
 * @date 2020年1月30日下午8:31:50
 */
public interface FtpTimerTask {
	
	/**
	 * 定时更新统计信息
	 */
	public void timerUpdateStatistics();

	/**
	 * 定期清理过期用户
	 * -过期禁用账号
	 */
	public void timerCleanExpiresUser();

}
