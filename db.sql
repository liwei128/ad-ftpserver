CREATE TABLE `ftp_user` (
  `userid` varchar(64) NOT NULL COMMENT '工号',
  `homedirectory` varchar(255) NOT NULL COMMENT '主目录',
  `enableflag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '启用1,0',
  `idletime` int(11) NOT NULL DEFAULT '300' COMMENT '以秒为单位，设置最大空闲时间',
  `writepermission` tinyint(1) NOT NULL DEFAULT '1' COMMENT '写权限1,0',
  `adminpermission` tinyint(1) NOT NULL DEFAULT '0' COMMENT '管理员权限1,0',
  `maxloginnumber` int(11) NOT NULL DEFAULT '0' COMMENT '最大登录数',
  `maxloginperip` int(11) NOT NULL DEFAULT '0' COMMENT '同ip最大登录数',
  `downloadrate` int(11) NOT NULL DEFAULT '0' COMMENT '下载速率',
  `uploadrate` int(11) NOT NULL DEFAULT '0' COMMENT '上传速率',
  `current_login_number` int(11) NOT NULL DEFAULT '0' COMMENT '用户当前登录数量',
  `expires` varchar(64) DEFAULT NULL COMMENT '有效期yyyy-MM-dd',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ftp_access_log` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `userid` varchar(64) NOT NULL COMMENT '工号',
  `ip` varchar(64) NOT NULL COMMENT 'ip',
  `operation` varchar(64) NOT NULL COMMENT '操作',
  `filepath` varchar(255) NOT NULL COMMENT '文件路径',
  `command` varchar(255) NOT NULL COMMENT '操作命令',
  `access_time` varchar(64) NOT NULL COMMENT '访问时间yyyy-MM-dd HH:mm:ss',
  PRIMARY KEY (`id`),
  KEY `userid_index` (`userid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ftp_statistics` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `startTime` varchar(64) DEFAULT NULL COMMENT '服务启动时间yyyy-MM-dd HH:mm:ss',
  `totalUploadNumber` varchar(64) DEFAULT NULL COMMENT '上传的文件数量',
  `totalDownloadNumber` varchar(64) DEFAULT NULL COMMENT '下载的文件数量',
  `totalDeleteNumber` varchar(64) DEFAULT NULL COMMENT '删除的文件数量',
  `totalUploadSize` varchar(64) DEFAULT NULL COMMENT '上传的总文件大小,单位bytes',
  `totalDownloadSize` varchar(64) DEFAULT NULL COMMENT '下载的总文件大小,单位bytes',
  `totalDirectoryCreated` varchar(64) DEFAULT NULL COMMENT '创建的总目录数',
  `totalDirectoryRemoved` varchar(64) DEFAULT NULL COMMENT '删除的总目录数',
  `totalConnectionNumber` varchar(64) DEFAULT NULL COMMENT '连接总数',
  `currentConnectionNumber` varchar(64) DEFAULT NULL COMMENT '当前连接数',
  `totalLoginNumber` varchar(64) DEFAULT NULL COMMENT '登录总数',
  `totalFailedLoginNumber` varchar(64) DEFAULT NULL COMMENT '登录失败总数',
  `currentLoginNumber` varchar(64) DEFAULT NULL COMMENT '当前登录数量',
  `totalAnonymousLoginNumber` varchar(64) DEFAULT NULL COMMENT '匿名登录总数',
  `currentAnonymousLoginNumber` varchar(64) DEFAULT NULL COMMENT '当前匿名登录数量',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;