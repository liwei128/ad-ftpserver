package com.sf.ftp.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.sf.ftp.web.beans.UserConstant;
import com.sf.ftp.web.beans.common.BaseRetCode;
import com.sf.ftp.web.beans.common.ResultData;
import com.sf.ftp.web.utils.ControllerUtil;

/**
 * 全局过滤器
 * @author abner.li
 * @date 2020年2月4日上午11:08:00
 */
@Configuration
public class GlobalFilterConfig {
	
	private static final String DOT = ".";
	private static final String API = "/api";

	@Value("${ignoreLoginUri}")
	private String[] ignoreLoginUri;
	
	@Value("${apiToken}")
	private String apiToken;
	
	

	/**
     * 登录过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> loginFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new Filter(){

			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
					throws IOException, ServletException {
				HttpServletRequest req = (HttpServletRequest)request;
				//不拦截静态资源.
				if(req.getRequestURI().contains(DOT)) {
					chain.doFilter(request, response);
					return;
				}
				//api接口验证
				if(req.getRequestURI().startsWith(API)) {
					if(!apiCheck(req)) {
						ControllerUtil.setResponseData(ResultData.getInstance(BaseRetCode.AUTH_FAILD));
					}else {
						chain.doFilter(request, response);
					}
					return;
				}
				//判断是否登录
				Object user = req.getSession().getAttribute(UserConstant.USER);
				if(isIgnoreLogin(req.getRequestURI())||user!=null){
					chain.doFilter(request, response);
				}else{
					ControllerUtil.setResponseData(ResultData.getInstance(BaseRetCode.NOT_LOGIN));
				}
			}
			
        	
        });
        registration.addUrlPatterns("/*");
        registration.setName("loginFilter");
        registration.setOrder(2);
        return registration;
    }
    
    /**
     * api接口验证
     * @param req
     * @return
     * boolean
     */
    protected boolean apiCheck(HttpServletRequest req) {
    	return apiToken.equals(req.getHeader("ftp-token"));
	}

	/**
     * 是否忽略登录
     * @param requestUri
     * @return
     */
    private boolean isIgnoreLogin(String requestUri) {
    	for(String uri : ignoreLoginUri){
    		if(uri.equals(requestUri)) {
    			return true;
    		}
    	}
		return false;
	}
    


    /**
     * 跨域过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
    	 FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
    	 
    	 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	 CorsConfiguration corsConfiguration = new CorsConfiguration();
         corsConfiguration.addAllowedOrigin("*"); 
         corsConfiguration.addAllowedHeader("*"); 
         corsConfiguration.addAllowedMethod("*"); 
         corsConfiguration.setAllowCredentials(true);
         source.registerCorsConfiguration("/**", corsConfiguration);
    	 registration.setFilter(new CorsFilter(source));
    	 registration.addUrlPatterns("/*");
         registration.setName("CorsFilter");
         registration.setOrder(1);
         return registration;
    }
    
}
