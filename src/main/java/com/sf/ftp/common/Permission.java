package com.sf.ftp.common;


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
