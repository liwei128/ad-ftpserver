package com.sf.ftp.web.beans.common;

/**
 * 基本响应状态码
 * @author abner.li
 * @date 2020年2月4日上午11:27:38
 */
public enum BaseRetCode implements RetCode{
	
	/**
	 * 成功
	 */
	SUCCESS(0,"成功"),
	/**
	 * 失败
	 */
	FAIL(1,"失败"),
	/**
	 * 未登录
	 */
	NOT_LOGIN(2,"未登录"), 
	
	/**
	 * 账号或密码不能为空
	 */
	EMPTY_USER(3,"账号或密码不能为空"), 
	
	/**
	 * 无访问权限
	 */
	NOT_PERMISSION(4,"无访问权限"), 
	
	/**
	 * 账号或密码错误
	 */
	PWD_ERR(5,"账号或密码错误"),
	
	/**
	 * 参数缺失
	 */
	PARAM_MISSING(6,"参数缺失"), 
	
	/**
	 * 域账号不存在
	 */
	NOT_AD_ACCOUNT(7,"域账号不存在"), 
	
	/**
	 * 认证失败
	 */
	AUTH_FAILD(8,"认证失败");

	
	private int code;
	
	private String msg;

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	private BaseRetCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
