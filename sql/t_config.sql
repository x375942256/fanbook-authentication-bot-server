CREATE TABLE `t_config` (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `tx_clientId` varchar(255) NOT NULL,
                            `tx_secret` varchar(255) DEFAULT NULL,
                            `tx_user_token` varchar(255) DEFAULT NULL,
                            `tx_user_openid` varchar(255) DEFAULT NULL,
                            `tx_user_refresh_token` varchar(255) DEFAULT NULL,
                            `word_code` varchar(255) DEFAULT NULL COMMENT '文档CODE',
                            `create_time` datetime DEFAULT NULL,
                            `update_time` datetime DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;