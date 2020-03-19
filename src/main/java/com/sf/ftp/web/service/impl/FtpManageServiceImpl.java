package com.sf.ftp.web.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ftpserver.ftplet.FtpStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.sf.ftp.common.FtpServerApi;
import com.sf.ftp.common.Permission;
import com.sf.ftp.common.UserType;
import com.sf.ftp.web.beans.UserConstant;
import com.sf.ftp.web.beans.common.BaseRetCode;
import com.sf.ftp.web.beans.common.PagingData;
import com.sf.ftp.web.beans.common.ResultData;
import com.sf.ftp.web.beans.dto.LogCondition;
import com.sf.ftp.web.beans.dto.UserCondition;
import com.sf.ftp.web.beans.po.FtpAccessLogEntity;
import com.sf.ftp.web.beans.po.FtpStatisticsEntity;
import com.sf.ftp.web.beans.po.FtpUserEntity;
import com.sf.ftp.web.dao.FtpAccessLogEntityMapper;
import com.sf.ftp.web.dao.FtpStatisticsEntityMapper;
import com.sf.ftp.web.dao.FtpUserEntityMapper;
import com.sf.ftp.web.service.AdService;
import com.sf.ftp.web.service.FtpManageService;
import com.sf.ftp.web.service.MailSendService;
import com.sf.ftp.web.utils.ControllerUtil;
import com.sf.ftp.web.utils.ExcelUtils;
/**
 * ftp管理相关服务
 * @author abner.li
 * @date 2020年2月4日下午12:53:27
 */
@Service
public class FtpManageServiceImpl implements FtpManageService{
	
	private final static Logger logger = LoggerFactory.getLogger(FtpManageServiceImpl.class);
	
	private final static List<Character> SPECIAL_CHARS=Lists.newArrayList('\'','\\',':','*','?','"','<','>','|','$','^','[',']','{','}');

	@Autowired
	private AdService adService;
	
	@Autowired
	private MailSendService mailSendService;

	@Autowired
	private FtpUserEntityMapper ftpUserEntityMapper;
	
	@Autowired
	private FtpAccessLogEntityMapper ftpAccessLogEntityMapper;
	
	@Autowired
	private FtpStatisticsEntityMapper ftpStatisticsEntityMapper;
	
	
	private FtpServerApi ftpServerApi = loadFtpServerApi();

	/**
	 * 加载ftp接口
	 * @return
	 * FtpServerApi
	 */
	private FtpServerApi loadFtpServerApi() {
		try {
			ServiceLoader<FtpServerApi> load = ServiceLoader.load(FtpServerApi.class);
			Iterator<FtpServerApi> iterator = load.iterator();
			while (iterator.hasNext()) {
				return iterator.next();
			}
		} catch (Throwable e) {
		}
		logger.warn("loadFtpServerApi fail,default get data from the database");
		return null;
	}

	@Override
	public ResultData<FtpStatisticsEntity> statistics() {
		if (ftpServerApi != null) {
			FtpStatistics ftpStatistics = ftpServerApi.getFtpStatistics();
			if (ftpStatistics != null) {
				return ResultData.getInstance(new FtpStatisticsEntity(ftpStatistics));
			}
		}
		return ResultData.getInstance(ftpStatisticsEntityMapper.query());
	}

	@Override
	public PagingData<FtpAccessLogEntity> logList(LogCondition condition) {
		int count  = ftpAccessLogEntityMapper.queryCountByCondition(condition);
		List<FtpAccessLogEntity> list = ftpAccessLogEntityMapper.queryByCondition(condition);
		PagingData<FtpAccessLogEntity> pagingData = PagingData.getInstance(condition.getLimit(),count,list);
		return pagingData;
	}

