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
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.apache.ftpserver.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sf.ftp.common.UserType;
import com.sf.ftp.server.bean.SfAdUser;
import com.sf.ftp.server.config.SystemConfig;

/**
 * 基于AD的用户管理
 * @author abner.li
 * @date 2020年2月11日上午8:48:47
 */
public class SfAdUserManager extends AbstractAdUserManager{

	private final Logger logger = LoggerFactory.getLogger(SfAdUserManager.class);
	
	private final static String ANONYMOUS = "anonymous";

    private String deleteUserStmt = "delete from ftp_user where userid = '{userid}'";

    private String selectAllStmt = "select userid from ftp_user";
    
    private String selectUserStmt;

    private DataSource dataSource;
    
    private SystemConfig systemConfig;
    

	public SfAdUserManager(SystemConfig systemConfig) {
		this.dataSource = systemConfig.getDataSource();
		this.systemConfig = systemConfig;
		String ftpPath = systemConfig.getProperty(SystemConfig.FTP_PATH);
		this.selectUserStmt = "select userid,usertype,password,permission,REPLACE(CONCAT('"+ftpPath+"',homedirectory),'//','/') as 'homedirectory',enableflag,idletime,maxloginnumber,maxloginperip,downloadrate,uploadrate  from ftp_user where userid = '{userid}' ";
	}

	@Override
	public User getUserByName(String username) {
		try (Connection connection = createConnection();
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(createSelectUserByNameSql(username));) {
	 		if (rs.next()) {
                SfAdUser user = new SfAdUser();
                user.setName(rs.getString(ATTR_LOGIN));
                user.setUsertype(UserType.valueOf(rs.getString(ATTR_USER_TYPE)));
                user.setPassword(rs.getString(ATTR_PWD));
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
		throw new FtpException("not impl saveUser");
		
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
            if(adUser==null||!pwdCheck(adUser,password)) {
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
	
	private boolean pwdCheck(User user, String password) {
		if(user instanceof SfAdUser) {
			SfAdUser adUser = (SfAdUser) user;
			if(adUser.getUsertype()==UserType.AD) {
				return adCheck(adUser.getName(),password);
			}
			return adUser.getPassword().equals(password);
		}
		return false;
	}

	private Connection createConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(true);
        return connection;
    }

	@Override
	public boolean adCheck(String userId, String password) {
		
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
