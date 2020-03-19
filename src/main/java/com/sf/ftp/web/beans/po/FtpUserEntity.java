package com.sf.ftp.web.beans.po;


import com.alibaba.fastjson.JSON;
import com.sf.ftp.common.UserType;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 用户信息
 * @author abner.li
 * @date 2020年2月4日下午12:39:05
 */
public class FtpUserEntity {
	
	@Excel(name = "工号")
    private String userid;
	
	@Excel(name = "邮箱")
    private String email;
	
	private String password;
	
	@Excel(name = "账号类型",replace= {"域账号_AD","外部账号_USER"})
    private UserType usertype;

	@Excel(name = "主目录")
    private String homedirectory;

	@Excel(name = "启用",replace= {"是_true","否_false"})
    private Boolean enableflag;

	@Excel(name = "最大空闲时间(秒)")
    private Integer idletime;

	@Excel(name = "访问权限")
    private String permission;

	@Excel(name = "管理员权限",replace= {"是_true","否_false"})
    private Boolean adminpermission;

	@Excel(name = "最大登录限制")
    private Integer maxloginnumber;

	@Excel(name = "同ip最大登录限制")
    private Integer maxloginperip;

	@Excel(name = "下载速率(byte)")
    private Integer downloadrate;

	@Excel(name = "上传速率(byte)")
    private Integer uploadrate;

    private Integer currentLoginNumber;

	@Excel(name = "有效期")
    private String expires;
	
    private String handler;
    
    private String createTime;
	

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getHomedirectory() {
        return homedirectory;
    }

    public void setHomedirectory(String homedirectory) {
        this.homedirectory = homedirectory;
    }

    public Boolean getEnableflag() {
        return enableflag;
    }

    public void setEnableflag(Boolean enableflag) {
        this.enableflag = enableflag;
    }

    public Integer getIdletime() {
        return idletime;
    }

    public void setIdletime(Integer idletime) {
        this.idletime = idletime;
    }

    public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Boolean getAdminpermission() {
        return adminpermission;
    }

    public void setAdminpermission(Boolean adminpermission) {
        this.adminpermission = adminpermission;
    }

    public Integer getMaxloginnumber() {
        return maxloginnumber;
    }

    public void setMaxloginnumber(Integer maxloginnumber) {
        this.maxloginnumber = maxloginnumber;
    }

    public Integer getMaxloginperip() {
        return maxloginperip;
    }

    public void setMaxloginperip(Integer maxloginperip) {
        this.maxloginperip = maxloginperip;
    }

    public Integer getDownloadrate() {
        return downloadrate;
    }

    public void setDownloadrate(Integer downloadrate) {
        this.downloadrate = downloadrate;
    }

    public Integer getUploadrate() {
        return uploadrate;
    }

    public void setUploadrate(Integer uploadrate) {
        this.uploadrate = uploadrate;
    }

    public Integer getCurrentLoginNumber() {
        return currentLoginNumber;
    }

    public void setCurrentLoginNumber(Integer currentLoginNumber) {
        this.currentLoginNumber = currentLoginNumber;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserType getUsertype() {
		return usertype;
	}

	public void setUsertype(UserType usertype) {
		this.usertype = usertype;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
    
}