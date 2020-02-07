package com.sf.ftp.web.beans.dto;


/**
 * 用户列表查询条件
 * @author abner.li
 * @date 2020年2月4日下午7:40:02
 */
public class UserCondition extends PageCondition{
	
	/**
	 * 用户名
	 */
    private String userid;
    
    /**
     * 启用
     */
    private Boolean enableflag;
    
    /**
     * 在线
     */
    private Boolean onLine;
    
    /**
     * 管理员权限
     */
    private Boolean adminpermission;
    
	public Boolean getAdminpermission() {
		return adminpermission;
	}

	public void setAdminpermission(Boolean adminpermission) {
		this.adminpermission = adminpermission;
	}

	public Boolean getOnLine() {
		return onLine;
	}

	public void setOnLine(Boolean onLine) {
		this.onLine = onLine;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Boolean getEnableflag() {
		return enableflag;
	}

	public void setEnableflag(Boolean enableflag) {
		this.enableflag = enableflag;
	}

	
    
    

}
