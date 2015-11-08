-- phpMyAdmin SQL Dump
-- version 4.3.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 05. Okt 2015 um 22:58
-- Server-Version: 5.6.24
-- PHP-Version: 5.6.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- CREATE AND GRANT RIGHTS
--

GRANT ALL ON `potatoDB`.* to 'PotatoServer'@'localhost' identified by 'potato';

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `potato`
--

CREATE DATABASE IF NOT EXISTS potatoDB;
USE potatoDB;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `aufgabe`
--

CREATE TABLE IF NOT EXISTS `aufgabe` (
  `aufg_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `aufg_termin`
--

CREATE TABLE IF NOT EXISTS `aufg_termin` (
  `aufg_name` varchar(255) NOT NULL,
  `fromDate` DATE NOT NULL,
  `toDate` DATE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `aufg_beinhaltet_eig`
--

CREATE TABLE IF NOT EXISTS `aufg_beinhaltet_eig` (
  `aufg_name` varchar(255) NOT NULL,
  `eig_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `aufg_gehoert_zu_parz`
--

CREATE TABLE IF NOT EXISTS `aufg_gehoert_zu_parz` (
  `aufg_name` varchar(255) NOT NULL,
  `parz_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `eigenschaft`
--

CREATE TABLE IF NOT EXISTS `eigenschaft` (
  `eig_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `parzellen`
--

CREATE TABLE IF NOT EXISTS `parzellen` (
  `parz_id` int(11) NOT NULL,
  `feld_nr` int(11) NOT NULL,
  `sorte` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `sorte`
--

CREATE TABLE IF NOT EXISTS `sorte` (
  `sort_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `aufgabe`
--
ALTER TABLE `aufgabe`
  ADD PRIMARY KEY (`aufg_name`);
  
--
-- Indizes für die Tabelle `aufgabe`
--
ALTER TABLE `aufg_termin`
  ADD PRIMARY KEY (`aufg_name`, `fromDate`, `toDate`);

--
-- Indizes für die Tabelle `aufg_beinhaltet_eig`
--
ALTER TABLE `aufg_beinhaltet_eig`
  ADD PRIMARY KEY (`aufg_name`,`eig_name`), ADD KEY `eig_name` (`eig_name`);

--
-- Indizes für die Tabelle `aufg_gehoert_zu_parz`
--
ALTER TABLE `aufg_gehoert_zu_parz`
  ADD PRIMARY KEY (`aufg_name`,`parz_id`), ADD KEY `parz_id` (`parz_id`);

--
-- Indizes für die Tabelle `eigenschaft`
--
ALTER TABLE `eigenschaft`
  ADD PRIMARY KEY (`eig_name`);

--
-- Indizes für die Tabelle `parzellen`
--
ALTER TABLE `parzellen`
  ADD PRIMARY KEY (`parz_id`), ADD KEY `sorte` (`sorte`);

--
-- Indizes für die Tabelle `sorte`
--
ALTER TABLE `sorte`
  ADD PRIMARY KEY (`sort_name`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `parzellen`
--
ALTER TABLE `parzellen`
  MODIFY `parz_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `aufg_termin`
--
ALTER TABLE `aufg_termin`
ADD CONSTRAINT `aufg_termin_ibfk_1` FOREIGN KEY (`aufg_name`) REFERENCES `aufgabe` (`aufg_name`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints der Tabelle `aufg_beinhaltet_eig`
--
ALTER TABLE `aufg_beinhaltet_eig`
ADD CONSTRAINT `aufg_beinhaltet_eig_ibfk_1` FOREIGN KEY (`aufg_name`) REFERENCES `aufgabe` (`aufg_name`) ON DELETE NO ACTION ON UPDATE CASCADE,
ADD CONSTRAINT `aufg_beinhaltet_eig_ibfk_2` FOREIGN KEY (`eig_name`) REFERENCES `eigenschaft` (`eig_name`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints der Tabelle `aufg_gehoert_zu_parz`
--
ALTER TABLE `aufg_gehoert_zu_parz`
ADD CONSTRAINT `aufg_gehoert_zu_parz_ibfk_1` FOREIGN KEY (`aufg_name`) REFERENCES `aufgabe` (`aufg_name`) ON DELETE NO ACTION ON UPDATE CASCADE,
ADD CONSTRAINT `aufg_gehoert_zu_parz_ibfk_2` FOREIGN KEY (`parz_id`) REFERENCES `parzellen` (`parz_id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints der Tabelle `parzellen`
--
ALTER TABLE `parzellen`
ADD CONSTRAINT `parzellen_ibfk_1` FOREIGN KEY (`sorte`) REFERENCES `sorte` (`sort_name`) ON DELETE SET NULL ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

--
-- Stored Procedures hinzuf�gen
--
CREATE PROCEDURE `insertEigenschaft`(
    IN `name` VARCHAR(255) CHARSET utf8
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO eigenschaft (eig_name) VALUES (name); 

CREATE PROCEDURE `insertSorte`(
    IN `name` VARCHAR(255) CHARSET utf8
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO sorte (sort_name) VALUES (name); 

CREATE PROCEDURE `insertParzelle`(
    IN `id` INTEGER,
    IN `nr` INTEGER,
    IN `sorte` VARCHAR(255) CHARSET utf8
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO parzellen (parz_id, feld_nr, sorte) VALUES (id, nr, sorte); 

CREATE PROCEDURE `insertAufgabe`(
    IN `name` VARCHAR(255) CHARSET utf8
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO aufgabe (aufg_name) VALUES (`name`); 

CREATE PROCEDURE `insertAufg_termin`(
	IN `name` VARCHAR(255) CHARSET utf8,
	IN `f` DATE,
	IN `t` DATE
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO aufg_termin (aufg_name, fromDate, toDate) VALUES (name, f, t); 

CREATE PROCEDURE `insertAufg_beinhaltet_eig`(
    IN `name1` VARCHAR(255) CHARSET utf8,
    IN `name2` VARCHAR(255) CHARSET utf8
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO aufg_beinhaltet_eig (`aufg_name`, `eig_name`) VALUES (name1, name2); 

CREATE PROCEDURE `insertAufg_gehoert_zu_parz`(
    IN `name1` VARCHAR(255) CHARSET utf8,
    IN `pid` INTEGER
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO aufg_gehoert_zu_parz (`aufg_name`, `parz_id`) VALUES (name1, pid); 

CREATE PROCEDURE `selectParzellen`() NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER SELECT * FROM parzellen;

CREATE PROCEDURE `selectEigenschaft`() NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER SELECT * FROM eigenschaft;

CREATE PROCEDURE `selectAufgabe`() NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER SELECT * FROM aufgabe;

CREATE PROCEDURE `selectEigenschaftByAufgabe`(IN `aufgabenName` VARCHAR(255))
    NO SQL
SELECT eig_name FROM aufg_beinhaltet_eig
WHERE aufg_name = aufgabenName;

CREATE PROCEDURE `selectDateByAufgabe`(IN `aufgabenName` VARCHAR(255))
    NO SQL
SELECT fromDate, toDate
FROM aufg_termin
WHERE aufg_name = aufgabenName
ORDER BY fromDate ASC;

CREATE PROCEDURE `selectAufgabeByEigenschaft` ( IN `eigName` VARCHAR( 255 ) CHARSET utf8 ) NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER SELECT DISTINCT aufg_name
FROM aufg_beinhaltet_eig
WHERE eig_name = eigName;