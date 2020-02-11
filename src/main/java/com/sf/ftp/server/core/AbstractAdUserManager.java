package com.sf.ftp.server.core;

import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;

/**
 * 抽象AD用户管理
 * @author abner.li
 * @date 2020年2月11日上午8:59:42
 */
public abstract class AbstractAdUserManager implements UserManager{
	
    public static final String ATTR_LOGIN = "userid";
    
    public static final String ATTR_USERNAME = "username";

    public static final String ATTR_HOME = "homedirectory";

    public static final String ATTR_PERM = "permission";

    public static final String ATTR_ENABLE = "enableflag";

    public static final String ATTR_MAX_IDLE_TIME = "idletime";

    public static final String ATTR_MAX_UPLOAD_RATE = "uploadrate";

    public static final String ATTR_MAX_DOWNLOAD_RATE = "downloadrate";

    public static final String ATTR_MAX_LOGIN_NUMBER = "maxloginnumber";

    public static final String ATTR_MAX_LOGIN_PER_IP = "maxloginperip";

    private String adminName = "admin";
    
    @Override
    public String getAdminName() throws FtpException{
        return adminName;
    }
    
    @Override
    public boolean isAdmin(String login) throws FtpException {
        return adminName.equals(login);
    }
    
    /**
     * 校验ad账号密码
     * @param userId
     * @param password
     * @return
     * boolean
     */
    public abstract boolean adCheck(String userId, String password);
    

}
