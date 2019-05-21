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

 Date: 22/04/2019 11:28:31
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
-- Table structure for tb_duplicate
-- ----------------------------
DROP TABLE IF EXISTS `tb_duplicate`;
CREATE TABLE `tb_duplicate`  (
  `collectorId` int(11) NOT NULL,
  `oldScInfo` varchar(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `newScInfo` varchar(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`collectorId`, `oldScInfo`, `newScInfo`, `date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- View structure for maxid
-- ----------------------------
DROP VIEW IF EXISTS `maxid`;
CREATE VIEW MAXID
AS
SELECT MAX(id) AS maxId, collectorId
FROM tb_Detail
GROUP BY collectorId;

-- ---------------------------------
-- 存储过程：插入登录详情
-- 调用示例
-- call insertLoginDetail(4006, "2019-04-16 23:55:03");
-- ---------------------------------
/*
	逻辑分析：
	1. 若该集中器无记录 -> 插入记录
	2. 若该集中器有记录
		2.1 且登出为空 -> 更新登录时间
		2.2 且登出不为空 -> 上一条记录已经完整，创建新记录

	参数：
	@currId 待插入集中器id
	@newTime 最新登录包抵达时间

	@maxLoginId 当前集中器的最新记录号
	@maxIdOutState 最新记录号对应的登出时间填入状况
*/
delimiter ;
drop procedure if exists insertLoginDetail;
delimiter $$
create PROCEDURE insertLoginDetail(IN currId int, IN newTime TIMESTAMP)
begin
	declare maxLoginId int;
	declare maxIdOutState timestamp;

	SELECT maxId FROM maxid
  WHERE collectorId = currId
  INTO maxLoginId;

	SELECT logoutTime FROM tb_detail
	WHERE id = maxLoginId
	INTO maxIdOutState;

	IF maxLoginId is null
	THEN
		INSERT INTO tb_detail(collectorId, loginTime) VALUES (currId, newTime);
	ELSE
		IF maxIdOutState is null
		THEN
			UPDATE tb_detail SET loginTime = newTime WHERE id = maxLoginId;
		ELSE
			INSERT INTO tb_detail(collectorId, loginTime) VALUES (currId, newTime);
		END IF;
	END IF;
end $$
delimiter ;

-- ---------------------------------
-- 存储过程：插入登出详情
-- 调用示例
-- call insertLogoutDetail(4006, "2019-04-16 23:55:03");
-- ---------------------------------
delimiter ;
drop procedure if exists insertLogoutDetail;
delimiter $$
create PROCEDURE insertLogoutDetail(IN currId int, IN newTime TIMESTAMP)
begin
	declare maxLogoutId int;

	select maxId from maxid
  where collectorId = currId
  INTO maxLogoutId;

	UPDATE tb_Detail
  SET logoutTime = newTime
  WHERE id = maxLogoutId;

end $$
delimiter ;