package com.sf.ftp.web.controller;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sf.ftp.web.beans.common.ResultData;
import com.sf.ftp.web.beans.po.FtpUserEntity;
import com.sf.ftp.web.service.FtpManageService;

/**
 * 对外提供API接口
 * @author abner.li
 * @date 2020年2月5日下午10:27:04
 */
@RestController
@RequestMapping("/api")
public class ApiController {
	
	@Resource
	private FtpManageService ftpManageService;
	
	/**
	 * 删除用户
	 * @param userid
	 * @return
	 * ResultData<Void>
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public ResultData<Void> delete(String userid){
		return ftpManageService.delete(userid);
	}
	
	
	/**
	 * 添加用户
	 * @param ftpUserEntity
	 * @return
	 * ResultData<Void>
	 */
	@ResponseBody
	@RequestMapping("/add")
	public ResultData<Void> add(FtpUserEntity ftpUserEntity){
		return ftpManageService.add(ftpUserEntity);
	}

}
