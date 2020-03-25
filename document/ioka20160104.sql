-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.15


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema ioka
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ ioka;
USE ioka;

--
-- Table structure for table `ioka`.`t_auth`
--

DROP TABLE IF EXISTS `t_auth`;
CREATE TABLE `t_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=gbk;

--
-- Dumping data for table `ioka`.`t_auth`
--

/*!40000 ALTER TABLE `t_auth` DISABLE KEYS */;
INSERT INTO `t_auth` (`id`,`menu_id`,`role_id`) VALUES 
 (1,1,1),
 (2,2,1),
 (3,3,1),
 (4,4,1),
 (5,5,1),
 (6,6,1),
 (7,7,1),
 (8,8,1),
 (9,9,1),
 (10,10,1),
 (18,11,1),
 (30,1,2),
 (31,2,2),
 (32,3,2),
 (33,4,2),
 (34,6,2),
 (39,1,3),
 (40,9,3),
 (41,10,3),
 (42,11,3);
/*!40000 ALTER TABLE `t_auth` ENABLE KEYS */;


--
-- Table structure for table `ioka`.`t_dict`
--

DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dict_code` varchar(60) DEFAULT NULL,
  `dict_text` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ioka`.`t_dict`
--

/*!40000 ALTER TABLE `t_dict` DISABLE KEYS */;
INSERT INTO `t_dict` (`id`,`dict_code`,`dict_text`) VALUES 
 (1,'unit','单位'),
 (2,'gender','性别'),
 (3,'order_status','订单状态'),
 (4,'general_status','默认状态');
/*!40000 ALTER TABLE `t_dict` ENABLE KEYS */;


--
-- Table structure for table `ioka`.`t_dict_item`
--

DROP TABLE IF EXISTS `t_dict_item`;
CREATE TABLE `t_dict_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dict_code` varchar(60) DEFAULT NULL,
  `item_code` varchar(60) DEFAULT NULL,
  `item_text` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ioka`.`t_dict_item`
--

/*!40000 ALTER TABLE `t_dict_item` DISABLE KEYS */;
INSERT INTO `t_dict_item` (`id`,`dict_code`,`item_code`,`item_text`) VALUES 
 (1,'unit','box','盒'),
 (2,'unit','bottle','瓶'),
 (3,'unit','package','包'),
 (4,'unit','bucket','桶'),
 (5,'unit','bowl','碗'),
 (6,'unit','tin','听'),
 (7,'unit','pot','罐'),
 (8,'unit','cup','杯'),
 (9,'unit','item','件'),
 (10,'gender','male','男'),
 (11,'gender','female','女'),
 (12,'order_status','0','待处理'),
 (13,'general_status','0','正常'),
 (14,'general_status','1','禁止'),
 (16,NULL,NULL,'测试1');
/*!40000 ALTER TABLE `t_dict_item` ENABLE KEYS */;


--
-- Table structure for table `ioka`.`t_menu`
--

DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(60) DEFAULT NULL,
  `menu_name` varchar(60) NOT NULL,
  `menu_url` varchar(255) DEFAULT NULL,
  `sid` bigint(20) NOT NULL,
  `istop` varchar(1) NOT NULL,
  `menu_icon` varchar(255) DEFAULT NULL,
  `sstatus` varchar(60) DEFAULT NULL,
  `relation_no` varchar(512) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=gbk;

--
-- Dumping data for table `ioka`.`t_menu`
--

/*!40000 ALTER TABLE `t_menu` DISABLE KEYS */;
INSERT INTO `t_menu` (`id`,`menu_code`,`menu_name`,`menu_url`,`sid`,`istop`,`menu_icon`,`sstatus`,`relation_no`,`remarks`) VALUES 
 (1,NULL,'系统管理',NULL,0,'T','menu-icon fa fa-tachometer','0','1.',NULL),
 (2,NULL,'机构管理','system/org/org.do',1,'F','menu-icon fa fa-caret-right','0','1.2.',NULL),
 (3,NULL,'角色管理','system/role/list.do',1,'F','menu-icon fa fa-caret-right','0','1.3.',NULL),
 (4,NULL,'操作员管理','system/oper/list.do',1,'F','menu-icon fa fa-caret-right','0','1.4.',NULL),
 (5,NULL,'字典管理','system/dict/list.do',1,'F','menu-icon fa fa-caret-right','0','1.5.',NULL),
 (6,NULL,'权限设置','system/auth/list.do',1,'F','menu-icon fa fa-caret-right','0','1.6.',NULL),
 (7,NULL,'模块菜单','system/menu/list.do',1,'F','menu-icon fa fa-caret-right','0','1.7.',NULL),
 (8,NULL,'系统参数','system/param/list.do',1,'F','menu-icon fa fa-caret-right','0','1.8.',NULL),
 (9,NULL,'日志管理','',1,'F','menu-icon fa fa-caret-right','0','1.9.',''),
 (10,NULL,'基础数据',NULL,0,'T','menu-icon fa fa-list','0','10.',NULL),
 (11,NULL,'产品目录',NULL,10,'F','menu-icon fa fa-caret-right','0','10.11.',NULL);
