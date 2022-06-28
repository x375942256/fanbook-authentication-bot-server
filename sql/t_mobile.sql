CREATE TABLE `t_mobile` (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `mobile` varchar(20) NOT NULL COMMENT '手机号码',
                            `guild` bigint(50) NOT NULL COMMENT '服务器',
                            `role_name` varchar(255) DEFAULT NULL COMMENT '角色名称',
                            `role_ids` varchar(255) DEFAULT NULL COMMENT '角色ID',
                            `sheet_no` int(11) DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `mobile` (`mobile`,`guild`,`sheet_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34898 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;