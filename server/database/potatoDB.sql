--
-- phpMyAdmin SQL Dump	
-- version 4.3.11
-- http://www.phpmyadmin.net
--
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `aufg_termin`
--

CREATE TABLE IF NOT EXISTS `aufg_termin` (
  `aufg_name` varchar(255) NOT NULL,
  `fromDate` DATE NOT NULL,
  `toDate` DATE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle 'feld'
--

CREATE TABLE IF NOT EXISTS `feld` (
 `feld_id` int(11) NOT NULL AUTO_INCREMENT,
 `row_nr` int(11) NOT NULL,
 `column_nr` int(11) NOT NULL,
 PRIMARY KEY (`feld_id`),
 UNIQUE KEY `row_column_integrity` (`row_nr`,`column_nr`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `aufg_beinhaltet_eig`
--

CREATE TABLE IF NOT EXISTS `aufg_beinhaltet_eig` (
  `aufg_name` varchar(255) NOT NULL,
  `eig_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `aufg_gehoert_zu_parz`
--

CREATE TABLE IF NOT EXISTS `aufg_gehoert_zu_parz` (
  `aufg_name` varchar(255) NOT NULL,
  `parz_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `eigenschaft`
--

CREATE TABLE IF NOT EXISTS `eigenschaft` (
  `eig_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
	
--
-- Tabellenstruktur für Tabelle `sorte`
--

CREATE TABLE IF NOT EXISTS `sorte` (
  `sort_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Indizes für die Tabelle `sorte`
--
ALTER TABLE `sorte`
  ADD PRIMARY KEY (`sort_name`);
  
-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `parzellen`
--

CREATE TABLE IF NOT EXISTS`parzellen` (
 `parz_id` int(11) NOT NULL AUTO_INCREMENT,
 `feld_nr` int(11) NOT NULL,
 `sorte` varchar(255) DEFAULT NULL,
 `parz_row` int(11) NOT NULL,
 `parz_col` int(11) NOT NULL,
 PRIMARY KEY (`parz_id`),
 UNIQUE KEY `row_col_unique` (`parz_row`,`parz_col`,`feld_nr`),
 KEY `sorte` (`sorte`),
 KEY `feld_nr` (`feld_nr`),
 CONSTRAINT `parzellen_ibfk_1` FOREIGN KEY (`sorte`) REFERENCES `sorte` (`sort_name`) ON DELETE SET NULL ON UPDATE CASCADE,
 CONSTRAINT `parzellen_ibfk_2` FOREIGN KEY (`feld_nr`) REFERENCES `feld` (`feld_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

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
-- AUTO_INCREMENT für exportierte Tabellen
--

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

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

--
-- Stored Procedures hinzufügen
--
CREATE PROCEDURE `insertEigenschaft`( IN `name` VARCHAR(255) CHARSET utf8
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO eigenschaft (eig_name) VALUES (name); 

CREATE PROCEDURE `insertSorte`( IN `name` VARCHAR(255) CHARSET utf8
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO sorte (sort_name) VALUES (name); 

CREATE PROCEDURE `insertParzelle`( IN `nr` INT, IN `sorte` VARCHAR(255) CHARSET utf8, IN `rowNr` INT, IN `colNr` INT
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO parzellen( feld_nr, sorte, parz_row, parz_col ) VALUES (nr, sorte, rowNr, colNr);

CREATE PROCEDURE `insertAufgabe`( IN `name` VARCHAR(255) CHARSET utf8
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO aufgabe (aufg_name) VALUES (`name`); 

CREATE PROCEDURE `insertAufg_termin`( IN `name` VARCHAR(255) CHARSET utf8, IN `f` DATE, IN `t` DATE
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO aufg_termin (aufg_name, fromDate, toDate) VALUES (name, f, t); 

CREATE PROCEDURE `insertAufg_beinhaltet_eig`( IN `name1` VARCHAR(255) CHARSET utf8, IN `name2` VARCHAR(255) CHARSET utf8
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO aufg_beinhaltet_eig (`aufg_name`, `eig_name`) VALUES (name1, name2); 

CREATE PROCEDURE `insertAufg_gehoert_zu_parz`( IN `name1` VARCHAR(255) CHARSET utf8, IN `pid` INTEGER
) NOT DETERMINISTIC MODIFIES SQL DATA SQL SECURITY DEFINER 
INSERT INTO aufg_gehoert_zu_parz (`aufg_name`, `parz_id`) VALUES (name1, pid); 

CREATE PROCEDURE `insertFeld` ( IN `rowNr` INT( 11 ) , IN `colNr` INT( 11 ) ) NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER INSERT INTO feld( row_nr, column_nr )
VALUES (
rowNr, colNr
);

--
-- Inserts a new Feld at the end of a given row. Creates the row if its non-existing
--

DROP PROCEDURE IF EXISTS `insertFeldIntoRow`;
DELIMITER //

CREATE PROCEDURE `insertFeldIntoRow`(IN `rowNr` INT UNSIGNED) 
NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER 
BEGIN 
	DECLARE maxCol INT DEFAULT (SELECT MAX(column_nr) FROM feld WHERE row_nr = rowNr); 
	IF(maxCol IS NULL) THEN SET maxCol = 0; END IF; 
	INSERT INTO feld( row_nr, column_nr ) VALUES (rowNr, maxCol + 1); 
END//

DELIMITER ;

--
-- Inserts a new Parzelle at the end of a given (non-)existing row of a given existing field.
--

DROP PROCEDURE IF EXISTS `insertParzelleIntoRow`;
DELIMITER //

CREATE PROCEDURE `insertParzelleIntoRow`(
	IN `feldNr` INT, 
	IN `rowNr` INT UNSIGNED, 
	IN `sortenName` VARCHAR(255))
   NO SQL
BEGIN
   DECLARE maxCol INT DEFAULT (SELECT MAX(parz_col) FROM parzellen WHERE feld_nr = feldNr AND parz_row = rowNr);
   IF(maxCol IS NULL) THEN SET maxCol = 0; END IF;
   IF(sortenName LIKE 'NULL') THEN SET sortenName = NULL; END IF;
   INSERT INTO parzellen( feld_nr, parz_row, parz_col, sorte ) VALUES (feldNr, rowNr, maxCol + 1, sortenName);
END//

DELIMITER ;

--
-- Switches the position of two existing parzellen with the SAME feld_nr
--

CREATE PROCEDURE `selectParzellenByRow`(
	IN `feldNr` INT, 
	IN `rowNr` INT)
   NO SQL
SELECT * 
FROM parzellen 
WHERE feld_nr = feldNr AND parz_row = rowNr 
ORDER BY parz_col ASC;

--
-- Switches the position of two existing parzellen with the SAME feld_nr
--

DROP PROCEDURE IF EXISTS `switchParzellen`;
DELIMITER //

CREATE PROCEDURE `switchParzellen`(IN `parA` INT, IN `parB` INT) 
NO SQL 
BEGIN

	DECLARE parARow INT;
	DECLARE parACol INT;
	DECLARE parBRow INT;
	DECLARE parBCol INT;

	IF NOT EXISTS (SELECT * FROM parzellen WHERE parz_id = parA) THEN
		SIGNAL SQLSTATE '45000';
	END IF;

	IF NOT EXISTS (SELECT * FROM parzellen WHERE parz_id = parB) THEN
		SIGNAL SQLSTATE '45000';
	END IF;

	IF((SELECT feld_nr FROM parzellen WHERE parz_id = parA) != (SELECT feld_nr FROM parzellen WHERE parz_id = parB)) THEN
		SIGNAL SQLSTATE '45000';
	END IF;

	SET parARow = (SELECT parz_row FROM parzellen WHERE parz_id = parA);
	SET parACol = (SELECT parz_col FROM parzellen WHERE parz_id = parA);
	SET parBRow = (SELECT parz_row FROM parzellen WHERE parz_id = parB);
	SET parBCol = (SELECT parz_col FROM parzellen WHERE parz_id = parB);

	UPDATE parzellen SET parz_row = -1, parz_col = -2 WHERE parz_id = parA;

	UPDATE parzellen SET parz_row = parARow, parz_col = parACol WHERE parz_id = parB;

	UPDATE parzellen SET parz_row = parBRow, parz_col = parBCol WHERE parz_id = parA;

END// 

DELIMITER ;

-- --------------------------------------------------------

CREATE PROCEDURE `selectParzellen`() NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER SELECT * FROM parzellen ORDER BY feld_nr, parz_row, parz_col ASC;

CREATE PROCEDURE `selectEigenschaft`() NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER SELECT * FROM eigenschaft;

CREATE PROCEDURE `selectAufgabe`() NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER SELECT * FROM aufgabe;

CREATE PROCEDURE `selectEigenschaftByAufgabe`( IN `aufgabenName` VARCHAR(255))
    NO SQL
SELECT eig_name FROM aufg_beinhaltet_eig
WHERE aufg_name = aufgabenName;

CREATE PROCEDURE `selectDateByAufgabe`( IN `aufgabenName` VARCHAR(255))
    NO SQL
SELECT fromDate, toDate
FROM aufg_termin
WHERE aufg_name = aufgabenName
ORDER BY fromDate ASC;

CREATE PROCEDURE `selectAufgabeByEigenschaft` ( IN `eigName` VARCHAR( 255 ) CHARSET utf8 ) NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER SELECT DISTINCT aufg_name
FROM aufg_beinhaltet_eig
WHERE eig_name = eigName;

CREATE PROCEDURE `selectParzelleBySorte` ( IN `sortenName` VARCHAR( 255 ) CHARSET utf8 ) NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER SELECT parz_id
FROM parzellen
WHERE sorte = sortenName;

CREATE PROCEDURE `selectFeldRows`() NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER SELECT DISTINCT row_nr FROM feld ORDER BY row_nr ASC;

CREATE PROCEDURE `selectFeldByRow`(IN `rowNr` INT) NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER SELECT * FROM feld WHERE row_nr = rowNr ORDER BY column_nr ASC;

CREATE PROCEDURE `selectSpecificFeld`(IN `id` INT) NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER SELECT * FROM feld WHERE feld_id = id;

CREATE PROCEDURE `selectSorte`() NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER SELECT * FROM sorte;

CREATE PROCEDURE `selectParzelleByFeld`(IN `feldId` INT) NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER SELECT * FROM parzellen WHERE feld_nr = feldId ORDER BY parz_row, parz_col ASC;

CREATE PROCEDURE `selectParzellenRows` ( IN `feldNr` INT( 11 ) ) NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER SELECT DISTINCT parz_row 
FROM parzellen 
WHERE feld_nr = feldNr 
ORDER BY parz_row ASC;

CREATE PROCEDURE `selectMaxFeldColumn` ( ) NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER 
SELECT MAX( counted ) AS maxColumn 
FROM ( 
SELECT COUNT( column_nr ) AS counted 
FROM feld 
GROUP BY row_nr 
) AS countColumns;

CREATE PROCEDURE `selectParzelleByAufgabe` ( IN `aufgName` VARCHAR( 255 ) ) NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER SELECT parz_id
FROM aufg_gehoert_zu_parz
WHERE aufg_name = aufgName;