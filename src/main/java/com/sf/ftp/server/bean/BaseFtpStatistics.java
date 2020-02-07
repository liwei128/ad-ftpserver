package com.sf.ftp.server.bean;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.ftpserver.ftplet.FtpStatistics;
import org.apache.ftpserver.ftplet.User;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

/**
 * 全局统计信息
 * @author abner.li
 * @date 2020年1月31日上午10:12:49
 */
public class BaseFtpStatistics implements FtpStatistics{
	
	/**
	 * 服务启动时间
	 */
	private Date startTime;
	
	/**
	 * 上传的文件数量
	 */
	private int totalUploadNumber;
	
	/**
	 * 下载的文件数量
	 */
	private int totalDownloadNumber;

	/**
	 * 删除的文件数量
	 */
	private int totalDeleteNumber;
	
	
	/**
	 * 上传的总文件大小,单位bytes
	 */
	private long totalUploadSize;
	
	/**
	 * 下载的总文件大小,单位bytes
	 */
	private long totalDownloadSize;

	/**
	 * 创建的总目录数
	 */
	private int totalDirectoryCreated;
	
	/**
	 * 删除的总目录数
	 */
	private int totalDirectoryRemoved;
	
	/**
	 * 连接总数
	 */
	private int totalConnectionNumber;

	/**
	 * 当前连接数
	 */
	private int currentConnectionNumber;
	/**
	 * 登录总数
	 */
	private int totalLoginNumber;
	
	/**
	 * 登录失败总数
	 */
	private int totalFailedLoginNumber;
	
	/**
	 * 当前登录数量
	 */
	private int currentLoginNumber;
	
	/**
	 * 匿名登录总数
	 */
	private int totalAnonymousLoginNumber;
	
	/**
	 * 当前匿名登录数量
	 */
	private int currentAnonymousLoginNumber;
	
	@Override
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@Override
	public int getTotalUploadNumber() {
		return totalUploadNumber;
	}

	public void setTotalUploadNumber(int totalUploadNumber) {
		this.totalUploadNumber = totalUploadNumber;
	}
	@Override
	public int getTotalDownloadNumber() {
		return totalDownloadNumber;
	}

	public void setTotalDownloadNumber(int totalDownloadNumber) {
		this.totalDownloadNumber = totalDownloadNumber;
	}
	@Override
	public int getTotalDeleteNumber() {
		return totalDeleteNumber;
	}

	public void setTotalDeleteNumber(int totalDeleteNumber) {
		this.totalDeleteNumber = totalDeleteNumber;
	}
	@Override
	public long getTotalUploadSize() {
		return totalUploadSize;
	}

	public void setTotalUploadSize(long totalUploadSize) {
		this.totalUploadSize = totalUploadSize;
	}
	@Override
	public long getTotalDownloadSize() {
		return totalDownloadSize;
	}

	public void setTotalDownloadSize(long totalDownloadSize) {
		this.totalDownloadSize = totalDownloadSize;
	}
	@Override
	public int getTotalDirectoryCreated() {
		return totalDirectoryCreated;
	}

	public void setTotalDirectoryCreated(int totalDirectoryCreated) {
		this.totalDirectoryCreated = totalDirectoryCreated;
	}
	@Override
	public int getTotalDirectoryRemoved() {
		return totalDirectoryRemoved;
	}

	public void setTotalDirectoryRemoved(int totalDirectoryRemoved) {
		this.totalDirectoryRemoved = totalDirectoryRemoved;
	}
	@Override
	public int getTotalConnectionNumber() {
		return totalConnectionNumber;
	}

	public void setTotalConnectionNumber(int totalConnectionNumber) {
		this.totalConnectionNumber = totalConnectionNumber;
	}
	@Override
	public int getCurrentConnectionNumber() {
		return currentConnectionNumber;
	}

	public void setCurrentConnectionNumber(int currentConnectionNumber) {
		this.currentConnectionNumber = currentConnectionNumber;
	}
	@Override
	public int getTotalLoginNumber() {
		return totalLoginNumber;
	}

	public void setTotalLoginNumber(int totalLoginNumber) {
		this.totalLoginNumber = totalLoginNumber;
	}
	@Override
	public int getTotalFailedLoginNumber() {
		return totalFailedLoginNumber;
	}

	public void setTotalFailedLoginNumber(int totalFailedLoginNumber) {
		this.totalFailedLoginNumber = totalFailedLoginNumber;
	}
	@Override
	public int getCurrentLoginNumber() {
		return currentLoginNumber;
	}

	public void setCurrentLoginNumber(int currentLoginNumber) {
		this.currentLoginNumber = currentLoginNumber;
	}
	@Override
	public int getTotalAnonymousLoginNumber() {
		return totalAnonymousLoginNumber;
	}

	public void setTotalAnonymousLoginNumber(int totalAnonymousLoginNumber) {
		this.totalAnonymousLoginNumber = totalAnonymousLoginNumber;
	}
	@Override
	public int getCurrentAnonymousLoginNumber() {
		return currentAnonymousLoginNumber;
	}

