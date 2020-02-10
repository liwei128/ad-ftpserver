package com.sf.ftp.server.bean;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.impl.BaseUser;

import com.google.common.collect.Sets;

/**
 * 对用户权限进行扩展
 * @author abner.li
 * @date 2020年2月10日下午5:43:01
 */
public class SfBaseUser extends BaseUser {
		
	private Set<Permission> permissions = Sets.newHashSet();
	
	/**
	 * 访问权限
	 * @author abner.li
	 * @date 2020年2月10日下午7:56:39
	 */
	public enum Permission{
	
		/**
		 * 删除
		 */
		DELETE("删除"),
		/**
		 * 上传
		 */
		UPLOAD("上传"),
		/**
		 * 下载
		 */
		DOWNLOAD("下载"),
		/**
		 * 修改
		 */
		MODIFY("修改");
		
		private String info;

		private Permission(String info) {
			this.info = info;
		}

		public String getInfo() {
			return info;
		}
		
		public static Permission getByInfo(String info) {
			for(Permission permission:values()) {
				if(permission.getInfo().equals(info)) {
					return permission;
				}
			}
			return null;
		}
		
	}
	
	public Set<Permission> getPermissions() {
		return Sets.newHashSet(permissions);
	}

	public SfBaseUser(User user,String permissionsStr) {
		super(user);
		if(StringUtils.isNotEmpty(permissionsStr)) {
			String[] split = permissionsStr.split(",");
			for(String permission:split) {
				permissions.add(Permission.getByInfo(permission));
			}
		}
	}

}
