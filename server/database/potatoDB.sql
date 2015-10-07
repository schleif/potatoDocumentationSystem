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


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `potato`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `aufgabe`
--

CREATE TABLE IF NOT EXISTS `aufgabe` (
  `aufg_name` varchar(255) NOT NULL,
  `from` date NOT NULL,
  `to` date NOT NULL
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
  ADD PRIMARY KEY (`parz_id`), ADD UNIQUE KEY `feld_nr` (`feld_nr`), ADD KEY `sorte` (`sorte`);

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
