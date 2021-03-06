package com.sf.ftp.web.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sf.ftp.web.beans.common.BaseRetCode;
import com.sf.ftp.web.beans.common.ResultData;
import com.sf.ftp.web.service.AdService;

/**
 * AD相关服务
 * @author abner.li
 * @date 2020年2月5日下午10:35:31
 */
@Service
public class AdserviceImpl implements AdService{
	
	private final static Logger logger = LoggerFactory.getLogger(AdserviceImpl.class);
	private final static String USERID = "sAMAccountName";
	private final static String DIS_NAME = "distinguishedName";
	private final static String USER_NAME = "sn";
	
	@Value("${ldapUrl}")
	private String ldapUrl;
	
	@Value("${adPrefix}")
	private String adPrefix;
	
	@Value("${adAccount}")
	private String adAccount;
	
	@Value("${adPassword}")
	private String adPassword;
	
	/**
	 * 搜索域节点DC=SF,DC=com
	 */
	@Value("${adSearchBase}")
	private String searchBase;
	
	@Override
	public ResultData<String> isAdUser(String userid) {
		try {
			Map<String, List<String>> info = queryInfo(USERID,userid,DIS_NAME,USER_NAME);
			if(info.size()>0&&StringUtils.isNotEmpty(info.get(DIS_NAME).get(0))){
				String username = info.get(USER_NAME)==null?"":info.get(USER_NAME).get(0);
				return ResultData.getInstance(username);
			}
		} catch (Exception e) {
			logger.error("isAdUser:{},Exception",userid,e);
		}
		return ResultData.getInstance(BaseRetCode.FAIL);
	}
	
	@Override
	public boolean checkUser(String userid, String password) {
		DirContext dc = null;
		try {
			dc = createUserDc(userid, password);
			return true;
		} catch (Exception e) {
			logger.info("user:{} adCheck fail", userid);
			return false;
		} finally {
			if (dc != null) {
				try {
					dc.close();
				} catch (NamingException e) {
					logger.error("DirContext close fail", e);
				}
			}
		}
	}
	
	private Map<String, List<String>> queryInfo(String paramName,String paramValue, String... fields) throws Exception {
		Map<String, List<String>> map = Maps.newHashMap();
		DirContext dc = null;
		try{
			dc = createDc();
			SearchControls searchCtls = new SearchControls();
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			// 查询条件
			String searchFilter = paramName+"=" + paramValue;
			searchCtls.setReturningAttributes(fields);
			NamingEnumeration<?> answer = dc.search(searchBase, searchFilter, searchCtls);
			//解析结果
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				NamingEnumeration<? extends Attribute> ne = sr.getAttributes().getAll();
				while(ne.hasMore()){
					Attribute attr = (Attribute) ne.next();
					NamingEnumeration<?> all = attr.getAll();
					List<String> list = Lists.newArrayList();
					while(all.hasMore()){
						String value = all.next().toString();
						list.add(value);
					}
					map.put(attr.getID().toString(), list);
				}
			}
			return map; 
		}finally {
			if(dc!=null){
				dc.close();
			}
		}
	}
	
	private InitialLdapContext createDc() throws Exception {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, adPrefix+adAccount);
        env.put(Context.SECURITY_CREDENTIALS, adPassword);
        env.put(Context.PROVIDER_URL, ldapUrl);
        return new InitialLdapContext(env,null);
    }
	
	private InitialLdapContext createUserDc(String account, String password) throws Exception {
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, adPrefix + account);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.PROVIDER_URL, ldapUrl);
		return new InitialLdapContext(env, null);
	}


}