	public void setCurrentAnonymousLoginNumber(int currentAnonymousLoginNumber) {
		this.currentAnonymousLoginNumber = currentAnonymousLoginNumber;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	@Override
	public int getCurrentUserLoginNumber(User user) {
		throw new RuntimeException("BaseFtpStatistics unrealized method");
	}

	@Override
	public int getCurrentUserLoginNumber(User user, InetAddress ipAddress) {
		throw new RuntimeException("BaseFtpStatistics unrealized method");
	}
	
	
	/**
	 * 字段转map
	 * @param statistics
	 * @return
	 * Map<String,Object>
	 */
	public static Map<String, Object> toSqlMap(FtpStatistics statistics){
		Map<String, Object> map = Maps.newHashMap();
		map.put("startTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(statistics.getStartTime()));
		map.put("totalUploadNumber",statistics.getTotalUploadNumber());
		map.put("totalDownloadNumber",statistics.getTotalDownloadNumber());
		map.put("totalDeleteNumber",statistics.getTotalDeleteNumber());
		map.put("totalUploadSize",statistics.getTotalUploadSize());
		map.put("totalDownloadSize",statistics.getTotalDownloadSize());
		map.put("totalDirectoryCreated",statistics.getTotalDirectoryCreated());
		map.put("totalDirectoryRemoved",statistics.getTotalDirectoryRemoved());
		map.put("totalConnectionNumber",statistics.getTotalConnectionNumber());
		map.put("currentConnectionNumber",statistics.getCurrentConnectionNumber());
		map.put("totalLoginNumber",statistics.getTotalLoginNumber());
		map.put("totalFailedLoginNumber",statistics.getTotalFailedLoginNumber());
		map.put("currentLoginNumber",statistics.getCurrentLoginNumber());
		map.put("totalAnonymousLoginNumber",statistics.getTotalAnonymousLoginNumber());
		map.put("currentAnonymousLoginNumber",statistics.getCurrentAnonymousLoginNumber());
		return map;
	}
	
	/**
	 * 追加新数据
	 * @param historyStatistics
	 * @param ftpStatistics
	 * @return
	 * FtpStatistics
	 */
	public static FtpStatistics append(FtpStatistics historyStatistics,FtpStatistics ftpStatistics) {
		if (historyStatistics == null) {
			return ftpStatistics;
		}
		BaseFtpStatistics newStatistics = new BaseFtpStatistics();
		newStatistics.setCurrentAnonymousLoginNumber(ftpStatistics.getCurrentAnonymousLoginNumber());
		newStatistics.setCurrentConnectionNumber(ftpStatistics.getCurrentConnectionNumber());
		newStatistics.setCurrentLoginNumber(ftpStatistics.getCurrentLoginNumber());
		newStatistics.setStartTime(historyStatistics.getStartTime());
		newStatistics.setTotalAnonymousLoginNumber(historyStatistics.getTotalAnonymousLoginNumber() + ftpStatistics.getTotalAnonymousLoginNumber());
		newStatistics.setTotalConnectionNumber(historyStatistics.getTotalConnectionNumber() + ftpStatistics.getTotalConnectionNumber());
		newStatistics.setTotalDeleteNumber(historyStatistics.getTotalDeleteNumber() + ftpStatistics.getTotalDeleteNumber());
		newStatistics.setTotalDirectoryCreated(historyStatistics.getTotalDirectoryCreated() + ftpStatistics.getTotalDirectoryCreated());
		newStatistics.setTotalDirectoryRemoved(historyStatistics.getTotalDirectoryRemoved() + ftpStatistics.getTotalDirectoryRemoved());
		newStatistics.setTotalDownloadNumber(historyStatistics.getTotalDownloadNumber() + ftpStatistics.getTotalDownloadNumber());
		newStatistics.setTotalDownloadSize(historyStatistics.getTotalDownloadSize() + ftpStatistics.getTotalDownloadSize());
		newStatistics.setTotalFailedLoginNumber(historyStatistics.getTotalFailedLoginNumber() + ftpStatistics.getTotalFailedLoginNumber());
		newStatistics.setTotalLoginNumber(historyStatistics.getTotalLoginNumber() + ftpStatistics.getTotalLoginNumber());
		newStatistics.setTotalUploadNumber(historyStatistics.getTotalUploadNumber() + ftpStatistics.getTotalUploadNumber());
		newStatistics.setTotalUploadSize(historyStatistics.getTotalUploadSize() + ftpStatistics.getTotalUploadSize());
		return newStatistics;

	}

	/**
	 * sql查询字段转换为实体
	 * @param rs
	 * @return
	 * @throws Exception
	 * FtpStatistics
	 */
	public static FtpStatistics createByResultSet(ResultSet rs) throws Exception {
		while (rs.next()) {
			BaseFtpStatistics ftpStatistics = new BaseFtpStatistics();
			ftpStatistics.setCurrentAnonymousLoginNumber(rs.getInt("currentAnonymousLoginNumber"));
			ftpStatistics.setCurrentConnectionNumber(rs.getInt("currentConnectionNumber"));
			ftpStatistics.setCurrentLoginNumber(rs.getInt("currentLoginNumber"));
			Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("startTime"));
			ftpStatistics.setStartTime(startTime);
			ftpStatistics.setTotalAnonymousLoginNumber(rs.getInt("totalAnonymousLoginNumber"));
			ftpStatistics.setTotalConnectionNumber(rs.getInt("totalConnectionNumber"));
			ftpStatistics.setTotalDeleteNumber(rs.getInt("totalDeleteNumber"));
			ftpStatistics.setTotalDirectoryCreated(rs.getInt("totalDirectoryCreated"));
			ftpStatistics.setTotalDirectoryRemoved(rs.getInt("totalDirectoryRemoved"));
			ftpStatistics.setTotalDownloadNumber(rs.getInt("totalDownloadNumber"));
			ftpStatistics.setTotalDownloadSize(rs.getLong("totalDownloadSize"));
			ftpStatistics.setTotalFailedLoginNumber(rs.getInt("totalFailedLoginNumber"));
			ftpStatistics.setTotalLoginNumber(rs.getInt("totalLoginNumber"));
			ftpStatistics.setTotalUploadNumber(rs.getInt("totalUploadNumber"));
			ftpStatistics.setTotalUploadSize(rs.getLong("totalUploadSize"));
			return ftpStatistics;
		}
		return null;

	}

}
