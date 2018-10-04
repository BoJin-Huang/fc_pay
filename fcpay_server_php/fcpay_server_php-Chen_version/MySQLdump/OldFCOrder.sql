-- MySQL dump 10.13  Distrib 5.7.21, for Linux (x86_64)
--
-- Host: localhost    Database: FCOrder
-- ------------------------------------------------------
-- Server version	5.7.21-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accountList`
--

DROP TABLE IF EXISTS `accountList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accountList` (
  `account_id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(30) NOT NULL,
  `password` char(64) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `nid` varchar(10) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `mobile` varchar(15) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accountList`
--

LOCK TABLES `accountList` WRITE;
/*!40000 ALTER TABLE `accountList` DISABLE KEYS */;
INSERT INTO `accountList` VALUES (1,'test001','7b81014bf034f9a6d7b676ef7ab990984f3277ee33ea93bb49efc25287d79875','黃聖堯','D0341522','1996-09-10',NULL,NULL),(2,'test002','0d84de13f19fb6055df810eebb2e2eb2bbbecd3a674df0d28be185566c642731','汪尚霆','D0342567','1996-01-11',NULL,NULL),(3,'OAuth01',NULL,NULL,NULL,NULL,NULL,NULL),(6,'T123456789',NULL,NULL,NULL,NULL,NULL,NULL),(7,'D0123456',NULL,NULL,NULL,NULL,NULL,NULL),(8,'D0449763',NULL,NULL,NULL,NULL,NULL,NULL),(9,'T03291',NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `accountList` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderDetailList`
--

