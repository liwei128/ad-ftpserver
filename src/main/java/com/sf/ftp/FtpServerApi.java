package com.sf.ftp;

import org.apache.ftpserver.ftplet.FtpStatistics;

/**
 * ftp对外服务接口,通过spi加载
 * @author abner.li
 * @date 2020年2月7日上午10:27:51
 */
public interface FtpServerApi {
	
	/**
	 * 获取累计统计信息
	 * @return
	 * FtpStatistics
	 */
	public FtpStatistics getFtpStatistics();

}
