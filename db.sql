DROP TABLE IF EXISTS `ftp_user`;
CREATE TABLE `ftp_user`  (
  `userid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工号',
  `usertype` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户类型',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `homedirectory` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主目录',
  `enableflag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '启用1,0',
  `idletime` int(11) NOT NULL DEFAULT 300 COMMENT '以秒为单位，设置最大空闲时间',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问权限',
  `adminpermission` tinyint(1) NOT NULL DEFAULT 0 COMMENT '管理员权限1,0',
  `maxloginnumber` int(11) NOT NULL DEFAULT 0 COMMENT '最大登录数',
  `maxloginperip` int(11) NOT NULL DEFAULT 0 COMMENT '同ip最大登录数',
  `downloadrate` int(11) NOT NULL DEFAULT 0 COMMENT '下载速率',
  `uploadrate` int(11) NOT NULL DEFAULT 0 COMMENT '上传速率',
  `current_login_number` int(11) NOT NULL DEFAULT 0 COMMENT '用户当前登录数量',
  `expires` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '有效期yyyy-MM-dd',
  `handler` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后操作人',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`userid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `ftp_user` VALUES ('admin', 'USER', '123456', 'xxx@xxx.com', '/', 1, 300, '上传,下载,删除,修改', 1, 0, 0, 0, 0, 0, '', 'system', '2020-03-09 09:20:38');

DROP TABLE IF EXISTS `ftp_statistics`;
CREATE TABLE `ftp_statistics`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `startTime` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务启动时间yyyy-MM-dd HH:mm:ss',
  `totalUploadNumber` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上传的文件数量',
  `totalDownloadNumber` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下载的文件数量',
  `totalDeleteNumber` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除的文件数量',
  `totalUploadSize` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上传的总文件大小,单位bytes',
  `totalDownloadSize` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下载的总文件大小,单位bytes',
  `totalDirectoryCreated` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建的总目录数',
  `totalDirectoryRemoved` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除的总目录数',
  `totalConnectionNumber` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接总数',
  `currentConnectionNumber` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前连接数',
  `totalLoginNumber` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录总数',
  `totalFailedLoginNumber` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录失败总数',
  `currentLoginNumber` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前登录数量',
  `totalAnonymousLoginNumber` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '匿名登录总数',
  `currentAnonymousLoginNumber` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前匿名登录数量',
  `updateTime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `ftp_access_log`;
CREATE TABLE `ftp_access_log`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `userid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工号',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ip',
  `operation` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作',
  `filepath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件路径',
  `access_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问时间yyyy-MM-dd HH:mm:ss',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `userid_index`(`userid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;