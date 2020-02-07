package com.sf.ftp.web.beans.common;


import java.io.Serializable;

import com.alibaba.fastjson.JSON;
/**
 * 返回数据
 * @author abner.li
 * @date 2020年2月4日上午11:26:02
 */
public class ResultData<T> implements RetCode,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3398323943665457721L;

	private int code;
	
	private String msg;
	
	private T data;
	
	@Override
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	@Override
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public ResultData(RetCode retCode) {
		this.code = retCode.getCode();
		this.msg = retCode.getMsg();
	}

	public ResultData(T data) {
		this();
		this.data = data;
	}
	
	public ResultData() {
		this.code = BaseRetCode.SUCCESS.getCode();
		this.msg = BaseRetCode.SUCCESS.getMsg();
	}

	public static <T> ResultData<T> getInstance(RetCode retCode){
		return new ResultData<T>(retCode);
	}
	
	public static <T> ResultData<T> getInstance(T data){
		return new ResultData<T>(data);
	}
	
	public static ResultData<Void> getInstance(){
		return new ResultData<Void>();
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	

}
