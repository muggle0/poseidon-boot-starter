/*
Navicat Premium Data Transfer

Source Server         : local
Source Server Type    : MySQL
Source Server Version : 50729
Source Host           : localhost:3306
Source Schema         : my_oa

Target Server Type    : MySQL
Target Server Version : 50729
File Encoding         : 65001

Date: 01/01/2022 15:29:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
`name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
`path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
`permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
`component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`type` int(5) NOT NULL COMMENT '类型     0：目录   1：菜单   2：按钮',
`icon` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
`orderNum` int(11) NULL DEFAULT NULL COMMENT '排序',
`created` datetime(0) NOT NULL,
`updated` datetime(0) NULL DEFAULT NULL,
`statu` int(5) NOT NULL,
`enabled` tinyint(1) NULL DEFAULT NULL,
PRIMARY KEY (`id`) USING BTREE,
UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
`code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
`remark` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
`created` datetime(0) NULL DEFAULT NULL,
`updated` datetime(0) NULL DEFAULT NULL,
`statu` int(5) NOT NULL,
PRIMARY KEY (`id`) USING BTREE,
UNIQUE INDEX `name`(`name`) USING BTREE,
UNIQUE INDEX `code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (3, '普通用户', 'normal', '只有基本查看功能', '2021-01-04 10:09:14', '2021-01-30 08:19:52', 1);
INSERT INTO `sys_role` VALUES (6, '超级管理员', 'admin', '系统默认最高权限，不可以编辑和任意修改', '2021-01-16 13:29:03', '2021-01-17 15:50:45', 1);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`role_id` bigint(20) NOT NULL,
`menu_id` bigint(20) NOT NULL,
`permission` varchar(255) NOT NULL COMMENT '菜单编码',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`city` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`created` datetime(0) NULL DEFAULT NULL,
`updated` datetime(0) NULL DEFAULT NULL,
`last_login` datetime(0) NULL DEFAULT NULL,
`statu` int(5) NOT NULL,
`enabled` tinyint(1) NOT NULL,
PRIMARY KEY (`id`) USING BTREE,
UNIQUE INDEX `UK_USERNAME`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$KzgwFkx9u4yasxHTkvP3j.XdZD5XvaDvecJ8YHHX6t8Nohj9LeN9K', 'https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg', '123@qq.com', '广州', '2021-01-12 22:13:53', '2021-01-16 16:57:32', '2020-12-30 08:38:37', 1, 1);
INSERT INTO `sys_user` VALUES (2, 'test', '$2a$10$0ilP4ZD1kLugYwLCs4pmb.ZT9cFqzOZTNaMiHxrBnVIQUGUwEvBIO', 'https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg', 'test@qq.com', NULL, '2021-01-30 08:20:22', '2021-01-30 08:55:57', NULL, 1, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`user_id` bigint(20) NOT NULL,
`role_id` bigint(20) NOT NULL,
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (4, 1, 6);
INSERT INTO `sys_user_role` VALUES (7, 1, 3);
INSERT INTO `sys_user_role` VALUES (13, 2, 3);

DROP TABLE IF EXISTS `sys_url_info`;
CREATE TABLE `sys_url_info` (
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

SET FOREIGN_KEY_CHECKS = 1;
