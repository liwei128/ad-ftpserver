package com.sf.ftp.web.beans.common;


/**
 * 响应状态码
 * @author abner.li
 * @date 2020年2月4日上午11:26:53
 */
public interface RetCode {
	
	/**
	 * 状态码
	 * @return
	 */
	public int getCode();
	
	/**
	 * 消息
	 * @return
	 */
	public String getMsg();
	
	/**
	 * 是否成功
	 * @return
	 */
	default boolean success(){
		return getCode() == BaseRetCode.SUCCESS.getCode();
	}

}
