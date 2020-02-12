package com.sf.ftp.server.core;

import java.io.IOException;

import org.apache.ftpserver.ftplet.DefaultFtpReply;
import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpReply;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sf.ftp.common.Permission;
import com.sf.ftp.server.bean.SfFtpRecord;
import com.sf.ftp.server.bean.SfFtpRecord.Operation;
import com.sf.ftp.server.bean.SfAdUser;
import com.sf.ftp.server.dao.FtpRecordDao;

/**
 * 记录用户操作
 * @author abner.li
 * @date 2020年1月29日下午4:29:20
 */
public class SfFtplet extends DefaultFtplet{
	
	private final static Logger logger = LoggerFactory.getLogger(SfFtplet.class);
	
	/**
	 * 访问目录指令
	 */
	private final static String PWD = "PWD";
	
	private FtpRecordDao ftpRecordDao;
	
	
	public SfFtplet(FtpRecordDao ftpRecordDao) {
		this.ftpRecordDao = ftpRecordDao;
	}

	/**
	 * 需要优雅停机
	 * 强制停止进程时，不执行
	 */
	@Override
	public void destroy() {
		ftpRecordDao.resetUserLoginStatus();
	}

	@Override
	public FtpletResult beforeCommand(FtpSession session, FtpRequest request) throws FtpException, IOException {
		String command = request.getCommand().toUpperCase();
		/**
		 * 访问目录
		 */
		if(PWD.equals(command)) {
			SfFtpRecord ftpRecord = Operation.ACCESS.builder(session, request);
			ftpRecordDao.saveRecord(ftpRecord);
		}
		return super.beforeCommand(session, request);
	}
	
	/**
	 * 登录
	 */
	@Override
	public FtpletResult onLogin(FtpSession session, FtpRequest request) throws FtpException, IOException {
		if(session.isLoggedIn()) {
			SfFtpRecord ftpRecord = Operation.LOGIN.builder(session, request);
			ftpRecordDao.saveRecord(ftpRecord);
		}
		return super.onLogin(session, request);
	}

	/**
	 * 删除
	 */
	@Override
	public FtpletResult onDeleteStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		if(!checkPermission(session,Permission.DELETE)) {
			return FtpletResult.SKIP;
		}
		SfFtpRecord ftpRecord = Operation.DELETE.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onDeleteEnd(session, request);
	}

	/**
	 * 上传
	 */
	@Override
	public FtpletResult onUploadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		if(!checkPermission(session,Permission.UPLOAD)) {
			return FtpletResult.SKIP;
		}
		SfFtpRecord ftpRecord = Operation.UPLOAD.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onUploadStart(session, request);
	}

	/**
	 * 下载
	 */
	@Override
	public FtpletResult onDownloadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		if(!checkPermission(session,Permission.DOWNLOAD)) {
			return FtpletResult.SKIP;
		}
		SfFtpRecord ftpRecord = Operation.DOWNLOAD.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onDownloadStart(session, request);
	}

	/**
	 * 删除文件夹
	 */
	@Override
	public FtpletResult onRmdirStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		if(!checkPermission(session,Permission.DELETE)) {
			return FtpletResult.SKIP;
		}
		SfFtpRecord ftpRecord = Operation.RMDIR.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onRmdirStart(session, request);
	}

	/**
	 * 新建文件夹
	 */
	@Override
	public FtpletResult onMkdirStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		if(!checkPermission(session,Permission.UPLOAD)) {
			return FtpletResult.SKIP;
		}
		SfFtpRecord ftpRecord = Operation.MKDIR.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onMkdirStart(session, request);
	}

	/**
	 * 上传(要求在此目录下的文件名是唯一的)
	 */
	@Override
	public FtpletResult onUploadUniqueStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		if(!checkPermission(session,Permission.UPLOAD)) {
			return FtpletResult.SKIP;
		}
		SfFtpRecord ftpRecord = Operation.UPLOAD.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onUploadUniqueStart(session, request);
	}
	

	/**
	 * 上传(断点续传)
	 */
	@Override
	public FtpletResult onAppendStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		if(!checkPermission(session,Permission.UPLOAD)) {
			return FtpletResult.SKIP;
		}
		SfFtpRecord ftpRecord = Operation.UPLOAD.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onUploadUniqueStart(session, request);
	}
	
	


	/**
	 * 重命名
	 */
	@Override
	public FtpletResult onRenameStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		if(!checkPermission(session,Permission.MODIFY)) {
			return FtpletResult.SKIP;
		}
		SfFtpRecord ftpRecord = Operation.RENAME.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onRenameStart(session, request);
	}

	
	private boolean checkPermission(FtpSession session, Permission permission) {
		SfAdUser user = (SfAdUser)session.getUser();
		if(!user.getPermissions().contains(permission)) {
			try {
				session.write(new DefaultFtpReply(FtpReply.REPLY_550_REQUESTED_ACTION_NOT_TAKEN, "not permission "+permission));
			} catch (Exception e) {
				logger.error("session.write() fail",e);
			}
			return false;
		}
		return true;
	}
	
}
