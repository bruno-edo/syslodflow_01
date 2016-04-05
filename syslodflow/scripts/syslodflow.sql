/*
Navicat MySQL Data Transfer

Source Server         : wamp local
Source Server Version : 50520
Source Host           : localhost:3306
Source Database       : syslodflow

Target Server Type    : MYSQL
Target Server Version : 50520
File Encoding         : 65001

Date: 2016-03-30 15:25:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `en_usuario`
-- ----------------------------
DROP TABLE IF EXISTS `en_usuario`;
CREATE TABLE `en_usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `login` varchar(32) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `ativo` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `ldwproject`;
CREATE TABLE `ldwproject` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
   'goal' varchar(255) DEFAULT NULL,
  `homepage` varchar(255) DEFAULT NULL,
  `report` varchar(255) DEFAULT NULL,
  `ldworkflow` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `ldworkflow`;
CREATE TABLE `ldworkflow` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  'pre_condition' varchar(255) DEFAULT NULL,
  `post_condition` varchar(255) DEFAULT NULL,
  `report` varchar(255) DEFAULT NULL,
  `ldworkflow` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;






-- ----------------------------
-- Records of en_usuario
-- ----------------------------
INSERT INTO `en_usuario` VALUES ('1', 'admin', 'admin', '1');
