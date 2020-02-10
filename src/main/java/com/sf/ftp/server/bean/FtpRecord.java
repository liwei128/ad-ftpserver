package com.sf.ftp.server.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

/**
 * ftp操作记录
 * @author abner.li
 * @date 2020年1月29日下午10:48:48
 */
public class FtpRecord {
	/**
	 * 用户工号
	 */
	private String userid;
	
	/**
	 * 用户ip
	 */
	private String ip;
	
	/**
	 * 操作
	 */
	private Operation operation;
	
	/**
	 * 文件路径
	 */
	private String filePath;
	
	/**
	 * 操作类型
	 * @author 01383518
	 * @date 2020年1月30日下午8:44:00
	 */
	public enum Operation{
		/**
		 * 登录
		 */
		LOGIN("登录"){
			@Override
			public FtpRecord builder(FtpSession session, FtpRequest request) throws FtpException{
				return super.builderIgnoreArgument(session, request);
			}
		},
		/**
		 * 访问目录
		 */
		ACCESS("访问目录"){
			@Override
			public FtpRecord builder(FtpSession session, FtpRequest request) throws FtpException{
				return super.builderIgnoreArgument(session, request);
			}
		},
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
		 * 删除文件夹
		 */
		RMDIR("删除文件夹"),
		/**
		 * 新建文件夹
		 */
		MKDIR("新建文件夹"),
		/**
		 * 重命名
		 */
		RENAME("重命名");
		
		private String info;
		
		public FtpRecord builder(FtpSession session, FtpRequest request) throws FtpException {
			FtpRecord record = builderIgnoreArgument(session,request);
			record.setFilePath(record.getFilePath()+"/"+request.getArgument());
			return record;
		};
		
		protected FtpRecord builderIgnoreArgument(FtpSession session, FtpRequest request) throws FtpException {
			String user = session.getUser().toString();
			String ip = session.getClientAddress().getAddress().getHostAddress();
			String filePath = session.getUser().getHomeDirectory()+session.getFileSystemView().getWorkingDirectory().getAbsolutePath();
			return new FtpRecord(user, ip, this,filePath);
		};
		
		public String getInfo() {
			return info;
		}

		private Operation(String info) {
			this.info = info;
		}
	}


	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}


	public Operation getOperation() {
		return operation;
	}


	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath  = filePath.replaceAll("///", "/").replaceAll("//", "/");
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}

	private FtpRecord(String userid, String ip, Operation operation, String filePath) {
		super();
		this.userid = userid;
		this.ip = ip;
		this.operation = operation;
		this.filePath = filePath.replaceAll("///", "/").replaceAll("//", "/");
	}
	
	public Map<String,Object> toSqlMap(){
		Map<String, Object> map = Maps.newHashMap();
		map.put("userid", userid);
		map.put("ip", ip);
		map.put("operation", operation.getInfo());
		map.put("filepath", filePath);
		map.put("access_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		return map;
	}


	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	

}
