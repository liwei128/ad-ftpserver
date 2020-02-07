package com.sf.ftp.web.service;

/**
 * AD相关服务
 * @author abner.li
 * @date 2020年2月5日下午10:34:52
 */
public interface AdService {

	/**
	 * 域账号校验
	 * @param username
	 * @param password
	 * @return
	 * boolean
	 */
	boolean checkUser(String username, String password);

	/**
	 * 是否为域账号
	 * @param userid
	 * @return
	 * boolean
	 */
	boolean isAdUser(String userid);

}
