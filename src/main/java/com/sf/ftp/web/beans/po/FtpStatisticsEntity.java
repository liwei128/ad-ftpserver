package com.sf.ftp.web.beans.po;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ftpserver.ftplet.FtpStatistics;

import com.alibaba.fastjson.JSON;

/**
 * 全局统计信息
 * @author abner.li
 * @date 2020年2月4日下午12:38:25
 */
public class FtpStatisticsEntity {
	
    private Long id;

    private String starttime;

    private String totaluploadnumber;

    private String totaldownloadnumber;

    private String totaldeletenumber;

    private String totaluploadsize;

    private String totaldownloadsize;

    private String totaldirectorycreated;

    private String totaldirectoryremoved;

    private String totalconnectionnumber;

    private String currentconnectionnumber;

    private String totalloginnumber;

    private String totalfailedloginnumber;

    private String currentloginnumber;

    private String totalanonymousloginnumber;

    private String currentanonymousloginnumber;

    private Date updatetime;

    public FtpStatisticsEntity(FtpStatistics ftpStatistics) {
    	this.id = 0L;
		this.starttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ftpStatistics.getStartTime());
		this.totaluploadnumber = String.valueOf(ftpStatistics.getTotalUploadNumber());
		this.totaldownloadnumber = String.valueOf(ftpStatistics.getTotalDownloadNumber());
		this.totaldeletenumber = String.valueOf(ftpStatistics.getTotalDeleteNumber());
		this.totaluploadsize = String.valueOf(ftpStatistics.getTotalUploadSize());
		this.totaldownloadsize = String.valueOf(ftpStatistics.getTotalDownloadSize());
		this.totaldirectorycreated = String.valueOf(ftpStatistics.getTotalDirectoryCreated());
		this.totaldirectoryremoved = String.valueOf(ftpStatistics.getTotalDirectoryRemoved());
		this.totalconnectionnumber = String.valueOf(ftpStatistics.getTotalConnectionNumber());
		this.currentconnectionnumber = String.valueOf(ftpStatistics.getCurrentConnectionNumber());
		this.totalloginnumber = String.valueOf(ftpStatistics.getTotalLoginNumber());
		this.totalfailedloginnumber = String.valueOf(ftpStatistics.getTotalFailedLoginNumber());
		this.currentloginnumber = String.valueOf(ftpStatistics.getCurrentLoginNumber());
		this.totalanonymousloginnumber = String.valueOf(ftpStatistics.getTotalAnonymousLoginNumber());
		this.currentanonymousloginnumber = String.valueOf(ftpStatistics.getCurrentAnonymousLoginNumber());
		this.updatetime = new Date();
	}


	public FtpStatisticsEntity() {
		super();
	}


	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getTotaluploadnumber() {
        return totaluploadnumber;
    }

    public void setTotaluploadnumber(String totaluploadnumber) {
        this.totaluploadnumber = totaluploadnumber;
    }

    public String getTotaldownloadnumber() {
        return totaldownloadnumber;
    }

    public void setTotaldownloadnumber(String totaldownloadnumber) {
        this.totaldownloadnumber = totaldownloadnumber;
    }

    public String getTotaldeletenumber() {
        return totaldeletenumber;
    }

    public void setTotaldeletenumber(String totaldeletenumber) {
        this.totaldeletenumber = totaldeletenumber;
    }

    public String getTotaluploadsize() {
        return totaluploadsize;
    }

    public void setTotaluploadsize(String totaluploadsize) {
        this.totaluploadsize = totaluploadsize;
    }

    public String getTotaldownloadsize() {
        return totaldownloadsize;
    }

    public void setTotaldownloadsize(String totaldownloadsize) {
        this.totaldownloadsize = totaldownloadsize;
    }

    public String getTotaldirectorycreated() {
        return totaldirectorycreated;
    }

    public void setTotaldirectorycreated(String totaldirectorycreated) {
        this.totaldirectorycreated = totaldirectorycreated;
    }

    public String getTotaldirectoryremoved() {
        return totaldirectoryremoved;
    }

    public void setTotaldirectoryremoved(String totaldirectoryremoved) {
        this.totaldirectoryremoved = totaldirectoryremoved;
    }

    public String getTotalconnectionnumber() {
        return totalconnectionnumber;
    }

    public void setTotalconnectionnumber(String totalconnectionnumber) {
        this.totalconnectionnumber = totalconnectionnumber;
    }

    public String getCurrentconnectionnumber() {
        return currentconnectionnumber;
    }

    public void setCurrentconnectionnumber(String currentconnectionnumber) {
        this.currentconnectionnumber = currentconnectionnumber;
    }

    public String getTotalloginnumber() {
        return totalloginnumber;
    }

    public void setTotalloginnumber(String totalloginnumber) {
        this.totalloginnumber = totalloginnumber;
    }

    public String getTotalfailedloginnumber() {
        return totalfailedloginnumber;
    }

    public void setTotalfailedloginnumber(String totalfailedloginnumber) {
        this.totalfailedloginnumber = totalfailedloginnumber;
    }

    public String getCurrentloginnumber() {
        return currentloginnumber;
    }

    public void setCurrentloginnumber(String currentloginnumber) {
        this.currentloginnumber = currentloginnumber;
    }

    public String getTotalanonymousloginnumber() {
        return totalanonymousloginnumber;
    }

    public void setTotalanonymousloginnumber(String totalanonymousloginnumber) {
        this.totalanonymousloginnumber = totalanonymousloginnumber;
    }

    public String getCurrentanonymousloginnumber() {
        return currentanonymousloginnumber;
    }

    public void setCurrentanonymousloginnumber(String currentanonymousloginnumber) {
        this.currentanonymousloginnumber = currentanonymousloginnumber;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}