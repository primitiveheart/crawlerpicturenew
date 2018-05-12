/*
MySQL Data Transfer
Source Host: localhost
Source Database: crawler
Target Host: localhost
Target Database: crawler
Date: 2018/5/12 17:52:12
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for keyword
-- ----------------------------
CREATE TABLE `keyword` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `keyword` varchar(100) DEFAULT NULL,
  `is_search` char(2) DEFAULT NULL,
  `is_new` char(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pic
-- ----------------------------
CREATE TABLE `pic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keyword_id` int(11) DEFAULT NULL,
  `url_id` int(11) DEFAULT NULL,
  `pic_url` varchar(300) DEFAULT NULL,
  `pic_name` varchar(100) DEFAULT NULL,
  `pic_description` varchar(500) DEFAULT NULL,
  `pic_web_url` varchar(300) DEFAULT NULL,
  `title_frequence` int(2) unsigned zerofill DEFAULT '00',
  `body_frequence` int(5) unsigned zerofill DEFAULT '00000',
  PRIMARY KEY (`id`),
  KEY `foreign_keyword_id` (`keyword_id`),
  KEY `foreign_url_id` (`url_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11707 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for url
-- ----------------------------
CREATE TABLE `url` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url_site` varchar(100) DEFAULT NULL,
  `keyword` varchar(100) DEFAULT NULL,
  `is_search` char(2) DEFAULT NULL,
  `is_new` char(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `keyword` VALUES ('1', '污水处理厂', '1', '0');
INSERT INTO `url` VALUES ('11', 'http://www.sina.com.cn/', '污水处理厂', '1', '0');
