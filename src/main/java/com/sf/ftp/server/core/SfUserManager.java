package com.sf.ftp.server.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.sql.DataSource;

import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.DbUserManager;
import org.apache.ftpserver.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.sf.ftp.server.bean.SfBaseUser;
import com.sf.ftp.server.config.SystemConfig;

/**
 * 用户管理 用户配置存储mysql ad校验用户登录
 * 
 * 1.弃用writepermission字段，默认都有写权限
 * 2.弃用userpassword字段，密码从ad校验
 * 
 * @author abner.li
 * @date 2020年1月21日上午11:29:49
 */
public class SfUserManager {
	
	private final static Logger logger = LoggerFactory.getLogger(SfUserManager.class);

	private String selectAllStmt = "select userid from ftp_user";
	private String selectUserStmt; 
	private String insertUserStmt = "insert into ftp_user (userid, homedirectory, enableflag, idletime, maxloginnumber, maxloginperip, downloadrate, uploadrate ) values ('{userid}', '{homedirectory}', {enableflag}, '{idletime}', '{maxloginnumber}','{maxloginperip}', '{downloadrate}', '{uploadrate}')";
	private String updateUserStmt = "update ftp_user set homedirectory = '{homedirectory}',enableflag = {enableflag},idletime = '{idletime}', maxloginnumber = '{maxloginnumber}',maxloginperip = '{maxloginperip}',downloadrate = '{downloadrate}',uploadrate = '{uploadrate}' where userid = '{userid}' ";
	private String deleteUserStmt = "delete from ftp_user where userid = '{userid}'";
	private String authenticateStmt = "select userid as 'userpassword' from ftp_user where userid = '{userid}'";
	private String isAdminStmt = "select userid from ftp_user where userid = '{userid}' and adminpermission ";
	private String selectPermissionStmt = "select permission from ftp_user where userid = '{userid}' ";
	
	private SystemConfig systemConfig;
	
	private PasswordEncryptor passwordEncryptor;
	
	private UserManager userManager;
	
	public SfUserManager(SystemConfig systemConfig) {
		this.systemConfig = systemConfig;
		String ftpPath = systemConfig.getProperty(SystemConfig.FTP_PATH);
		this.selectUserStmt = "select userid,userid as 'userpassword',REPLACE(CONCAT('"+ftpPath+"',homedirectory),'//','/') as 'homedirectory',enableflag,idletime,'1' as 'writepermission',maxloginnumber,maxloginperip,downloadrate,uploadrate  from ftp_user where userid = '{userid}' ";
		this.passwordEncryptor = new AdPasswordEncryptor();
		this.userManager =  new AdUserManager(systemConfig.getDataSource(), selectAllStmt, selectUserStmt, insertUserStmt,
				updateUserStmt, deleteUserStmt, authenticateStmt, isAdminStmt, passwordEncryptor, null);
	}
	

	public UserManager getUserManager() {
		return userManager;
	}
	
	/**
	 * AD用户管理
	 * @author abner.li
	 * @date 2020年2月10日下午7:33:11
	 */
	class AdUserManager extends DbUserManager{

		public AdUserManager(DataSource dataSource, String selectAllStmt, String selectUserStmt, String insertUserStmt,
				String updateUserStmt, String deleteUserStmt, String authenticateStmt, String isAdminStmt,
				PasswordEncryptor passwordEncryptor, String adminName) {
			super(dataSource, selectAllStmt, selectUserStmt, insertUserStmt, updateUserStmt, deleteUserStmt, authenticateStmt,
					isAdminStmt, passwordEncryptor, adminName);
		}
		
		@Override
		public User getUserByName(String name) throws FtpException {
			User user = super.getUserByName(name);
			if (user != null) {
				return new SfBaseUser(user, selectPermissionByUser(name));
			}
			return user;
		}

		private String selectPermissionByUser(String name) {
			try (Connection connection = createConnection();Statement stmt = connection.createStatement();
					ResultSet rs = stmt.executeQuery(permissionByUserSql(name));) {
				while (rs.next()) {
					return rs.getString("permission");
				}
			} catch (Exception e) {
				logger.error("selectPermissionByUser ", e);
			}
			return null;
		}

		private String permissionByUserSql(String name) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("userid", name);
			return StringUtils.replaceString(selectPermissionStmt, map);
		}
		
	}

	/**
	 * AD账号登录校验
	 * @author 01383518
	 * @date 2020年2月3日下午10:05:34
	 */
	class AdPasswordEncryptor implements PasswordEncryptor{
		
		private final Logger logger = LoggerFactory.getLogger(AdPasswordEncryptor.class);
		
		private final static String SIT = "sit";
		private final static String DEFAULT_PWD = "123";

		@Override
		public String encrypt(String password) {
			return password;
		}

		@Override
		public boolean matches(String password, String userId) {
			if (SIT.equals(systemConfig.getProperty(SystemConfig.PROFILES))) {
				return DEFAULT_PWD.equals(password);
			}
			return adCheck(userId, password);
		}
		
		private boolean adCheck(String userId, String password) {
			DirContext dc = null;
			try {
				dc = createUserDc(userId, password);
				return true;
			} catch (Exception e) {
				logger.info("user:{} adCheck fail", userId);
				return false;
			} finally {
				if (dc != null) {
					try {
						dc.close();
					} catch (NamingException e) {
						logger.error("DirContext close fail", e);
					}
				}
			}
		}
		
		private InitialLdapContext createUserDc(String account, String password) throws Exception {
			Properties env = new Properties();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, systemConfig.getProperty(SystemConfig.AD_PREFIX) + account);
			env.put(Context.SECURITY_CREDENTIALS, password);
			env.put(Context.PROVIDER_URL, systemConfig.getProperty(SystemConfig.LDAP_URL));
			return new InitialLdapContext(env, null);
		}
		
	}

}
