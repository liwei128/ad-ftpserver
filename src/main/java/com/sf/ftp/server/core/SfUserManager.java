package com.sf.ftp.server.core;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;

import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.DbUserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sf.ftp.server.config.SystemConfig;

/**
 * 用户管理 用户配置存储mysql ad校验用户登录
 * 
 * @author abner.li
 * @date 2020年1月21日上午11:29:49
 */
public class SfUserManager {

	private String selectAllStmt = "select userid from ftp_user";
	private String selectUserStmt; 
	private String insertUserStmt = "insert into ftp_user (userid, homedirectory, enableflag, idletime, writepermission, maxloginnumber, maxloginperip, downloadrate, uploadrate ) values ('{userid}', '{homedirectory}', {enableflag}, '{idletime}', {writepermission}, '{maxloginnumber}','{maxloginperip}', '{downloadrate}', '{uploadrate}')";
	private String updateUserStmt = "update ftp_user set homedirectory = '{homedirectory}',enableflag = {enableflag},idletime = '{idletime}', writepermission = {writepermission}, maxloginnumber = '{maxloginnumber}',maxloginperip = '{maxloginperip}',downloadrate = '{downloadrate}',uploadrate = '{uploadrate}' where userid = '{userid}' ";
	private String deleteUserStmt = "delete from ftp_user where userid = '{userid}'";
	private String authenticateStmt = "select userid as 'userpassword' from ftp_user where userid = '{userid}'";
	private String isAdminStmt = "select userid from ftp_user where userid = '{userid}' and adminpermission ";
	
	private SystemConfig systemConfig;
	
	private PasswordEncryptor passwordEncryptor;
	
	public SfUserManager(SystemConfig systemConfig) {
		this.systemConfig = systemConfig;
		String ftpPath = systemConfig.getProperty(SystemConfig.FTP_PATH);
		this.selectUserStmt = "select userid,userid as 'userpassword',REPLACE(CONCAT('"+ftpPath+"',homedirectory),'//','/') as 'homedirectory',enableflag,idletime,writepermission,maxloginnumber,maxloginperip,downloadrate,uploadrate  from ftp_user where userid = '{userid}' ";
		this.passwordEncryptor = new AdPasswordEncryptor();
	}
	

	public DbUserManager getUserManager() {
		return new DbUserManager(systemConfig.getDataSource(), selectAllStmt, selectUserStmt, insertUserStmt, updateUserStmt,
				deleteUserStmt, authenticateStmt, isAdminStmt, passwordEncryptor, null);
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