	@Override
	public ResultData<Void> doLogin(String username, String password) {
		if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)) {
			return ResultData.getInstance(BaseRetCode.EMPTY_USER);
		}
		FtpUserEntity user = ftpUserEntityMapper.selectPwdByUserId(username);
		if (user == null || !user.getAdminpermission() || !user.getEnableflag()) {
			return ResultData.getInstance(BaseRetCode.NOT_PERMISSION);
		}
		if(user.getUsertype()==UserType.AD&&!adService.checkUser(username,password)) {
			return ResultData.getInstance(BaseRetCode.PWD_ERR);
		}
		if(user.getUsertype()==UserType.USER&&!password.equals(user.getPassword())) {
			return ResultData.getInstance(BaseRetCode.PWD_ERR);
		}
		user.setPassword(null);
		ControllerUtil.getRequest().getSession().setAttribute(UserConstant.USER, user);
		return ResultData.getInstance();
	}
	
	@Override
	public PagingData<FtpUserEntity> userList(UserCondition condition) {
		int count  = ftpUserEntityMapper.queryCountByCondition(condition);
		List<FtpUserEntity> list = ftpUserEntityMapper.queryByCondition(condition);
		PagingData<FtpUserEntity> pagingData = PagingData.getInstance(condition.getLimit(),count,list);
		return pagingData;
	}


	@Override
	public ResultData<Void> delete(String userid) {
		int count = ftpUserEntityMapper.deleteByUserId(userid);
		return count > 0 ? ResultData.getInstance() : ResultData.getInstance(BaseRetCode.FAIL);
	}

	@Override
	public ResultData<Void> modify(FtpUserEntity ftpUserEntity) {
		ResultData<Void> res = checkFtpUserEntity(ftpUserEntity);
		if(!res.success()) {
			return res;
		}
		ftpUserEntity.setPassword(null);
		int count = ftpUserEntityMapper.updateByUserId(ftpUserEntity);
		return count > 0 ? ResultData.getInstance() : ResultData.getInstance(BaseRetCode.FAIL);
	}

	private ResultData<Void> checkFtpUserEntity(FtpUserEntity ftpUserEntity) {
		if (StringUtils.isEmpty(ftpUserEntity.getUserid()) || StringUtils.isEmpty(ftpUserEntity.getHomedirectory())
				|| StringUtils.isEmpty(ftpUserEntity.getEmail())||ftpUserEntity.getUsertype()==null) {
			return ResultData.getInstance(BaseRetCode.PARAM_MISSING);
		}
		checkHomedirectory(ftpUserEntity);
		ftpUserEntity.setCurrentLoginNumber(null);
		if(!checkPermission(ftpUserEntity)) {
			return ResultData.getInstance(BaseRetCode.PERMISSION_FAILD);
		}
		return ResultData.getInstance();
	}

	@Override
	public ResultData<Void> add(FtpUserEntity ftpUserEntity) {
		ResultData<Void> res = checkFtpUserEntity(ftpUserEntity);
		if(!res.success()) {
			return res;
		}
		if(ftpUserEntity.getUsertype()==UserType.AD&&!adService.isAdUser(ftpUserEntity.getUserid()).success()) {
			return ResultData.getInstance(BaseRetCode.NOT_AD_ACCOUNT);
		}
		if(ftpUserEntityMapper.selectByUserId(ftpUserEntity.getUserid())!=null) {
			ftpUserEntityMapper.updateByUserId(ftpUserEntity);
			return ResultData.getInstance();
		}
		//设置随机密码
		if(ftpUserEntity.getUsertype()==UserType.USER) {
			String password = RandomStringUtils.randomAlphanumeric(10);
			ftpUserEntity.setPassword(password);
		}else {
			ftpUserEntity.setPassword(null);
		}
		ftpUserEntityMapper.insert(ftpUserEntity);
		if(ftpUserEntity.getUsertype()==UserType.USER) {
			mailSendService.sendPwdMail(ftpUserEntity);
		}
		return ResultData.getInstance();
		
	}

	private boolean checkPermission(FtpUserEntity ftpUserEntity) {
		String permissions = ftpUserEntity.getPermission();
		if(StringUtils.isEmpty(permissions)) {
			return true;
		}
		String[] permissionList = permissions.split(",");
		for(String permission:permissionList) {
			if(Permission.getByInfo(permission)==null) {
				return false;
			};
		}
		return true;
	}

	private void checkHomedirectory(FtpUserEntity ftpUserEntity) {
		String homedirectory = ftpUserEntity.getHomedirectory();
		StringBuilder text = new StringBuilder();
		char[] charArray = homedirectory.toCharArray();
		for(char a:charArray){
			if(!SPECIAL_CHARS.contains(a)){
				text.append(a);
			}
		}
		ftpUserEntity.setHomedirectory(text.toString());
	}

	@Override
	public FtpUserEntity selectByUserId(String userid) {
		return ftpUserEntityMapper.selectByUserId(userid);
	}

	@Override
	public String importExcel(MultipartFile file) {
		return ControllerUtil.importExcel(file,FtpUserEntity.class,user->{
			user.setHandler(ControllerUtil.getUser().getUserid());
			return add(user).success();
		});
	}

	@Override
	public void exportUser(UserCondition condition) throws Exception {
		List<FtpUserEntity> list = ftpUserEntityMapper.queryAllByCondition(condition);
		ExcelUtils.exportExcel(list,"FTP用户列表","sheet",FtpUserEntity.class,"FTP用户列表",ControllerUtil.getResponse());
	}
	
	@Override
	public void exportLog(LogCondition condition) throws Exception {
		List<FtpAccessLogEntity> list = ftpAccessLogEntityMapper.queryAllByCondition(condition);
		ExcelUtils.exportExcel(list,"用户访问记录","sheet",FtpAccessLogEntity.class,"用户访问记录",ControllerUtil.getResponse());
	}

	@Override
	public ResultData<Void> retPwd(String userid) {
		FtpUserEntity user = ftpUserEntityMapper.selectByUserId(userid);
		if(user==null||user.getUsertype()!=UserType.USER) {
			return ResultData.getInstance(BaseRetCode.FAIL);
		}
		String password = RandomStringUtils.randomAlphanumeric(10);
		user.setPassword(password);
		user.setHandler(ControllerUtil.getUser().getUserid());
		ftpUserEntityMapper.updateByUserId(user);
		mailSendService.sendPwdMail(user);
		return ResultData.getInstance();
	}

}
