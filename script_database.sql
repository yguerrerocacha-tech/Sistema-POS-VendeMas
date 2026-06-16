-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: db_goodmarket
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `dni` char(8) NOT NULL,
  `nombre_completo` varchar(100) NOT NULL,
  PRIMARY KEY (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES ('10234567','Pedro Alonso Guerrero Morales'),('40987654','Ricardo Smith Cacha Quispe'),('45678912','María Alejandra Torres Silva'),('46758493','Jorge Luis Villanueva Vega'),('71234567','Juan Carlos Mendoza Prado'),('71980912','Yhor'),('73210984','Claudia Fiorella Ramos Diaz'),('75643219','Ana Sofía Benavides Castro');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_venta`
--

DROP TABLE IF EXISTS `detalle_venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_venta` (
  `id_detalle` int NOT NULL AUTO_INCREMENT,
  `id_venta` int DEFAULT NULL,
  `id_producto` int DEFAULT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_detalle`),
  KEY `id_venta` (`id_venta`),
  KEY `id_producto` (`id_producto`),
  CONSTRAINT `detalle_venta_ibfk_1` FOREIGN KEY (`id_venta`) REFERENCES `venta` (`id_venta`) ON DELETE CASCADE,
  CONSTRAINT `detalle_venta_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_venta`
--

