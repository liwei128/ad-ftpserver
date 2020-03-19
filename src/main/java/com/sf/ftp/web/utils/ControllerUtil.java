package com.sf.ftp.web.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.sf.ftp.web.beans.UserConstant;
import com.sf.ftp.web.beans.po.FtpUserEntity;

/**
 * Controller工具
 * @author abner.li
 * @date 2020年2月4日上午11:18:34
 */
public class ControllerUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(ControllerUtil.class);
		
	public static String getDomainName() {
		return getRequest().getServerName();
	}
	
	public static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) requestAttributes).getRequest();
		}
		return null;
	}
	
	public static HttpServletResponse getResponse() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) requestAttributes).getResponse();
		}
		return null;
	}
	

	
	public static void setResponseData(Object data,HttpStatus status) throws IOException{
		HttpServletResponse response = getResponse();
		response.setStatus(status.value());
		response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.append(JSON.toJSONString(data));
        
	}
	
	public static void setResponseData(Object data) throws IOException{
		setResponseData(data, HttpStatus.OK);
	}

	public static <T> String importExcel(MultipartFile file, Class<T> pojoClass,Predicate<? super T> action) {
		AtomicInteger atomicInteger = new AtomicInteger(0);
		List<T> lists = Lists.newArrayList();
		try{
			lists = ExcelUtils.importExcel(file,0,1, pojoClass);
			lists.forEach(u->{
	        	if(!action.test(u)) {
	        		return;
	        	}
	        	atomicInteger.incrementAndGet();
	        });
        }catch(Exception e){
        	logger.error("importExcel fail ",e);
        }
		String res = "";
		if(atomicInteger.get()>0) {
			res += "导入成功："+atomicInteger.get();
		}
		if(lists.size()>atomicInteger.get()) {
			res+="    导入失败："+(lists.size()-atomicInteger.get());
		}
        return res;
	}

	public static FtpUserEntity getUser() {
		Object user = getRequest().getSession().getAttribute(UserConstant.USER);
		return (FtpUserEntity) user;
	}


}
