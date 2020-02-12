package com.sf.ftp.server.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.sql.DataSource;

import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.AnonymousAuthentication;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginRequest;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.TransferRateRequest;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.apache.ftpserver.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sf.ftp.server.bean.SfAdUser;
import com.sf.ftp.server.config.SystemConfig;

/**
 * 基于AD的用户管理
 * @author abner.li
 * @date 2020年2月11日上午8:48:47
 */
public class SfAdUserManager extends AbstractAdUserManager{

	private final Logger logger = LoggerFactory.getLogger(SfAdUserManager.class);
	
	private final static String SIT = "sit";
	
	private final static String DEFAULT_PWD = "123";
	
	private final static String ANONYMOUS = "anonymous";

    private String insertUserStmt = "insert into ftp_user (userid,username,permission, homedirectory, enableflag, idletime, maxloginnumber, maxloginperip, downloadrate, uploadrate ) values ('{userid}','{username}','{permission}', '{homedirectory}', {enableflag}, '{idletime}', '{maxloginnumber}','{maxloginperip}', '{downloadrate}', '{uploadrate}')";

    private String updateUserStmt = "update ftp_user set username = '{username}',permission = '{permission}',homedirectory = '{homedirectory}',enableflag = {enableflag},idletime = '{idletime}', maxloginnumber = '{maxloginnumber}',maxloginperip = '{maxloginperip}',downloadrate = '{downloadrate}',uploadrate = '{uploadrate}' where userid = '{userid}' ";;

    private String deleteUserStmt = "delete from ftp_user where userid = '{userid}'";

    private String selectAllStmt = "select userid from ftp_user";
    
    private String selectUserStmt;

    private DataSource dataSource;
    
    private SystemConfig systemConfig;
    

	public SfAdUserManager(SystemConfig systemConfig) {
		this.dataSource = systemConfig.getDataSource();
		this.systemConfig = systemConfig;
		String ftpPath = systemConfig.getProperty(SystemConfig.FTP_PATH);
		this.selectUserStmt = "select userid,username,permission,REPLACE(CONCAT('"+ftpPath+"',homedirectory),'//','/') as 'homedirectory',enableflag,idletime,maxloginnumber,maxloginperip,downloadrate,uploadrate  from ftp_user where userid = '{userid}' ";
	}

	@Override
	public User getUserByName(String username) {
		try (Connection connection = createConnection();
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(createSelectUserByNameSql(username));) {
	 		if (rs.next()) {
                SfAdUser user = new SfAdUser();
                user.setName(rs.getString(ATTR_LOGIN));
                user.setUser(rs.getString(ATTR_USERNAME));
                user.setHomeDirectory(rs.getString(ATTR_HOME));
                user.setEnabled(rs.getBoolean(ATTR_ENABLE));
                user.setMaxIdleTime(rs.getInt(ATTR_MAX_IDLE_TIME));

                List<Authority> authorities = new ArrayList<Authority>();
                authorities.add(new WritePermission());
                authorities.add(new ConcurrentLoginPermission(rs.getInt(ATTR_MAX_LOGIN_NUMBER), rs.getInt(ATTR_MAX_LOGIN_PER_IP)));
                authorities.add(new TransferRatePermission(rs.getInt(ATTR_MAX_DOWNLOAD_RATE), rs.getInt(ATTR_MAX_UPLOAD_RATE)));
                user.setAuthorities(authorities);
                user.setPermissionsString(rs.getString(ATTR_PERM));
                return user;
            }
		} catch (Exception e) {
			logger.error("getUserByName,{}  ",username,e);
		}
		return null;
	}

