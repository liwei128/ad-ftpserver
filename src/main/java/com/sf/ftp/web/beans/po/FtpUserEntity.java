package com.sf.ftp.web.beans.po;

import com.alibaba.fastjson.JSON;

/**
 * 用户信息
 * @author abner.li
 * @date 2020年2月4日下午12:39:05
 */
public class FtpUserEntity {
	
    private String userid;

    private String homedirectory;

    private Boolean enableflag;

    private Integer idletime;

    private Boolean writepermission;

    private Boolean adminpermission;

    private Integer maxloginnumber;

    private Integer maxloginperip;

    private Integer downloadrate;

    private Integer uploadrate;

    private Integer currentLoginNumber;

    private String expires;

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

    public Boolean getWritepermission() {
        return writepermission;
    }

    public void setWritepermission(Boolean writepermission) {
        this.writepermission = writepermission;
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

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
    
    
    
}