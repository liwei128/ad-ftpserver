package com.sf.ftp.web.beans.dto;


/**
 * 日志列表查询条件
 * @author abner.li
 * @date 2020年2月4日下午7:40:02
 */
public class LogCondition extends PageCondition{
	
	/**
	 * 工号
	 */
    private String userid;

    /**
     * ip
     */
    private String ip;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
    
    

}
