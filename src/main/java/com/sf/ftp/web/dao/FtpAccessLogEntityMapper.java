package com.sf.ftp.web.dao;

import java.util.List;

import com.sf.ftp.web.beans.dto.LogCondition;
import com.sf.ftp.web.beans.po.FtpAccessLogEntity;

/**
 * 访问日志
 * @author abner.li
 * @date 2020年2月4日下午12:41:40
 */
public interface FtpAccessLogEntityMapper {

	/**
	 * 查询记录数
	 * @param condition
	 * @return
	 * int
	 */
	int queryCountByCondition(LogCondition condition);

	/**
	 * 查询列表
	 * @param condition
	 * @return
	 * List<FtpAccessLogEntity>
	 */
	List<FtpAccessLogEntity> queryByCondition(LogCondition condition);

	/**
	 * 查询所有
	 * @param condition
	 * @return
	 * List<FtpAccessLogEntity>
	 */
	List<FtpAccessLogEntity> queryAllByCondition(LogCondition condition);
	

}