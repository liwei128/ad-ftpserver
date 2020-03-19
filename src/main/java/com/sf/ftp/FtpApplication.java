package com.sf.ftp;

import org.apache.commons.lang3.ArrayUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sf.ftp.server.core.SfFtpServer;

/**
 * 服务启动 
 * ftp服务和web管理页面均可选择独立运行、分离部署
 * 项目可按包路径com.sf.ftp.server、com.sf.ftp.web拆分为两个项目，项目间无任何依赖、耦合
 * 
 * @author abner.li
 * @date 2020年2月6日上午11:08:30
 */
@SpringBootApplication
@MapperScan(basePackages = { "com.sf.ftp.web.dao" })
public class FtpApplication {

	private static final String FTP = "ftp";

	private static final String WEB = "web";

	public static void main(String[] args) throws Exception {

		if (ArrayUtils.contains(args, FTP)) {
			// 启动ftp服务
			SfFtpServer.run();
		}
		if (ArrayUtils.contains(args, WEB)) {
			// 启动web管理页面
			SpringApplication.run(FtpApplication.class, args);
		}
	}

}
