CREATE TABLE `t_word` (
                          `id` int(11) NOT NULL AUTO_INCREMENT,
                          `guild` bigint(50) DEFAULT NULL COMMENT '服务器ID',
                          `work_code` varchar(255) DEFAULT NULL COMMENT '文档ID',
                          `roles` varchar(255) DEFAULT NULL COMMENT '角色ID',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;