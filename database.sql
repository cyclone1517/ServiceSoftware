/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : servicesoftware

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 15/04/2019 13:43:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_data
-- ----------------------------
DROP TABLE IF EXISTS `tb_data`;
CREATE TABLE `tb_data`  (
  `collectorId` int(11) NOT NULL,
  `meterId` int(11) NOT NULL,
  `data` int(11) NOT NULL,
  `state` int(11) NOT NULL,
  PRIMARY KEY (`collectorId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_detail`;
CREATE TABLE `tb_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `collectorId` int(11) NOT NULL COMMENT '集中器id',
  `loginTime` timestamp(0) NOT NULL COMMENT '本次登录时间',
  `logoutTime` timestamp(0) NULL DEFAULT NULL COMMENT '本次登出时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_login
-- ----------------------------
DROP TABLE IF EXISTS `tb_login`;
CREATE TABLE `tb_login`  (
  `collectorId` int(11) NOT NULL,
  `state` tinyint(4) NOT NULL,
  `time` timestamp(0) NOT NULL,
  PRIMARY KEY (`collectorId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

delimiter $$
REPLACE PROCEDURE insertLoginDetail(IN currId int, IN time TIMESTAMP)
BEGIN
    SET @maxLoginId = SELECT MAX(id) from tb_detail;
    (
        SELECT id from tb_detail WHERE collectorId = currId
    ) tempDetail;

    SELECT logoutTime from tb_detail
    where id = @maxLoginId
    INTO @newlyOut;

    IF @newlyOut = null
    THEN
        update tb_detail loginTime = time where id = @maxLoginId;
    ELSE
        insert into tb_detail(collectorId, loginTime) values (currId, time);
    END IF;
END;
$$
delimiter ;