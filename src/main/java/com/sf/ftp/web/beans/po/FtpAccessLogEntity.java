package com.sf.ftp.web.beans.po;

import com.alibaba.fastjson.JSON;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 访问日志
 * @author abner.li
 * @date 2020年2月4日下午12:37:53
 */
public class FtpAccessLogEntity {
	
    private Long id;

	@Excel(name = "工号")
    private String userid;

	@Excel(name = "ip")
    private String ip;

	@Excel(name = "操作")
    private String operation;

	@Excel(name = "文件路径")
    private String filepath;

	@Excel(name = "访问时间")
    private String accessTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }
    
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}