DROP TABLE IF EXISTS `orderDetailList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderDetailList` (
  `orderID` int(11) NOT NULL,
  `productID` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderDetailList`
--

LOCK TABLES `orderDetailList` WRITE;
/*!40000 ALTER TABLE `orderDetailList` DISABLE KEYS */;
INSERT INTO `orderDetailList` VALUES (2,1,1),(3,4,1),(4,6,2),(4,10,1),(5,4,1),(6,1,3),(6,4,4),(6,6,1),(7,3,1),(8,3,1),(9,1,1),(10,3,1),(11,10,1),(12,10,1),(13,10,1),(14,10,1),(15,10,1),(16,10,1),(17,10,1),(18,10,1),(19,2,1),(20,10,1),(21,10,1),(22,10,1),(23,10,1),(24,10,1),(25,10,1),(26,10,1),(27,3,1),(28,10,1),(29,8,3),(29,10,2),(30,1,2),(30,5,1),(31,10,1),(32,6,2),(32,9,1),(32,10,1),(33,6,2),(33,9,1),(33,10,1),(34,4,1),(35,10,1),(36,10,1),(37,10,1),(38,3,1),(39,9,1),(40,10,1),(41,10,2),(42,10,2),(43,9,1),(44,10,2),(45,5,1),(46,3,1),(47,5,1),(48,3,1),(49,3,5),(50,6,1),(50,8,2),(51,9,1),(52,3,1),(52,7,1),(52,9,1),(53,4,9),(54,10,1),(55,3,1),(55,4,4),(56,5,1),(57,10,1),(58,10,3),(59,10,3),(60,10,4),(61,10,2),(62,10,3),(63,10,3),(64,10,1),(65,1,1),(66,1,1),(67,1,1),(68,1,1),(69,1,1),(70,1,1),(71,1,1),(72,10,1),(73,10,1),(74,10,1),(75,1,1),(76,1,1);
/*!40000 ALTER TABLE `orderDetailList` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderList`
--

DROP TABLE IF EXISTS `orderList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderList` (
  `orderID` int(11) NOT NULL AUTO_INCREMENT,
  `memberID` varchar(30) NOT NULL,
  `total_price` int(11) NOT NULL,
  `location` varchar(20) NOT NULL,
  `memo` varchar(20) DEFAULT NULL,
  `orderDate` char(15) NOT NULL,
  `client_paid` int(11) NOT NULL DEFAULT '0',
  `server_paid` int(11) NOT NULL DEFAULT '0',
  `email_paid` int(11) NOT NULL DEFAULT '0',
  `pickup` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`orderID`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderList`
--

LOCK TABLES `orderList` WRITE;
/*!40000 ALTER TABLE `orderList` DISABLE KEYS */;
INSERT INTO `orderList` VALUES (2,'test001',100,'1','','20171026121151',1,0,0,0),(3,'test001',120,'1','','20171026123941',1,27637,0,0),(4,'test001',210,'1','','20171026143830',27638,27638,0,1),(5,'test001',120,'1','','20171026144004',0,0,0,0),(6,'test001',880,'1','','20171102152623',27648,27648,0,1),(7,'test001',80,'1','','20171107164530',27657,27657,0,0),(8,'test001',80,'1','','20171115230647',27670,27670,0,0),(9,'test001',100,'1','','20171116110708',27671,27671,0,1),(10,'test002',80,'1','','20171116110850',27672,27672,0,1),(11,'test001',10,'1','','20171116152310',0,0,0,0),(12,'test001',10,'1','','20171116153025',0,0,0,0),(13,'test001',10,'1','','20171116153352',0,0,0,0),(14,'test001',10,'1','','20171116154921',27674,27674,0,0),(15,'test001',10,'1','','20171116155101',27675,27675,0,0),(16,'test001',10,'1','','20171116162105',0,0,0,0),(17,'test001',10,'1','','20171116162155',0,0,0,0),(18,'test001',10,'1','','20171116162346',0,0,0,0),(19,'test001',100,'1','','20171122184516',0,0,0,0),(20,'test001',10,'1','','20171123152801',0,0,0,0),(21,'test001',10,'1','','20171123153142',0,0,0,0),(22,'test001',10,'1','','20171123153426',0,0,0,0),(23,'test001',10,'1','','20171123153608',27680,27680,0,0),(24,'test001',10,'1','','20171123153936',27681,27681,0,0),(25,'test001',10,'1','','20171123154416',27682,27682,0,1),(26,'test001',10,'1','','20171123154542',27683,27683,0,0),(27,'test001',80,'1','','20171123154650',27684,27684,0,0),(28,'test001',10,'1','','20171123154927',27685,27685,0,0),(29,'test001',320,'4','','20171123172309',0,0,0,0),(30,'test001',300,'1','','20171123174514',0,0,0,0),(31,'test001',10,'1','','20171129231729',27711,27711,0,1),(32,'test001',360,'1','','20171130180115',0,0,0,0),(33,'test001',360,'1','','20171130180141',0,0,0,0),(34,'test002',120,'1','','20171205124447',27736,27736,0,0),(35,'test001',10,'1','','20171206142220',27762,27762,0,0),(36,'test001',10,'1','','20171206205244',0,0,0,0),(37,'test001',10,'1','','20171207161504',0,0,0,0),(38,'test001',80,'1','','20171207162554',0,0,0,0),(39,'test001 ',150,'1','','20171207164604',0,0,0,0),(40,'test001',10,'1','','20171208072206',27772,27772,0,0),(41,'test002 ',20,'1','','20171208090908',0,0,0,0),(42,'test002 ',20,'1','','20171208090942',27774,27774,0,0),(43,'test001',150,'1','','20171208114903',27776,27776,0,1),(44,'test001 ',20,'4','','20171208133453',0,0,0,0),(45,'test001 ',100,'1','','20171208134822',27777,27777,0,0),(46,'test001 ',80,'1','','20171208135452',0,0,0,0),(47,'test001 ',100,'1','','20171208135518',0,0,0,0),(48,'test001 ',80,'1','','20171208140408',27778,27778,0,0),(49,'test001 ',400,'1','','20171208141639',27779,27779,0,0),(50,'test001 ',300,'3','','20171208142928',0,0,0,0),(51,'test001 ',150,'1','','20171208144933',27780,27780,0,0),(52,'test001',330,'1','','20171208152828',0,0,0,0),(53,'test001',1080,'1','','20171208155452',27781,27781,0,0),(54,'T03291',10,'1','','20180118125620',0,0,0,0),(55,'D0123456',560,'1','','20180119140148',28146,28146,0,0),(56,'T03291',100,'1','','20180131142118',0,0,0,0),(57,'T03291',10,'1','','20180208200848',28335,28335,0,0),(58,'T03291',30,'1','','20180208202811',0,0,0,0),(59,'T03291',30,'2','','20180208203219',28336,28336,0,0),(60,'T03291',40,'1','','20180208204813',28337,28337,0,0),(61,'T03291',20,'1','','20180208213644',0,0,0,0),(62,'T03291',30,'1','','20180208220615',28338,28338,0,0),(63,'T03291',30,'1','','20180208221646',0,0,0,0),(64,'T03291',10,'1','','20180208222242',28339,28339,0,0),(65,'T03291',100,'1','','20180212144323',0,0,0,0),(66,'T03291',100,'1','','20180212144418',0,0,0,0),(67,'T03291',100,'1','','20180212144825',0,0,0,0),(68,'T03291',100,'1','','20180212145005',0,0,0,0),(69,'T03291',100,'1','','20180212145101',0,0,0,0),(70,'T03291',100,'1','','20180212145211',0,0,0,0),(71,'T03291',100,'1','','20180212145217',0,0,0,0),(72,'T03291',10,'1','','20180212145241',28374,28374,0,0),(73,'T03291',10,'1','','20180212145245',0,0,0,0),(74,'D0123456',10,'1','','20180308163538',0,0,0,0),(75,'T03291',100,'1','','20180309144712',0,0,0,0),(76,'T03291',100,'0','','20180309220049',0,0,0,0);
/*!40000 ALTER TABLE `orderList` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productList`
--

DROP TABLE IF EXISTS `productList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productList` (
  `productID` int(11) NOT NULL AUTO_INCREMENT,
  `productName` varchar(30) CHARACTER SET utf8 NOT NULL,
  `productPrice` int(11) NOT NULL,
  `ManufacturerID` int(11) NOT NULL,
  `ManufacturerName` varchar(30) CHARACTER SET utf8 NOT NULL,
  `Introduce` varchar(30) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`productID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productList`
--

LOCK TABLES `productList` WRITE;
/*!40000 ALTER TABLE `productList` DISABLE KEYS */;
INSERT INTO `productList` VALUES (1,'雞腿飯',100,1,'快餐便當','出餐快'),(2,'排骨飯',100,1,'快餐便當','出餐快'),(3,'合菜飯',80,1,'快餐便當','出餐快'),(4,'雞腿飯',120,2,'金豹便當店','金豹'),(5,'叉燒飯',100,3,'慶記燒臘','台中名產'),(6,'燒鴨飯',100,3,'慶記燒臘','台中名產'),(7,'油雞飯',100,3,'慶記燒臘','台中名產'),(8,'燒肉飯',100,3,'慶記燒臘','台中名產'),(9,'三寶飯',150,3,'慶記燒臘','台中名產'),(10,'白飯',10,4,'白飯專賣店','單純白飯');
/*!40000 ALTER TABLE `productList` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-13 17:37:26
