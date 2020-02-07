package com.sf.ftp.web.dao;


import java.util.List;

import com.sf.ftp.web.beans.dto.UserCondition;
import com.sf.ftp.web.beans.po.FtpUserEntity;

/**
 * 用户管理
 * @author abner.li
 * @date 2020年2月4日下午12:43:26
 */
public interface FtpUserEntityMapper {
	
	/**
	 * 删除
	 * @param userid
	 * @return
	 * int
	 */
    int deleteByUserId(String userid);

    /**
     * 添加
     * @param record
     * @return
     * int
     */
    int insert(FtpUserEntity record);

    /**
     * 查询
     * @param userid
     * @return
     * FtpUserEntity
     */
    FtpUserEntity selectByUserId(String userid);

    /**
     * 更新
     * @param record
     * @return
     * int
     */
    int updateByUserId(FtpUserEntity record);

    /**
     * 查询记录数
     * @param condition
     * @return
     * int
     */
	int queryCountByCondition(UserCondition condition);

	/**
	 * 查询列表
	 * @param condition
	 * @return
	 * List<FtpUserEntity>
	 */
	List<FtpUserEntity> queryByCondition(UserCondition condition);
}