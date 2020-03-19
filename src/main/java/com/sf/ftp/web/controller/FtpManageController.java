package com.sf.ftp.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sf.ftp.web.beans.UserConstant;
import com.sf.ftp.web.beans.common.PagingData;
import com.sf.ftp.web.beans.common.ResultData;
import com.sf.ftp.web.beans.dto.LogCondition;
import com.sf.ftp.web.beans.dto.UserCondition;
import com.sf.ftp.web.beans.po.FtpAccessLogEntity;
import com.sf.ftp.web.beans.po.FtpStatisticsEntity;
import com.sf.ftp.web.beans.po.FtpUserEntity;
import com.sf.ftp.web.service.FtpManageService;
import com.sf.ftp.web.utils.ControllerUtil;

/**
 * ftp管理相关服务
 * @author abner.li
 * @date 2020年2月4日下午12:49:08
 */
@Controller
public class FtpManageController {
	
	@Resource
	private FtpManageService ftpManageService;
	
	/**
	 * 主页
	 * @param modelMap
	 * @return
	 * String
	 */
	@RequestMapping("/")
	public String index(ModelMap modelMap) {
		HttpSession session = ControllerUtil.getRequest().getSession();
		Object user = session.getAttribute("user");
		if (user == null) {
			return "login";
		}
		modelMap.put("user", user);
		Object path = session.getAttribute("view");
		return path==null?"user":path.toString();
	}
	
	/**
	 * 切换页面
	 * @param view
	 * @return
	 * ResultData<Void>
	 */
	@RequestMapping("/switcherView")
	@ResponseBody
	public ResultData<Void> switcherView(String view) {
		ControllerUtil.getRequest().getSession().setAttribute("view", view);;
		return ResultData.getInstance();
	}
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 * ResultData<Void>
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public ResultData<Void> doLogin(String username,String password) {
		return ftpManageService.doLogin(username,password);
	}
	
	/**
	 * 退出
	 * @return
	 * ResultData<Void>
	 */
	@RequestMapping("/exit")
	@ResponseBody
	public ResultData<Void> exit() {
		ControllerUtil.getRequest().getSession().removeAttribute(UserConstant.USER);
		return ResultData.getInstance();
	}
	
	/**
	 * 用户列表查询
	 * @param condition
	 * @return
	 * PagingData<FtpUserEntity>
	 */
	@RequestMapping("/user/list")
	@ResponseBody
	public PagingData<FtpUserEntity> list(UserCondition condition){
		return ftpManageService.userList(condition);
	}
	
	/**
	 * 删除用户
	 * @param userid
	 * @return
	 * ResultData<Void>
	 */
	@ResponseBody
	@RequestMapping("/user/delete")
	public ResultData<Void> delete(String userid){
		return ftpManageService.delete(userid);
	}
	
	/**
	 * 重置密码
	 * @param userid
	 * @return
	 * ResultData<Void>
	 */
	@ResponseBody
	@RequestMapping("/user/retPwd")
	public ResultData<Void> retPwd(String userid){
		return ftpManageService.retPwd(userid);
	}
	
	/**
	 * 修改用户
	 * @param ftpUserEntity
	 * @return
	 * ResultData<Void>
	 */
	@ResponseBody
	@RequestMapping("/user/doModify")
	public ResultData<Void> doModify(FtpUserEntity ftpUserEntity){
		ftpUserEntity.setHandler(ControllerUtil.getUser().getUserid());
		return ftpManageService.modify(ftpUserEntity);
	}
	
	/**
	 * 用户修改页面
	 * @param userid
	 * @param modelMap
	 * @return
	 * String
	 */
	@RequestMapping("/user/modify")
    public String modify(String userid, ModelMap modelMap) {
		FtpUserEntity ftpUserEntity = ftpManageService.selectByUserId(userid);
		modelMap.addAttribute("item",ftpUserEntity);
        return "user_edit";
    }
	
	/**
	 * 添加用户
	 * @param ftpUserEntity
	 * @return
	 * ResultData<Void>
	 */
	@ResponseBody
	@RequestMapping("/user/doAdd")
	public ResultData<Void> doAdd(FtpUserEntity ftpUserEntity){
		ftpUserEntity.setHandler(ControllerUtil.getUser().getUserid());
		return ftpManageService.add(ftpUserEntity);
	}
	
	/**
	 * 添加用户页面
	 * @return
	 * String
	 */
	@RequestMapping("/user/add")
    public String add() {
        return "user_add";
    }
	
	/**
	 * 系统统计信息
	 * @return
	 * ResultData<FtpStatisticsEntity>
	 */
	@RequestMapping("/statistics")
	@ResponseBody
	public ResultData<FtpStatisticsEntity> statistics(){
		return ftpManageService.statistics();
	}
	
	/**
	 * 访问记录列表
	 * @param condition
	 * @return
	 * PagingData<FtpAccessLogEntity>
	 */
	@RequestMapping("/log/list")
	@ResponseBody
	public PagingData<FtpAccessLogEntity> list(LogCondition condition){
		return ftpManageService.logList(condition);
	}
	
    /**
	 * 
	 * 跳转到导入excel文件页面
	 */
    @RequestMapping("/user/importView")
    public String importView(ModelMap modelMap){
    	modelMap.addAttribute("actionURL","/user/import");
    	modelMap.addAttribute("hrefURL","/static/userModel.xlsx");
    	return "importView";
    }
    
    /**
     * 导入用户
     * @param multiReq
     * @return
     * String
     */
    @RequestMapping("/user/import")
    @ResponseBody
    public String importUser(MultipartHttpServletRequest multiReq){
    	return ftpManageService.importExcel(multiReq.getFile("file1"));
    }
    
    /**
     * 导出用户
     * @param condition
     * @throws Exception
     * void
     */
    @RequestMapping("/user/export")
    @ResponseBody
    public void exportUser(UserCondition condition) throws Exception{
    	ftpManageService.exportUser(condition);
    }
    
    /**
     * 导出用户访问记录
     * @param condition
     * @throws Exception
     * void
     */
    @RequestMapping("/log/export")
    @ResponseBody
    public void exportLog(LogCondition condition) throws Exception{
    	ftpManageService.exportLog(condition);
    }

}
