/*
Navicat MySQL Data Transfer

Source Server         : localdb
Source Server Version : 80012
Source Host           : localhost:3306
Source Database       : servicesoftware

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2019-03-17 23:31:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_data
-- ----------------------------
DROP TABLE IF EXISTS `tb_data`;
CREATE TABLE `tb_data` (
  `collectorId` int(11) NOT NULL,
  `meterId` int(11) NOT NULL,
  `data` int(11) NOT NULL,
  `state` int(11) NOT NULL,
  PRIMARY KEY (`collectorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_heartbeat
-- ----------------------------
DROP TABLE IF EXISTS `tb_heartbeat`;
CREATE TABLE `tb_heartbeat` (
  `collectorId` int(11) NOT NULL,
  `port` int(11) NOT NULL,
  `time` timestamp NOT NULL,
  PRIMARY KEY (`collectorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_login
-- ----------------------------
DROP TABLE IF EXISTS `tb_login`;
CREATE TABLE `tb_login` (
  `collectorId` int(11) NOT NULL,
  `port` int(11) NOT NULL,
  `state` tinyint(4) NOT NULL,
  `time` timestamp NOT NULL,
  PRIMARY KEY (`collectorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