/*!40000 ALTER TABLE `t_menu` ENABLE KEYS */;


--
-- Table structure for table `ioka`.`t_oper`
--

DROP TABLE IF EXISTS `t_oper`;
CREATE TABLE `t_oper` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `oper_code` varchar(60) DEFAULT NULL,
  `oper_name` varchar(60) DEFAULT NULL,
  `org_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `org_relation_no` varchar(512) DEFAULT NULL,
  `pwd` varchar(128) NOT NULL,
  `sstatus` varchar(60) DEFAULT NULL,
  `login_name` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=gbk;

--
-- Dumping data for table `ioka`.`t_oper`
--

/*!40000 ALTER TABLE `t_oper` DISABLE KEYS */;
INSERT INTO `t_oper` (`id`,`oper_code`,`oper_name`,`org_id`,`role_id`,`org_relation_no`,`pwd`,`sstatus`,`login_name`) VALUES 
 (8,'0000','超级管理员',1,1,'1.','670b14728ad9902aecba32e22fa4f6bd','0','sa'),
 (12,'dd','aarrr',1,2,'1.','670b14728ad9902aecba32e22fa4f6bd','0','addrrr'),
 (13,'dda','dddda',3,2,'1.3.','670b14728ad9902aecba32e22fa4f6bd','0','ddddddd');
/*!40000 ALTER TABLE `t_oper` ENABLE KEYS */;


--
-- Table structure for table `ioka`.`t_org`
--

DROP TABLE IF EXISTS `t_org`;
CREATE TABLE `t_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_code` varchar(60) DEFAULT NULL,
  `org_name` varchar(128) DEFAULT NULL,
  `sid` bigint(20) NOT NULL,
  `istop` varchar(1) NOT NULL,
  `org_icon` varchar(60) DEFAULT NULL,
  `relation_no` varchar(512) DEFAULT NULL,
  `sstatus` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=gbk;

--
-- Dumping data for table `ioka`.`t_org`
--

/*!40000 ALTER TABLE `t_org` DISABLE KEYS */;
INSERT INTO `t_org` (`id`,`org_code`,`org_name`,`sid`,`istop`,`org_icon`,`relation_no`,`sstatus`) VALUES 
 (1,NULL,'大功率',0,'T',NULL,'1.','0'),
 (2,NULL,'子部门',1,'F',NULL,'1.2.','0'),
 (3,NULL,'子部门1',1,'F',NULL,'1.3.','0'),
 (6,'d','ddd12255',3,'F','','1.3.1450082228032.','0'),
 (8,'ddddddd','ddddd',2,'F','','1.2.20151215124553.','0'),
 (9,'vv','vv',2,'F','','1.2.20151215124654.','0'),
 (10,'gg','gg',2,'F','','1.2.20151215124800.','0'),
 (13,'tsttt','tsttt',2,'F','','1.2.20151215144635.','0'),
 (15,'dd','ddd',2,'F','','1.2.20151215145619.','0'),
 (16,'d111','dd1',2,'F','','1.2.20151216195022.','0'),
 (17,'llo','lioa测',1,'F','','1.20151216201821.','0'),
 (18,'pp','pp',1,'F','','1.20151217223416.','0'),
 (19,'vvd','vvd1',1,'F','','1.20151217223422.','0');
/*!40000 ALTER TABLE `t_org` ENABLE KEYS */;


--
-- Table structure for table `ioka`.`t_role`
--

DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(60) NOT NULL,
  `sid` bigint(20) NOT NULL,
  `role_relation_no` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=gbk;

--
-- Dumping data for table `ioka`.`t_role`
--

/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
INSERT INTO `t_role` (`id`,`role_name`,`sid`,`role_relation_no`) VALUES 
 (1,'超级管理员',0,'1.'),
 (2,'测试角色1',1,'1.2.'),
 (3,'测试1',1,'1.20151217223146.');
/*!40000 ALTER TABLE `t_role` ENABLE KEYS */;


--
-- Table structure for table `ioka`.`t_system_param`
--

DROP TABLE IF EXISTS `t_system_param`;
CREATE TABLE `t_system_param` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_name` varchar(60) NOT NULL,
  `param_text` varchar(512) NOT NULL,
  `remark` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=gbk;

--
-- Dumping data for table `ioka`.`t_system_param`
--

/*!40000 ALTER TABLE `t_system_param` DISABLE KEYS */;
INSERT INTO `t_system_param` (`id`,`param_name`,`param_text`,`remark`) VALUES 
 (1,'product_image_url','http://192.168.0.120:8080/kkx/resource/images/product/','产品图片URL'),
 (2,'appuser_image_url','http://127.0.0.1:8080/kkx/upload/product/','用户图片URL'),
 (3,'product_category_image_url','http://192.168.0.120:8080/kkx/resource/images/product/','产品小类图片URL');
/*!40000 ALTER TABLE `t_system_param` ENABLE KEYS */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
