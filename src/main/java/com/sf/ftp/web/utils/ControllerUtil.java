package com.sf.ftp.web.utils;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.alibaba.fastjson.JSON;

/**
 * Controller工具
 * @author abner.li
 * @date 2020年2月4日上午11:18:34
 */
public class ControllerUtil {
		
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


}
