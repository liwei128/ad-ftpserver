package com.sf.ftp.web.service;

import com.sf.ftp.web.beans.common.ResultData;

/**
 * AD相关服务
 * @author abner.li
 * @date 2020年2月5日下午10:34:52
 */
public interface AdService {

	/**
	 * 域账号校验
	 * @param userid
	 * @param password
	 * @return
	 * boolean
	 */
	boolean checkUser(String userid, String password);

	/**
	 * 是否为域账号
	 * @param userid
	 * @return
	 * ResultData<String>
	 */
	ResultData<String> isAdUser(String userid);

}