LOCK TABLES `detalle_venta` WRITE;
/*!40000 ALTER TABLE `detalle_venta` DISABLE KEYS */;
INSERT INTO `detalle_venta` VALUES (1,1,12,12,5.50),(2,1,4,1,6.50),(3,2,2,1,4.20);
/*!40000 ALTER TABLE `detalle_venta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historial_abastecimiento`
--

DROP TABLE IF EXISTS `historial_abastecimiento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historial_abastecimiento` (
  `id_historial` int NOT NULL AUTO_INCREMENT,
  `id_producto` int DEFAULT NULL,
  `ruc_proveedor` varchar(11) DEFAULT NULL,
  `cantidad_paquetes` int NOT NULL,
  `unidades_por_paquete` int NOT NULL,
  `stock_anterior` int NOT NULL,
  `stock_nuevo` int NOT NULL,
  `fecha_hora` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_historial`),
  KEY `id_producto` (`id_producto`),
  KEY `ruc_proveedor` (`ruc_proveedor`),
  CONSTRAINT `historial_abastecimiento_ibfk_1` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`),
  CONSTRAINT `historial_abastecimiento_ibfk_2` FOREIGN KEY (`ruc_proveedor`) REFERENCES `proveedores` (`ruc`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historial_abastecimiento`
--

LOCK TABLES `historial_abastecimiento` WRITE;
/*!40000 ALTER TABLE `historial_abastecimiento` DISABLE KEYS */;
INSERT INTO `historial_abastecimiento` VALUES (1,10,NULL,1,6,45,51,'2026-06-16 11:55:49'),(2,11,NULL,2,6,25,37,'2026-06-16 11:56:28'),(4,11,NULL,2,6,37,49,'2026-06-16 13:51:50'),(5,2,'20100058623',10,6,40,100,'2026-06-14 13:55:32'),(6,3,'20100192765',5,6,0,30,'2026-06-15 13:55:32'),(7,11,'20100058623',4,6,1,25,'2026-06-16 08:55:32'),(8,17,'20100058623',5,6,0,30,'2026-06-16 11:55:32'),(9,18,'20100192765',8,6,2,50,'2026-06-16 13:55:32'),(10,37,'20100192765',2,6,70,82,'2026-06-16 13:55:48');
/*!40000 ALTER TABLE `historial_abastecimiento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id_producto` int NOT NULL AUTO_INCREMENT,
  `nombre_producto` varchar(100) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `stock` int NOT NULL DEFAULT '0',
  `imagen_ruta` varchar(255) DEFAULT NULL,
  `proveedor_ruc` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id_producto`),
  UNIQUE KEY `nombre_producto` (`nombre_producto`),
  KEY `fk_productos_proveedores` (`proveedor_ruc`),
  CONSTRAINT `fk_productos_proveedores` FOREIGN KEY (`proveedor_ruc`) REFERENCES `proveedores` (`ruc`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,'Arroz Costeño Gran Reserva 1kg',4.50,50,'aceite_deleite.png',NULL),(2,'Leche Gloria Azul 400g',4.20,99,'leche_gloria.png',NULL),(3,'Aceite Primor Premium 1L',11.50,30,'atun_campomar.png',NULL),(4,'Gaseosa Inka Kola 1.5L',6.50,79,'inka_kola.png',NULL),(5,'Detergente Opal Ultra 750g',8.90,40,'detergente_opal.png',NULL),(6,'Atún Campomar Trozos',5.80,121,'atun_campomar.png',NULL),(7,'Galletas Soda Field Pack',3.50,120,'soda_field.png',NULL),(8,'Pan de Molde Bimbo Grande',10.50,20,'pan_bimbo.png',NULL),(9,'Fideos Don Vittorio 500g',3.80,60,'fideos_vittorio.png',NULL),(10,'Mayonesa Alacena 190g',4.50,51,'alacena.png',NULL),(11,'Yogurt Gloria Fresa 1L',6.20,49,'yogurt_fresa.png',NULL),(12,'Papel Higiénico Elite 4un',5.50,18,'papel_elite.png',NULL),(13,'Shampoo hs',3.00,45,'shampoo_hs.png',NULL),(14,'Producto Fantasma',10.00,5,'atun_gloria.png',NULL),(16,'Paneton Gloria',25.00,15,'paneton_gloria.png',NULL),(17,'Mantequilla Gloria',4.50,30,'mantequilla_gloria.png',NULL),(18,'Chocolate Sublime 30g',2.50,5,'Sublmen pequeño.jpg','20100058623'),(19,'Arroz Costeño Saco 5kg',21.50,40,'arroz_costeno.png','20100192765'),(20,'Aceite Deleite Premium 1L',8.50,60,'aceite_deleite.png','20100192765'),(21,'Azúcar Rubia Cartavio 1kg',4.50,80,'azucar_cartavio.png','20100130211'),(22,'Fideos Lavaggi Espagueti 500g',3.20,100,'fideos_lavaggi.png','20100192765'),(23,'Lentejas Costeño Bolsa 500g',5.20,50,'lentejas_costeno.png','20100192765'),(24,'Salsa de Tomate Pomarola 150g',2.80,90,'pomarola.png','20100192765'),(25,'Leche Gloria Azul Six Pack',23.50,20,'leche_azul_six.png','20100058623'),(26,'Yogurt Gloria Vainilla 1L',6.50,45,'yogurt_vainilla.png','20100058623'),(27,'Queso Bonlé Familiar 400g',13.50,25,'queso_bonle.png','20100058623'),(28,'Leche Evaporada Light Gloria',4.50,70,'leche_light.png','20100058623'),(29,'Yogurt Gloria Durazno 1L',6.20,30,'yogurt_durazno.png','20100058623'),(30,'Gaseosa Coca Cola 1.5L',7.20,65,'cocacola_15.png','20100130211'),(31,'Gaseosa Inca Kola 3L',11.50,50,'incakola_3l.png','20100130211'),(32,'Agua San Luis sin Gas 2.5L',3.80,80,'agua_sanluis.png','20100130211'),(33,'Frugos del Valle Durazno 1L',4.80,55,'frugos_durazno.png','20100130211'),(34,'Refresco Negrita Chicha Sobre',1.20,150,'chicha_negrita.png','20100192765'),(35,'Bebida Energizante Volt Maca',2.50,100,'volt_maca.png','20100130211'),(36,'Detergente Ariel Toque Downy 2.8kg',32.50,15,'detergente_ariel.png','20501234567'),(37,'Lavavajillas Ayudín Limón 400g',4.80,82,'ayudin_limon.png','20100192765'),(38,'Suavizante Bolivar Flores 900ml',9.50,35,'suavizante_bolivar.png','20100192765'),(39,'Limpiador Poett Primavera 900ml',5.20,48,'poett_primavera.png','20501234567'),(40,'Esponja Verde Scotch-Brite 2un',3.50,110,'esponja_scotch.png','20100130211'),(41,'Galletas Casino Menta Pack x6',4.20,85,'casino_menta.png','20100192765'),(42,'Papa Natuchips Picante Bolsa',3.50,60,'natuchips.png','20100130211'),(43,'Chocolate Snickers Bar 50g',3.80,95,'snickers.png','20100130211'),(44,'Cereal Ángel Flakes Caja 500g',12.80,30,'cereal_angel.png','20100192765'),(45,'Chupetín Globe Pop Paquete x24',6.50,40,'globe_pop.png','20100130211');
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedores`
--

DROP TABLE IF EXISTS `proveedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedores` (
  `ruc` varchar(11) NOT NULL,
  `razon_social` varchar(150) NOT NULL,
  `telefono` varchar(9) DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ruc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedores`
--

LOCK TABLES `proveedores` WRITE;
/*!40000 ALTER TABLE `proveedores` DISABLE KEYS */;
INSERT INTO `proveedores` VALUES ('20100058623','GLORIA S.A.','987654321','Av. Republica de Panama 2461'),('20100130211','DISTRIBUIDORA DE BEBIDAS LIMA S.A.','015551234','Av. Los Olivos 1040, Los Olivos'),('20100192765','ALICORP S.A.A.','912345678','Av. Argentina 4793'),('20501234567','PROCTER & GAMBLE PERU S.R.L.','016111000','Av. Driving 150, Santiago de Surco');
/*!40000 ALTER TABLE `proveedores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `rol` enum('ADMINISTRADOR','CAJERO') NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Yhor','1234','ADMINISTRADOR'),(2,'Brigith','240320','CAJERO'),(3,'Lennin','861169','CAJERO'),(4,'CarlosAdmin','adm2026','ADMINISTRADOR'),(5,'DanielaCaja','98765','CAJERO'),(6,'EstebanCaja','54321','CAJERO'),(7,'Carlos','240320','CAJERO');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `venta`
--

DROP TABLE IF EXISTS `venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `venta` (
  `id_venta` int NOT NULL AUTO_INCREMENT,
  `cliente` varchar(100) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `descuento` decimal(10,2) NOT NULL,
  `total_pagado` decimal(10,2) NOT NULL,
  `fecha_hora` datetime DEFAULT CURRENT_TIMESTAMP,
  `cajero` varchar(50) NOT NULL DEFAULT 'Yhor',
  `turno` varchar(20) NOT NULL DEFAULT 'MAÑANA',
  `metodo_pago` enum('EFECTIVO','YAPE','PLIN','TARJETA') NOT NULL DEFAULT 'EFECTIVO',
  PRIMARY KEY (`id_venta`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venta`
--

LOCK TABLES `venta` WRITE;
/*!40000 ALTER TABLE `venta` DISABLE KEYS */;
INSERT INTO `venta` VALUES (1,'Yhor',72.50,0.72,71.78,'2026-06-16 13:26:53','Yhor','MAÑANA','EFECTIVO'),(2,'Público General',4.20,0.04,4.16,'2026-06-16 13:40:25','Carlos','TARDE','TARJETA');
/*!40000 ALTER TABLE `venta` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-16 14:02:01
