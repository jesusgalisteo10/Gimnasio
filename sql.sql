CREATE DATABASE  IF NOT EXISTS `gimnasio` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gimnasio`;
-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: gimnasio
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `ClientCode` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `Surname` varchar(50) NOT NULL,
  `Email` varchar(50) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `DNI` char(9) NOT NULL,
  `Sex` varchar(20) NOT NULL,
  PRIMARY KEY (`ClientCode`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (36,'javi','guerrero','javi@gmail.com','ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f','12345678k','masculino'),(37,'jesus','galisteo','jesus@gmail.com','ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f','12345567T','masculino'),(38,'Juan','Heredia','juan@gmail.com','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','12345678F','masculino'),(39,'rafa','quiles','rafa@gmail.com','ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f','12345678D','masculino');
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_machine`
--

DROP TABLE IF EXISTS `client_machine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client_machine` (
  `ClientCode` int NOT NULL,
  `MachineCode` int NOT NULL,
  PRIMARY KEY (`ClientCode`,`MachineCode`),
  KEY `client_machine_ibfk_2` (`MachineCode`),
  CONSTRAINT `client_machine_ibfk_1` FOREIGN KEY (`ClientCode`) REFERENCES `client` (`ClientCode`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `client_machine_ibfk_2` FOREIGN KEY (`MachineCode`) REFERENCES `machine` (`MachineCode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_machine`
--

LOCK TABLES `client_machine` WRITE;
/*!40000 ALTER TABLE `client_machine` DISABLE KEYS */;
INSERT INTO `client_machine` VALUES (39,26),(39,29),(39,31);
/*!40000 ALTER TABLE `client_machine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `machine`
--

DROP TABLE IF EXISTS `machine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `machine` (
  `MachineCode` int NOT NULL AUTO_INCREMENT,
  `RoomCode` int DEFAULT NULL,
  `MachineType` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`MachineCode`),
  KEY `machine_ibfk_1` (`RoomCode`),
  CONSTRAINT `machine_ibfk_1` FOREIGN KEY (`RoomCode`) REFERENCES `room` (`RoomCode`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `machine`
--

LOCK TABLES `machine` WRITE;
/*!40000 ALTER TABLE `machine` DISABLE KEYS */;
INSERT INTO `machine` VALUES (26,3,'pesas'),(27,1,'hombro'),(29,2,'biceps'),(30,20,'triceps'),(31,11,'cuello'),(33,22,'press militar');
/*!40000 ALTER TABLE `machine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `RoomCode` int NOT NULL,
  PRIMARY KEY (`RoomCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1),(2),(3),(4),(6),(10),(11),(12),(15),(18),(20),(22),(24),(25),(55),(88),(97),(99);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-22 12:24:16
