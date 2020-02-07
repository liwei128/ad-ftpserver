package com.sf.ftp.web.beans.common;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 分页结果
 * @author abner.li
 * @date 2020年2月4日上午11:25:44
 */
public class PagingData<T> {
	
	/**
	 * 总页数
	 */
	private int pageTotal;
	
	/**
	 * 总记录数
	 */
	private int total;
	
	/**
	 * 数据列表
	 */
	private List<T> rows;

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public static <T> PagingData<T> getInstance(int pageSize, int total, List<T> list) {
		PagingData<T> pagingData = new PagingData<>();
		pagingData.setRows(list);
		pagingData.setTotal(total);
		int end = total%pageSize>0?1:0;
		pagingData.setPageTotal(total/pageSize+end);
		return pagingData;
	}
	
	

}
