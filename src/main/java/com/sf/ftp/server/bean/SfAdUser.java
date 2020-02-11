package com.sf.ftp.server.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.ftplet.User;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.sf.ftp.common.Permission;


/**
 * 用户实体类
 * 对用户权限进行扩展
 * @author abner.li
 * @date 2020年2月10日下午5:43:01
 */
public class SfAdUser implements User {
	
    private String name;
    
    private String user;

    private int maxIdleTime;

    private String homeDirectory;

    private boolean enabled;

    private List<? extends Authority> authorities = new ArrayList<Authority>();
		
	private Set<Permission> permissions = Sets.newHashSet();
	
	
	public Set<Permission> getPermissions() {
		return Collections.unmodifiableSet(permissions);
	}
	public void setPermissions(Set<Permission> permissions) {
		if (permissions != null) {
            this.permissions = Collections.unmodifiableSet(permissions);
        }
	}
	
	public String getPermissionsString() {
		Set<String> collect = permissions.stream().map(p->{
			return p.getInfo();
		}).collect(Collectors.toSet());
		return Joiner.on(",").join(collect);
	}
	
	public void setPermissionsString(String permissionsStr) {
		Set<Permission> newPermission = Sets.newHashSet();
		if(StringUtils.isNotEmpty(permissionsStr)) {
			String[] split = permissionsStr.split(",");
			for(String permission:split) {
				newPermission.add(Permission.getByInfo(permission));
			}
		}
		this.permissions = Collections.unmodifiableSet(newPermission);
	}

	@Override
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public int getMaxIdleTime() {
		return maxIdleTime;
	}


	public void setMaxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
        if (this.maxIdleTime < 0) {
        	this.maxIdleTime = 0;
        }
	}

	@Override
	public String getHomeDirectory() {
		return homeDirectory;
	}


	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}

	@Override
	public boolean getEnabled() {
		return enabled;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public List<Authority> getAuthorities() {
		if (authorities != null) {
            return Collections.unmodifiableList(authorities);
        } 
        return null;
	}


	public void setAuthorities(List<? extends Authority> authorities) {
		if (authorities != null) {
            this.authorities = Collections.unmodifiableList(authorities);
        }
	}


	@Override
	public List<Authority> getAuthorities(Class<? extends Authority> clazz) {
		 List<Authority> selected = new ArrayList<Authority>();
        for (Authority authority : authorities) {
            if (authority.getClass().equals(clazz)) {
                selected.add(authority);
            }
        }

	    return selected;
	}

	@Override
	public AuthorizationRequest authorize(AuthorizationRequest request) {
		if(authorities == null) {
            return null;
        }
        boolean someoneCouldAuthorize = false;
        for (Authority authority : authorities) {
            if (authority.canAuthorize(request)) {
                someoneCouldAuthorize = true;
                request = authority.authorize(request);
                if (request == null) {
                    return null;
                }
            }

        }
        if (someoneCouldAuthorize) {
            return request;
        } else {
            return null;
        }
	}


}
