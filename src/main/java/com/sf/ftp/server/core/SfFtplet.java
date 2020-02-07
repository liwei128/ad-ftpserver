package com.sf.ftp.server.core;

import java.io.IOException;

import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;

import com.sf.ftp.server.bean.FtpRecord;
import com.sf.ftp.server.bean.FtpRecord.Operation;
import com.sf.ftp.server.dao.FtpRecordDao;

/**
 * 记录用户操作
 * @author abner.li
 * @date 2020年1月29日下午4:29:20
 */
public class SfFtplet extends DefaultFtplet{
	
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
			FtpRecord ftpRecord = Operation.ACCESS.builder(session, request);
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
			FtpRecord ftpRecord = Operation.LOGIN.builder(session, request);
			ftpRecordDao.saveRecord(ftpRecord);
		}
		return super.onLogin(session, request);
	}

	/**
	 * 删除
	 */
	@Override
	public FtpletResult onDeleteStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		FtpRecord ftpRecord = Operation.DELETE.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onDeleteEnd(session, request);
	}

	/**
	 * 上传
	 */
	@Override
	public FtpletResult onUploadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		FtpRecord ftpRecord = Operation.UPLOAD.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onUploadStart(session, request);
	}

	/**
	 * 下载
	 */
	@Override
	public FtpletResult onDownloadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		FtpRecord ftpRecord = Operation.DOWNLOAD.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onUploadStart(session, request);
	}

	/**
	 * 删除文件夹
	 */
	@Override
	public FtpletResult onRmdirStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		FtpRecord ftpRecord = Operation.RMDIR.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onUploadStart(session, request);
	}

	/**
	 * 新建文件夹
	 */
	@Override
	public FtpletResult onMkdirStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		FtpRecord ftpRecord = Operation.MKDIR.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onUploadStart(session, request);
	}

	/**
	 * 上传(要求在此目录下的文件名是唯一的)
	 */
	@Override
	public FtpletResult onUploadUniqueStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		FtpRecord ftpRecord = Operation.UPLOAD.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onUploadStart(session, request);
	}


	/**
	 * 重命名
	 */
	@Override
	public FtpletResult onRenameStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
		FtpRecord ftpRecord = Operation.RENAME.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onUploadStart(session, request);
	}

	/**
	 * 执行操作
	 */
	@Override
	public FtpletResult onSite(FtpSession session, FtpRequest request) throws FtpException, IOException {
		FtpRecord ftpRecord = Operation.SITE.builder(session, request);
		ftpRecordDao.saveRecord(ftpRecord);
		return super.onUploadStart(session, request);
	}
	
}
