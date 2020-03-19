package com.sf.ftp.web.service;

import org.springframework.web.multipart.MultipartFile;

import com.sf.ftp.web.beans.common.PagingData;
import com.sf.ftp.web.beans.common.ResultData;
import com.sf.ftp.web.beans.dto.LogCondition;
import com.sf.ftp.web.beans.dto.UserCondition;
import com.sf.ftp.web.beans.po.FtpAccessLogEntity;
import com.sf.ftp.web.beans.po.FtpStatisticsEntity;
import com.sf.ftp.web.beans.po.FtpUserEntity;

/**
 * ftp管理相关服务
 * @author abner.li
 * @date 2020年2月4日下午12:50:29
 */
public interface FtpManageService {

	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 * ResultData<Void>
	 */
	ResultData<Void> doLogin(String username, String password);

	/**
	 * 查询列表
	 * @param condition
	 * @return
	 * PagingData<FtpUserEntity>
	 */
	PagingData<FtpUserEntity> userList(UserCondition condition);

	/**
	 * 删除账号
	 * @param userid
	 * @return
	 * ResultData<Void>
	 */
	ResultData<Void> delete(String userid);

	/**
	 * 修改账号
	 * @param ftpUserEntity
	 * @return
	 * ResultData<Void>
	 */
	ResultData<Void> modify(FtpUserEntity ftpUserEntity);

	/**
	 * 添加账号
	 * @param ftpUserEntity
	 * @return
	 * ResultData<Void>
	 */
	ResultData<Void> add(FtpUserEntity ftpUserEntity);
	
	/**
	 * 查询统计信息
	 * @return
	 * ResultData<FtpStatisticsEntity>
	 */
	ResultData<FtpStatisticsEntity> statistics();
	
	/**
	 * 日志查询
	 * @param condition
	 * @return
	 * PagingData<FtpAccessLogEntity>
	 */
	PagingData<FtpAccessLogEntity> logList(LogCondition condition);

	/**
	 * 查询用户
	 * @param userid
	 * @return
	 * FtpUserEntity
	 */
	FtpUserEntity selectByUserId(String userid);

	/**
	 * 导入
	 * @param file
	 * @return
	 * String
	 */
	String importExcel(MultipartFile file);

	/**
	 * 导出用户
	 * @param condition
	 * @throws Exception
	 * void
	 */
	void exportUser(UserCondition condition) throws Exception ;
	
	
	/**
	 * 导出日志
	 * @param condition
	 * @throws Exception
	 * void
	 */
	void exportLog(LogCondition condition) throws Exception ;

	/**
	 * 重置密码
	 * @param userid
	 * @return
	 * ResultData<Void>
	 */
	ResultData<Void> retPwd(String userid);

}
