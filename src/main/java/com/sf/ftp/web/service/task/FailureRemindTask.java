package com.sf.ftp.web.service.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sf.ftp.web.beans.po.FtpUserEntity;
import com.sf.ftp.web.dao.FtpUserEntityMapper;
import com.sf.ftp.web.service.MailSendService;

/**
 * 用户权限过期提醒
 * @author abner.li
 * @date 2020年3月8日下午6:16:52
 */
@Service
public class FailureRemindTask {
	
	private final static Logger logger = LoggerFactory.getLogger(FailureRemindTask.class);

	
	@Autowired
	private FtpUserEntityMapper ftpUserEntityMapper;
	
	@Autowired
	private MailSendService mailSendService;
	
	/**
	 * 过期账户提醒
	 * 7天内
	 * void
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void expiresRemind() {
		try {
			List<FtpUserEntity> users = ftpUserEntityMapper.queryExpiresUserByDate(formatTimeByDay(7));
			if(users==null||users.size()==0) {
				return ;
			}
			users.forEach(user->{
				String title = "ftp服务账号即将过期";
				StringBuilder body = new StringBuilder();
				body.append("账号:").append(user.getUserid())
				.append("<br/>目录:").append(user.getHomedirectory())
				.append("<br/>您申请的ftp权限将于").append(user.getExpires()).append("到期，")
				.append("如需继续使用请前往http://itrms.sf-express.com/IT019.html");
				String[] to = new String[]{user.getEmail()};
				mailSendService.sendMail(title, body.toString(), to);
			});
		}catch (Exception e) {
			logger.error("expiresRemind fail",e);
		}
	}
	
	private String formatTimeByDay(int day){
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, day);
			return dateFormat.format(calendar.getTime());
		}catch(Exception e){
			return "";
		}
	}

}
