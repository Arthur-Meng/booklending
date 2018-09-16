/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80012
Source Host           : localhost:3306
Source Database       : booklending

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2018-09-16 21:38:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `book`
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `bookid` varchar(255) NOT NULL,
  `ISBN` varchar(13) DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `author` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `pubdate` varchar(255) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `binding` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `translator` varchar(255) DEFAULT NULL,
  `publisher` varchar(255) DEFAULT NULL,
  `author_intro` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `summary` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `price` varchar(255) DEFAULT NULL,
  `ebook_price` varchar(255) DEFAULT NULL,
  UNIQUE KEY `book_index` (`bookid`),
  KEY `ISBN` (`ISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES ('7367202390db4d54841803278c0a0e30', '9787121084379', 'JavaScript语言精粹', 'Douglas Crockford', '2009-4', 'JavaScript,Web前端开发,编程,web开发,javacript,计算机,O\'Reilly,web', 'https://img3.doubanio.com/view/subject/m/public/s3651235.jpg', '平装', '赵泽欣,鄢学鹍', '电子工业出版社', 'Douglas Crockford是一名来自Yahoo!的资深JavaScript架构师，以创造和维护JSON (JavaScriptObject Notation) 格式而为大家所熟知。他定期在各类会议上发表有关高级JavaScript的主题演讲。', '本书通过对JavaScript语言的分析，甄别出好的和坏的特性，从而提取出相对这门语言的整体而言具有更好的可靠性、可读性和可维护性的JavaScript的子集，以便你能用它创建真正可扩展的和高效的代码。\n雅虎资深JavaScript架构师Douglas Crockford倾力之作。\n向读者介绍如何运用JavaScript创建真正可扩展的和高效的代码。', '35.00元', null);

-- ----------------------------
-- Table structure for `booklike`
-- ----------------------------
DROP TABLE IF EXISTS `booklike`;
CREATE TABLE `booklike` (
  `ISBN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `userid` varchar(5) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`ISBN`,`userid`,`status`),
  UNIQUE KEY `like_index` (`ISBN`,`userid`,`status`) USING BTREE,
  KEY `like_index2` (`userid`) USING BTREE,
  CONSTRAINT `like_key1` FOREIGN KEY (`ISBN`) REFERENCES `book` (`isbn`),
  CONSTRAINT `like_key2` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of booklike
-- ----------------------------

-- ----------------------------
-- Table structure for `bookstatus`
-- ----------------------------
DROP TABLE IF EXISTS `bookstatus`;
CREATE TABLE `bookstatus` (
  `bookid` varchar(255) NOT NULL,
  `status` int(1) NOT NULL,
  `addtime` date DEFAULT NULL,
  `likeall` int(11) DEFAULT NULL,
  `wannaall` int(11) DEFAULT NULL,
  `commentall` int(11) DEFAULT NULL,
  `scoreall` double(11,1) DEFAULT NULL,
  `ifnew` int(1) DEFAULT NULL,
  UNIQUE KEY `book_status_index` (`bookid`),
  CONSTRAINT `book_status_key` FOREIGN KEY (`bookid`) REFERENCES `book` (`bookid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bookstatus
-- ----------------------------
INSERT INTO `bookstatus` VALUES ('7367202390db4d54841803278c0a0e30', '1', '2018-09-16', null, null, null, null, '1');

-- ----------------------------
-- Table structure for `borrow`
-- ----------------------------
DROP TABLE IF EXISTS `borrow`;
CREATE TABLE `borrow` (
  `borrowid` varchar(255) NOT NULL,
  `userid` varchar(5) NOT NULL,
  `bookid` varchar(255) NOT NULL,
  `borrowtime` date NOT NULL,
  `confirmtime` date DEFAULT NULL,
  `borrowstatus` int(1) NOT NULL,
  `returntime` date DEFAULT NULL,
  `renew` int(1) DEFAULT NULL,
  UNIQUE KEY `borrow_index` (`borrowid`),
  KEY `borrow_key2` (`bookid`),
  KEY `borrow_index2` (`userid`,`bookid`) USING BTREE,
  CONSTRAINT `borrow_key1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`),
  CONSTRAINT `borrow_key2` FOREIGN KEY (`bookid`) REFERENCES `book` (`bookid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of borrow
-- ----------------------------
INSERT INTO `borrow` VALUES ('d15a6f2ec4624149bdfff3b8ccc6c8d0', '22510', '7367202390db4d54841803278c0a0e30', '2018-09-16', '2018-09-16', '3', '2018-10-31', '1');

-- ----------------------------
-- Table structure for `commend`
-- ----------------------------
DROP TABLE IF EXISTS `commend`;
CREATE TABLE `commend` (
  `userid` varchar(5) NOT NULL,
  `bookid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `reason` text,
  `date` date DEFAULT NULL,
  `like` int(10) unsigned zerofill DEFAULT '0000000000',
  UNIQUE KEY `commend_index` (`userid`,`bookid`),
  KEY `commend_keys` (`bookid`),
  CONSTRAINT `commend_key1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of commend
-- ----------------------------

-- ----------------------------
-- Table structure for `comment`
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `ISBN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `userid` varchar(255) NOT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `date` date NOT NULL,
  `score` int(11) DEFAULT NULL,
  UNIQUE KEY `comment_index` (`ISBN`,`userid`) USING BTREE,
  KEY `comment_index2` (`userid`) USING BTREE,
  CONSTRAINT `comment_key1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of comment
-- ----------------------------

-- ----------------------------
-- Table structure for `likecommend`
-- ----------------------------
DROP TABLE IF EXISTS `likecommend`;
CREATE TABLE `likecommend` (
  `bookid` varchar(255) NOT NULL,
  `userid` varchar(255) NOT NULL,
  `date` date DEFAULT NULL,
  UNIQUE KEY `likecommend_index` (`bookid`,`userid`),
  KEY `likecommend_key2` (`userid`),
  CONSTRAINT `likecommend_key1` FOREIGN KEY (`bookid`) REFERENCES `book` (`bookid`),
  CONSTRAINT `likecommend_key2` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of likecommend
-- ----------------------------

-- ----------------------------
-- Table structure for `logincount`
-- ----------------------------
DROP TABLE IF EXISTS `logincount`;
CREATE TABLE `logincount` (
  `date` date NOT NULL,
  `count` int(6) DEFAULT NULL,
  UNIQUE KEY `logincount_index` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of logincount
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userid` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `password` varchar(16) NOT NULL,
  `preference` varchar(255) DEFAULT NULL,
  `allloginnum` int(11) DEFAULT NULL,
  `yearloginnum` int(11) DEFAULT NULL,
  `mouthloginnum` int(11) DEFAULT NULL,
  `weekloginnum` int(11) DEFAULT NULL,
  UNIQUE KEY `user_index` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('22510', '孟佳伟', '903019946@qq.com', '1', '1', null, null, null, null, null);
