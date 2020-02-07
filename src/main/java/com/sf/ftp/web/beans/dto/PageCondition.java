package com.sf.ftp.web.beans.dto;

import com.alibaba.fastjson.JSON;

/**
 * 分页条件
 * @author abner.li
 * @date 2020年2月4日下午7:40:37
 */
public class PageCondition{
	
	private int limit = 20;
	
	private int offset = 0;


	public int getLimit() {
		return limit;
	}


	public void setLimit(int limit) {
		this.limit = limit;
	}


	public int getOffset() {
		return offset;
	}


	public void setOffset(int offset) {
		this.offset = offset;
	}


	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	

}
