package com.sf.ftp.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ftpserver.ftplet.FtpStatistics;
import org.apache.ftpserver.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.sf.ftp.server.bean.SfFtpStatistics;
import com.sf.ftp.server.bean.SfFtpRecord;
import com.sf.ftp.server.config.SystemConfig;

/**
 * 操作记录保存入库
 * 
 * @author abner.li
 * @date 2020年1月29日下午10:46:55
 */
public class SfFtpRecordDao implements FtpRecordDao {

	private final static Logger logger = LoggerFactory.getLogger(SfFtpRecordDao.class);

	private DataSource dataSource;

	private FtpStatistics historyStatistics;

	private String insertRecordStmt = "INSERT INTO ftp_access_log(userid, ip, operation, filepath, access_time) VALUES ('{userid}', '{ip}', '{operation}', '{filepath}', '{access_time}')";

	private String updateUserLoginStmt = "update ftp_user set current_login_number = '{current_login_number}' where userid = '{userid}' ";
	
	private String resetUserLoginStmt = "update ftp_user set current_login_number = '0' ";

	private String disabledUserByDateStmt = "update ftp_user set enableflag = '0',handler = 'system' where ISNULL(expires)=0 and LENGTH(trim(expires))>0 and expires < '{expires}'";

	private String loadHistoryStatisticsStmt = "select startTime,totalUploadNumber,totalDownloadNumber,totalDeleteNumber,totalUploadSize,totalDownloadSize,totalDirectoryCreated,totalDirectoryRemoved,totalConnectionNumber,currentConnectionNumber,totalLoginNumber,totalFailedLoginNumber,currentLoginNumber,totalAnonymousLoginNumber,currentAnonymousLoginNumber from ftp_statistics";

	private String insertStatisticsStmt = "INSERT INTO ftp_statistics(startTime,totalUploadNumber,totalDownloadNumber,totalDeleteNumber,totalUploadSize,totalDownloadSize,totalDirectoryCreated,totalDirectoryRemoved,totalConnectionNumber,currentConnectionNumber,totalLoginNumber,totalFailedLoginNumber,currentLoginNumber,totalAnonymousLoginNumber,currentAnonymousLoginNumber) VALUES ('{startTime}','{totalUploadNumber}','{totalDownloadNumber}','{totalDeleteNumber}','{totalUploadSize}','{totalDownloadSize}','{totalDirectoryCreated}','{totalDirectoryRemoved}','{totalConnectionNumber}','{currentConnectionNumber}','{totalLoginNumber}','{totalFailedLoginNumber}','{currentLoginNumber}','{totalAnonymousLoginNumber}','{currentAnonymousLoginNumber}') ";
	
	private String updateStatisticsStmt = "update ftp_statistics set startTime='{startTime}',totalUploadNumber='{totalUploadNumber}',totalDownloadNumber='{totalDownloadNumber}',totalDeleteNumber='{totalDeleteNumber}',totalUploadSize='{totalUploadSize}',totalDownloadSize='{totalDownloadSize}',totalDirectoryCreated='{totalDirectoryCreated}',totalDirectoryRemoved='{totalDirectoryRemoved}',totalConnectionNumber='{totalConnectionNumber}',currentConnectionNumber='{currentConnectionNumber}',totalLoginNumber='{totalLoginNumber}',totalFailedLoginNumber='{totalFailedLoginNumber}',currentLoginNumber='{currentLoginNumber}',totalAnonymousLoginNumber='{totalAnonymousLoginNumber}',currentAnonymousLoginNumber='{currentAnonymousLoginNumber}' ";

	
	public SfFtpRecordDao(SystemConfig systemConfig) {
		this.dataSource = systemConfig.getDataSource();
		this.historyStatistics = loadHistoryStatistics();
		resetUserLoginStatus();
	}


	@Override
	public void resetUserLoginStatus() {
		try (Connection connection = createConnection(); Statement stmt = connection.createStatement();) {
			stmt.executeUpdate(resetUserLoginStmt);
		} catch (Exception e) {
			logger.error("resetUserLoginStatus ", e);
		}
	}

	@Override
	public void saveRecord(SfFtpRecord ftpRecord) {
		try (Connection connection = createConnection(); Statement stmt = connection.createStatement();) {
			String sql = StringUtils.replaceString(insertRecordStmt, ftpRecord.toSqlMap());
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			logger.error("saveRecord:{}", ftpRecord, e);
		}

	}

	@Override
	public void updateUserLoginNumber(String userid, int currentUserLoginNumber) {
		try (Connection connection = createConnection(); Statement stmt = connection.createStatement();) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("userid", userid);
			map.put("current_login_number", currentUserLoginNumber);
			String sql = StringUtils.replaceString(updateUserLoginStmt, map);
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			logger.error("updateUserLoginNumber ", e);
		}
		
	}

	@Override
	public void updateStatistics(FtpStatistics ftpStatistics) {
		FtpStatistics newStatistics = getFtpStatistics(ftpStatistics);
		String sql = loadHistoryStatistics()==null?insertStatisticsStmt:updateStatisticsStmt;
		try (Connection connection = createConnection(); Statement stmt = connection.createStatement();) {
			Map<String,Object> map = SfFtpStatistics.toSqlMap(newStatistics);
			String executeSql = StringUtils.replaceString(sql, map);
			stmt.executeUpdate(executeSql);
		} catch (Exception e) {
			logger.error("updateStatistics ", e);
		}
	}
	
	@Override
	public FtpStatistics getFtpStatistics(FtpStatistics ftpStatistics) {
		return SfFtpStatistics.append(historyStatistics,ftpStatistics);
	}

	@Override
	public void disabledUserByDate(String expiresDate) {
		try (Connection connection = createConnection(); Statement stmt = connection.createStatement();) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("expires", expiresDate);
			String sql = StringUtils.replaceString(disabledUserByDateStmt, map);
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			logger.error("disabledUserByDate ", e);
		}
	}

	
	private FtpStatistics loadHistoryStatistics() {
		try (Connection connection = createConnection();
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(loadHistoryStatisticsStmt);) {
			return SfFtpStatistics.createByResultSet(rs);
		} catch (Exception e) {
			logger.error("loadHistoryStatistics ", e);
		}
		return null;
	}

	private Connection createConnection() throws SQLException {
		Connection connection = dataSource.getConnection();
		connection.setAutoCommit(true);
		return connection;
	}

}
