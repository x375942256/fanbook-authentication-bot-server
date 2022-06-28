CREATE TABLE `t_record` (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `fb_user` bigint(50) NOT NULL COMMENT 'fanbook用户ID',
                            `mobile` varchar(50) NOT NULL COMMENT '手机号码',
                            `guild` bigint(255) NOT NULL COMMENT '服务器ID',
                            `status` int(1) DEFAULT NULL COMMENT '状态',
                            `create_time` varchar(50) DEFAULT NULL,
                            `update_time` varchar(50) DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE KEY `fb_user` (`fb_user`,`mobile`,`guild`)
) ENGINE=InnoDB AUTO_INCREMENT=4947 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;