	private String createSelectUserByNameSql(String userid) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(ATTR_LOGIN, escapeString(userid));
        return StringUtils.replaceString(selectUserStmt, map);
	}


	@Override
	public String[] getAllUserNames() throws FtpException {
		List<String> names = Lists.newArrayList();
		try (Connection connection = createConnection();
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(selectAllStmt);) {
			
			while (rs.next()) {
                names.add(rs.getString(ATTR_LOGIN));
            }
            
		} catch (Exception e) {
			logger.error("getAllUserNames ",e);
		}
		return names.toArray(new String[0]);
	}

	@Override
	public void delete(String userid) throws FtpException {
		try (Connection connection = createConnection();
				Statement stmt = connection.createStatement();) {
			
			HashMap<String, Object> map = Maps.newHashMap();
	        map.put(ATTR_LOGIN, escapeString(userid));
	        String sql = StringUtils.replaceString(deleteUserStmt, map);
	        stmt.executeUpdate(sql);
		} catch (Exception e) {
			logger.error("delete ",e);
		}
	}

	@Override
	public void save(User user) throws FtpException {

		try (Connection connection = createConnection();
				Statement stmt = connection.createStatement();) {
			HashMap<String, Object> map = Maps.newHashMap();
	        map.put(ATTR_LOGIN, escapeString(user.getName()));
	        String home = user.getHomeDirectory() == null ? "/":user.getHomeDirectory();
            map.put(ATTR_HOME, escapeString(home));
            map.put(ATTR_ENABLE, String.valueOf(user.getEnabled()));
            map.put(ATTR_MAX_IDLE_TIME, user.getMaxIdleTime());
            
            TransferRateRequest transferRateRequest = new TransferRateRequest();
            transferRateRequest = (TransferRateRequest) user.authorize(transferRateRequest);
            if (transferRateRequest != null) {
                map.put(ATTR_MAX_UPLOAD_RATE, transferRateRequest.getMaxUploadRate());
                map.put(ATTR_MAX_DOWNLOAD_RATE, transferRateRequest.getMaxDownloadRate());
            } else {
                map.put(ATTR_MAX_UPLOAD_RATE, 0);
                map.put(ATTR_MAX_DOWNLOAD_RATE, 0);
            }
            ConcurrentLoginRequest concurrentLoginRequest = new ConcurrentLoginRequest(0, 0);
            concurrentLoginRequest = (ConcurrentLoginRequest) user.authorize(concurrentLoginRequest);
            if (concurrentLoginRequest != null) {
                map.put(ATTR_MAX_LOGIN_NUMBER, concurrentLoginRequest.getMaxConcurrentLogins());
                map.put(ATTR_MAX_LOGIN_PER_IP, concurrentLoginRequest.getMaxConcurrentLoginsPerIP());
            } else {
                map.put(ATTR_MAX_LOGIN_NUMBER, 0);
                map.put(ATTR_MAX_LOGIN_PER_IP, 0);
            }
            if(user instanceof SfAdUser) {
            	SfAdUser sfAdUser = (SfAdUser) user;
            	map.put(ATTR_USERNAME, sfAdUser.getUser());
            	map.put(ATTR_PERM, sfAdUser.getPermissionsString());
            }
            String sql = null;
            if (!doesExist(user.getName())) {
                sql = StringUtils.replaceString(insertUserStmt, map);
            } else {
                sql = StringUtils.replaceString(updateUserStmt, map);
            }
	        stmt.executeUpdate(sql);
		} catch (Exception e) {
			logger.error("save ",e);
		}
		
	}

	@Override
	public boolean doesExist(String userid) {
		try (Connection connection = createConnection();
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(createSelectUserByNameSql(userid));) {
			return rs.next();
		} catch (Exception e) {
			logger.error("doesExist,{}  ",userid,e);
		}
		return false;
	}

	@Override
	public User authenticate(Authentication authentication) throws AuthenticationFailedException {
		if (authentication instanceof UsernamePasswordAuthentication) {
            UsernamePasswordAuthentication upauth = (UsernamePasswordAuthentication) authentication;
            String user = upauth.getUsername();
            String password = upauth.getPassword();
            if (user == null) {
                throw new AuthenticationFailedException("Authentication failed");
            }
            User adUser = getUserByName(user);
            if(adUser==null||!adCheck(user,password)) {
            	throw new AuthenticationFailedException("Authentication failed");
            }
            return adUser;
        } else if (authentication instanceof AnonymousAuthentication) {
        	if (doesExist(ANONYMOUS)) {
                return getUserByName(ANONYMOUS);
        	} 
        	throw new AuthenticationFailedException("Authentication failed");
        } else {
            throw new IllegalArgumentException(
                    "Authentication not supported by this user manager");
        }
	}
	
	private Connection createConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(true);
        return connection;
    }

	@Override
	public boolean adCheck(String userId, String password) {
		
		if (SIT.equals(systemConfig.getProperty(SystemConfig.PROFILES))) {
			return DEFAULT_PWD.equals(password);
		}
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
	
	private String escapeString(String input) {
        if (input == null) {
            return input;
        }
        StringBuilder valBuf = new StringBuilder(input);
        for (int i = 0; i < valBuf.length(); i++) {
            char ch = valBuf.charAt(i);
            if (ch == '\'' || ch == '\\' || ch == '$' || ch == '^' || ch == '['
                    || ch == ']' || ch == '{' || ch == '}') {

                valBuf.insert(i, '\\');
                i++;
            }
        }
        return valBuf.toString();
    }

}
