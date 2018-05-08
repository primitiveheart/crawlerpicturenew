/*
MySQL Data Transfer
Source Host: localhost
Source Database: crawler
Target Host: localhost
Target Database: crawler
Date: 2018/5/8 12:41:07
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
  PRIMARY KEY (`id`),
  KEY `foreign_keyword_id` (`keyword_id`),
  KEY `foreign_url_id` (`url_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `keyword` VALUES ('1', '污水处理厂', '1', '1');
INSERT INTO `pic` VALUES ('25', '1', '-1', 'http://img4.imgtn.bdimg.com/it/u=2232172891,3146995249&fm=27&gp=0.jpg', '种城镇污水处理厂提标改造工艺及典型案例分析', '为满足更严格的排放标准，中国城镇污水处理厂正在进行新一轮的提标改造。提标改造中的工艺选择不仅关系到排放是否能够达到更严的标准，还关系到以能耗为主的污水处理成本。从中国各地提标改造工艺选择及实际工程情况来看，提标改造必须对原水水质、地域、原工艺的可利用程度、当地社会经济条件、运行管理水平等多方面进行考虑，在小试、中试或相似工程的经验基础上确定工艺。有条件的城镇污水处理厂宜考虑处理设施前端配套建设调蓄水池，处理设施末端在排出前设置出水缓冲区，提高稳定达标的保障度。', 'http://www.water8848.com/news/201608/29/75334.html');
INSERT INTO `pic` VALUES ('26', '-1', '1', 'http://img4.imgtn.bdimg.com/it/u=2590083204,1729667342&fm=27&gp=0.jpg', '污水处理厂典型工艺流程图', '接触氧化池进水水质还不算太差,可以好氧处理,氧化沟工艺,可根据流量和处理负荷设计池宽和池长,如果是生物滤塔也可以设计塔高和层数.Amn-O工艺也可解决', 'http://www.haoxyx.com/b/9847/98472888.html');
INSERT INTO `url` VALUES ('1', 'test', 'testkeyword', '1', '0');
INSERT INTO `url` VALUES ('2', 'test2', 'testkeyword2', '0', '0');
INSERT INTO `url` VALUES ('3', 'test', 'sadfsdfsd', '1', '1');
INSERT INTO `url` VALUES ('9', 'test3', 'test3teset', '1', '1');
INSERT INTO `url` VALUES ('10', 'test3', 'tsest', '1', '1');
