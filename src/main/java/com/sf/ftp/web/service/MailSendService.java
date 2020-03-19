package com.sf.ftp.web.service;

import com.sf.ftp.web.beans.po.FtpUserEntity;

/**
 * 邮件发送服务
 * @author abner.li
 * @date 2020年3月8日下午5:23:17
 */
public interface MailSendService {

	/**
	 * 发送邮件
	 * @param title
	 * @param mailBody
	 * @param to
	 * @throws Exception
	 */
	public void sendMail(String title, String mailBody ,String[] to);

	/**
	 * 异步给用户发送密码
	 * @param ftpUserEntity
	 * void
	 */
	public void sendPwdMail(FtpUserEntity ftpUserEntity);
	


}
