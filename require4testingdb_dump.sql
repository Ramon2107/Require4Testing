/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-12.1.2-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: require4testingdb
-- ------------------------------------------------------
-- Server version	12.1.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `requirements`
--

DROP TABLE IF EXISTS `requirements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `requirements` (
  `created_at` datetime(6) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) NOT NULL,
  `status` varchar(50) NOT NULL,
  `name` varchar(500) NOT NULL,
  `description` text DEFAULT NULL,
  `readable_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsw3t4wqg3lono3dgtdst6xkew` (`readable_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requirements`
--

LOCK TABLES `requirements` WRITE;
/*!40000 ALTER TABLE `requirements` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `requirements` VALUES
('2026-02-27 20:34:23.000000',4,1,'2026-02-27 20:34:23.000000','IN_PROGRESS','Registrierung & Login','Kundenkonto-Verwaltung.','REQ-2026-001'),
('2026-02-27 20:34:23.000000',4,2,'2026-02-27 20:34:23.000000','SUCCESSFUL','Produktsuche & Filter','Produkte finden.','REQ-2026-002'),
('2026-02-27 20:34:23.000000',4,3,'2026-02-27 20:34:23.000000','FAILED','Warenkorb','Verwaltung der Artikel.','REQ-2026-003'),
('2026-02-27 20:34:23.000000',4,4,'2026-02-27 20:34:23.000000','IN_PROGRESS','Checkout & Zahlung','Bestellabschluss.','REQ-2026-004'),
('2026-02-27 20:34:23.000000',4,5,'2026-02-27 20:34:23.000000','PLANNING','Retouren & Erstattung','Rückgabe-Prozess.','REQ-2026-005');
/*!40000 ALTER TABLE `requirements` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `test_case_test_run`
--

DROP TABLE IF EXISTS `test_case_test_run`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_case_test_run` (
  `executed_at` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_case_id` bigint(20) NOT NULL,
  `test_run_id` bigint(20) NOT NULL,
  `tester_id` bigint(20) DEFAULT NULL,
  `notes` text DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKam5m9ovmn3sojppm87gnhrann` (`test_case_id`),
  KEY `FK5df0ey72nwl91g3ri0ji63ska` (`test_run_id`),
  KEY `FK65xw47ut7h4358nynw7gh9wj4` (`tester_id`),
  CONSTRAINT `FK5df0ey72nwl91g3ri0ji63ska` FOREIGN KEY (`test_run_id`) REFERENCES `test_runs` (`id`),
  CONSTRAINT `FK65xw47ut7h4358nynw7gh9wj4` FOREIGN KEY (`tester_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKam5m9ovmn3sojppm87gnhrann` FOREIGN KEY (`test_case_id`) REFERENCES `test_cases` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_case_test_run`
--

LOCK TABLES `test_case_test_run` WRITE;
/*!40000 ALTER TABLE `test_case_test_run` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `test_case_test_run` VALUES
('2026-02-27 18:34:23.000000',1,1,1,5,'Registrierung via E-Mail funktioniert.','PASSED'),
('2026-02-27 19:34:23.000000',2,2,1,5,'Login-Dauer unter 200ms.','PASSED'),
(NULL,3,3,1,6,NULL,'ASSIGNED'),
('2026-02-26 20:34:23.000000',4,4,2,5,'Volltextsuche findet alles.','PASSED'),
('2026-02-27 20:34:23.000000',5,10,3,5,'BUG: Warenkorb leer nach Login!','FAILED'),
(NULL,7,11,4,3,NULL,'ASSIGNED');
/*!40000 ALTER TABLE `test_case_test_run` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `test_cases`
--

DROP TABLE IF EXISTS `test_cases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_cases` (
  `created_at` datetime(6) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `requirement_id` bigint(20) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `name` varchar(500) NOT NULL,
  `description` text DEFAULT NULL,
  `readable_id` varchar(255) NOT NULL,
  `test_steps` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7srdgl2rqjhgy38hshgus7xju` (`readable_id`),
  KEY `FKbex52v5e8qob9iytsk9gjq66c` (`requirement_id`),
  CONSTRAINT `FKbex52v5e8qob9iytsk9gjq66c` FOREIGN KEY (`requirement_id`) REFERENCES `requirements` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_cases`
--

LOCK TABLES `test_cases` WRITE;
/*!40000 ALTER TABLE `test_cases` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `test_cases` VALUES
('2026-02-27 20:34:23.000000',3,1,1,'2026-02-27 20:34:23.000000','Registrierung validieren','Pflichtfelder prüfen.','TC-001','1. Seite öffnen\n2. Felder füllen\n3. Senden'),
('2026-02-27 20:34:23.000000',3,2,1,'2026-02-27 20:34:23.000000','Login mit korrektem PW','Erfolgreiche Anmeldung prüfen.','TC-002','1. Login öffnen\n2. Daten eingeben'),
('2026-02-27 20:34:23.000000',3,3,1,'2026-02-27 20:34:23.000000','Login mit falschem PW','Fehlermeldung prüfen.','TC-003','1. Login öffnen\n2. Falsche Daten eingeben'),
('2026-02-27 20:34:23.000000',3,4,2,'2026-02-27 20:34:23.000000','Volltextsuche','Suche nach Produktnamen.','TC-004','1. Suchfeld nutzen\n2. Begriff Sneaker eingeben'),
('2026-02-27 20:34:23.000000',3,5,2,'2026-02-27 20:34:23.000000','Preisfilter','Einschränkung der Preisspanne.','TC-005','1. Kategorie wählen\n2. Filter 50-100 setzen'),
('2026-02-27 20:34:23.000000',3,6,2,'2026-02-27 20:34:23.000000','Kategoriefilter','Filterung nach Marken/Größen.','TC-006','1. Marke wählen\n2. Filter anwenden'),
('2026-02-27 20:34:23.000000',3,7,3,'2026-02-27 20:34:23.000000','Add to Cart','Artikel in den Warenkorb legen.','TC-007','1. Detail öffnen\n2. In den Warenkorb klicken'),
('2026-02-27 20:34:23.000000',3,8,3,'2026-02-27 20:34:23.000000','Menge erhöhen','Anpassung der Stückzahl.','TC-008','1. Korb öffnen\n2. Menge auf 2 setzen'),
('2026-02-27 20:34:23.000000',3,9,3,'2026-02-27 20:34:23.000000','Remove from Cart','Artikel entfernen.','TC-009','1. Korb öffnen\n2. Entfernen klicken'),
('2026-02-27 20:34:23.000000',3,10,3,'2026-02-27 20:34:23.000000','Warenkorb-Persistenz','Inhalt bleibt nach Login erhalten.','TC-010','1. Gastkorb füllen\n2. Einloggen'),
('2026-02-27 20:34:23.000000',3,11,4,'2026-02-27 20:34:23.000000','Adressvalidierung','Prüfung der Lieferadresse.','TC-011','1. Checkout\n2. Adresse füllen'),
('2026-02-27 20:34:23.000000',3,12,4,'2026-02-27 20:34:23.000000','PayPal Redirect','Weiterleitung zum Zahlungsanbieter.','TC-012','1. Checkout bis Zahlung\n2. PayPal wählen'),
('2026-02-27 20:34:23.000000',3,13,4,'2026-02-27 20:34:23.000000','Kreditkartenzahlung','Eingabe von Test-CC-Daten.','TC-013','1. Checkout bis Zahlung\n2. CC Daten füllen'),
('2026-02-27 20:34:23.000000',3,14,4,'2026-02-27 20:34:23.000000','Zahlung abgelehnt','Handling von CC Fehlern.','TC-014','1. Decline Karte nutzen'),
('2026-02-27 20:34:23.000000',3,15,4,'2026-02-27 20:34:23.000000','Checkout ohne Adresse','Pflichtfeldvalidierung Checkout.','TC-015','1. Checkout ohne Adresse starten');
/*!40000 ALTER TABLE `test_cases` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `test_results`
--

DROP TABLE IF EXISTS `test_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_results` (
  `created_at` datetime(6) NOT NULL,
  `executed_at` datetime(6) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_case_id` bigint(20) NOT NULL,
  `test_run_id` bigint(20) NOT NULL,
  `tester_id` bigint(20) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `status` varchar(50) NOT NULL,
  `notes` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_results`
--

LOCK TABLES `test_results` WRITE;
/*!40000 ALTER TABLE `test_results` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `test_results` VALUES
('2026-02-27 20:34:23.000000','2026-02-27 20:34:23.000000',1,10,3,5,'2026-02-27 20:34:23.000000','FAILED','BUG: Warenkorb leer nach Login!');
/*!40000 ALTER TABLE `test_results` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `test_runs`
--

DROP TABLE IF EXISTS `test_runs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_runs` (
  `created_at` datetime(6) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `requirement_id` bigint(20) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `status` varchar(50) NOT NULL,
  `name` varchar(500) NOT NULL,
  `description` text DEFAULT NULL,
  `readable_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKbv2hrjwfy9tfcrg81gg512q78` (`readable_id`),
  KEY `FKh08n5upfh6bqxoy6pnavj8qwn` (`requirement_id`),
  CONSTRAINT `FKh08n5upfh6bqxoy6pnavj8qwn` FOREIGN KEY (`requirement_id`) REFERENCES `requirements` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_runs`
--

LOCK TABLES `test_runs` WRITE;
/*!40000 ALTER TABLE `test_runs` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `test_runs` VALUES
('2026-02-27 20:34:23.000000',2,1,1,'2026-02-27 20:34:23.000000','IN_PROGRESS','Smoke Test Login','Basis-Checks.','TR-2026-0001'),
('2026-02-27 20:34:23.000000',2,2,2,'2026-02-27 20:34:23.000000','SUCCESSFUL','Regression Suche','Suche & Filter.','TR-2026-0002'),
('2026-02-27 20:34:23.000000',2,3,3,'2026-02-27 20:34:23.000000','FAILED','Warenkorb Kern','Handling & Logik.','TR-2026-0003'),
('2026-02-27 20:34:23.000000',2,4,4,'2026-02-28 20:43:50.347773','IN_PROGRESS','Checkout E2E','Vollständiger Kauf.','TR-2026-0004');
/*!40000 ALTER TABLE `test_runs` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `created_at` datetime(6) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) NOT NULL,
  `role` varchar(50) NOT NULL,
  `username` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `users` VALUES
('2026-02-27 20:34:23.000000',1,'2026-02-27 20:34:23.000000','ADMIN','Admin','admin@require4testing.local','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('2026-02-27 20:34:23.000000',2,'2026-02-27 20:34:23.000000','MANAGER','Nina','nina.mgmt@require4testing.local','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('2026-02-27 20:34:23.000000',3,'2026-02-27 20:34:23.000000','CREATOR','Luca','luca.tc@require4testing.local','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('2026-02-27 20:34:23.000000',4,'2026-02-27 20:34:23.000000','REQUIREMENT_ENGINEER','Mina','mina.req@require4testing.local','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('2026-02-27 20:34:23.000000',5,'2026-02-27 20:34:23.000000','TESTER','Timo','timo.test@require4testing.local','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('2026-02-27 20:34:23.000000',6,'2026-02-27 20:34:23.000000','TESTER','Julia','julia.test@require4testing.local','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
commit;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2026-02-28 22:11:53
