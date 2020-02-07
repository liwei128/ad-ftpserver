package com.sf.ftp.web.dao;

import com.sf.ftp.web.beans.po.FtpStatisticsEntity;

/**
 * 全局统计信息
 * @author abner.li
 * @date 2020年2月4日下午12:42:23
 */
public interface FtpStatisticsEntityMapper {

	/**
	 * 查询
	 * @return
	 * FtpStatisticsEntity
	 */
	FtpStatisticsEntity query();

}