CREATE TABLE `oa_url_info` (
  `id` bigint(20) NOT NULL,
  `url` varchar(150) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `gmt_create` date DEFAULT NULL COMMENT '创建时间',
  `enable` tinyint(1) DEFAULT NULL COMMENT '是否有效',
  `request_type` varchar(10) DEFAULT NULL COMMENT '请求类型',
  `class_name` varchar(255) DEFAULT NULL COMMENT '类名',
  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父行id',
  `parent_url` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;