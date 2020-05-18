-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 18, 2020 at 08:13 PM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mydbtest`
--

-- --------------------------------------------------------

--
-- Table structure for table `adresse`
--

CREATE TABLE `adresse` (
  `adresseid` bigint(20) NOT NULL,
  `kundeid` bigint(20) NOT NULL,
  `adressenr` int(11) NOT NULL,
  `strasse` varchar(255) NOT NULL,
  `hausnummer` varchar(10) NOT NULL,
  `adresszusatz` varchar(255) DEFAULT NULL,
  `plz` varchar(10) NOT NULL,
  `ort` varchar(255) NOT NULL,
  `land` varchar(255) NOT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastmodified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `version` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `bankverbindung`
--

CREATE TABLE `bankverbindung` (
  `bankverbindungid` bigint(20) NOT NULL,
  `kundeid` bigint(20) NOT NULL,
  `bankverbindungnr` int(11) NOT NULL,
  `iban` varchar(30) NOT NULL,
  `bic` varchar(15) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `kontoinhaber` varchar(255) NOT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastmodified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `version` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `gebuehr`
--

CREATE TABLE `gebuehr` (
  `gebuehrid` bigint(20) NOT NULL,
  `grundgebuehr` int(11) NOT NULL,
  `einsatzprotipp` int(11) NOT NULL,
  `einsatzspiel77` int(11) NOT NULL,
  `einsatzsuper6` int(11) NOT NULL,
  `gueltigab` datetime DEFAULT NULL,
  `gueltigbis` datetime DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastmodified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `version` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `gewinnklasse`
--

CREATE TABLE `gewinnklasse` (
  `gewinnklasseid` bigint(20) NOT NULL,
  `spielid` bigint(20) NOT NULL,
  `gewinnklassenr` int(11) NOT NULL,
  `bezeichnunglatein` varchar(5) NOT NULL,
  `beschreibung` varchar(255) DEFAULT NULL,
  `isabsolut` tinyint(1) NOT NULL,
  `betrag` bigint(20) DEFAULT NULL,
  `gueltigab` datetime DEFAULT NULL,
  `gueltigbis` datetime DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastmodified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `version` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `gewinnklasseziehungquote`
--

CREATE TABLE `gewinnklasseziehungquote` (
  `gewinnklasseziehungquoteid` bigint(20) NOT NULL,
  `anzahlgewinner` int(11) NOT NULL,
  `gewinnklasseid` bigint(20) NOT NULL,
  `ziehungid` bigint(20) NOT NULL,
  `quote` bigint(20) NOT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastmodified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `version` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `groups`
--

CREATE TABLE `groups` (
  `group_id` bigint(20) NOT NULL,
  `group_name` varchar(20) NOT NULL,
  `group_desc` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `jackpot`
--

CREATE TABLE `jackpot` (
  `jackpotid` bigint(20) NOT NULL,
  `ziehungid` bigint(20) NOT NULL,
  `anzahlziehungen` int(11) NOT NULL,
  `betrag` bigint(20) NOT NULL,
  `gewinnklasseid` bigint(20) NOT NULL,
  `betragkumuliert` bigint(20) NOT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastmodified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `version` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `kunde`
--

CREATE TABLE `kunde` (
  `kundeid` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `vorname` varchar(255) DEFAULT NULL,
  `guthaben` bigint(20) DEFAULT NULL,
  `dispo` bigint(20) DEFAULT NULL,
  `gesperrt` datetime DEFAULT NULL,
  `isannahmestelle` tinyint(1) DEFAULT 0,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastmodified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `version` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `lottoschein`
--

CREATE TABLE `lottoschein` (
  `lottoscheinid` bigint(20) NOT NULL,
  `belegnummer` bigint(20) DEFAULT NULL,
  `losnummer` int(11) NOT NULL,
  `kundeid` bigint(20) NOT NULL,
  `isspiel77` tinyint(1) DEFAULT NULL,
  `issuper6` tinyint(1) DEFAULT NULL,
  `ismittwoch` tinyint(1) DEFAULT NULL,
  `issamstag` tinyint(1) DEFAULT NULL,
  `laufzeit` int(11) DEFAULT NULL,
  `tipps` varbinary(96) DEFAULT NULL,
  `isabgeschlossen` tinyint(1) DEFAULT NULL,
  `abgabezeitpunkt` datetime NOT NULL DEFAULT current_timestamp(),
  `kosten` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastmodified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `version` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `lottoscheinziehung`
--

CREATE TABLE `lottoscheinziehung` (
  `lottoscheinziehungid` bigint(20) NOT NULL,
  `lottoscheinid` bigint(20) NOT NULL,
  `ziehungnr` int(11) NOT NULL,
  `ziehungid` bigint(20) NOT NULL,
  `gewinnklasseidspiel77` bigint(20) DEFAULT NULL,
  `gewinnklasseidsuper6` bigint(20) DEFAULT NULL,
  `isabgeschlossen` tinyint(1) DEFAULT NULL,
  `isletzteziehung` tinyint(1) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastmodified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `version` int(11) DEFAULT NULL,
  `gewinnspiel77` bigint(20) DEFAULT NULL,
  `gewinnsuper6` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `lottoscheinziehung6aus49`
--

CREATE TABLE `lottoscheinziehung6aus49` (
  `lottoscheinziehung6aus49id` bigint(20) NOT NULL,
  `lottoscheinziehungid` bigint(20) NOT NULL,
  `tippnr` int(11) NOT NULL,
  `gewinnklasseid` bigint(20) NOT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastmodified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `version` int(11) DEFAULT NULL,
  `gewinn` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `role_id` bigint(20) NOT NULL,
  `role_name` varchar(20) NOT NULL,
  `role_desc` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `spiel`
--

CREATE TABLE `spiel` (
  `spielid` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `beschreibung` varchar(1023) NOT NULL,
  `pfadanleitung` varchar(255) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastmodified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `version` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` bigint(20) NOT NULL,
  `username` varchar(10) NOT NULL,
  `first_name` varchar(20) DEFAULT NULL,
  `middle_name` varchar(20) DEFAULT NULL,
  `last_name` varchar(20) DEFAULT NULL,
  `password` char(64) NOT NULL,
  `kundeid` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `users2`
--

CREATE TABLE `users2` (
  `user_id` bigint(20) NOT NULL,
  `username` varchar(16) NOT NULL,
  `password` char(64) NOT NULL,
  `kundeid` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user_groups`
--

CREATE TABLE `user_groups` (
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user_roles`
--

CREATE TABLE `user_roles` (
  `user_rolesid` bigint(20) NOT NULL,
  `role` varchar(20) DEFAULT NULL,
  `username` varchar(16) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ziehung`
--

CREATE TABLE `ziehung` (
  `ziehungid` bigint(20) NOT NULL,
  `ziehungsdatum` date NOT NULL,
  `zahlenalsbits` bigint(20) DEFAULT NULL,
  `superzahl` int(11) DEFAULT NULL,
  `spiel77` int(11) DEFAULT NULL,
  `super6` int(11) DEFAULT NULL,
  `einsatzlotto` bigint(20) DEFAULT NULL,
  `einsatzspiel77` bigint(20) DEFAULT NULL,
  `einsatzsuper6` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastmodified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `version` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `adresse`
--
ALTER TABLE `adresse`
  ADD PRIMARY KEY (`adresseid`),
  ADD UNIQUE KEY `uq_kundeid_adressnr` (`kundeid`,`adressenr`),
  ADD KEY `idx_fk_adresse_kunde` (`kundeid`);

--
-- Indexes for table `bankverbindung`
--
ALTER TABLE `bankverbindung`
  ADD PRIMARY KEY (`bankverbindungid`),
  ADD UNIQUE KEY `uq_kundeid_bankverbindungnr` (`kundeid`,`bankverbindungnr`),
  ADD KEY `idx_fk_bankverbindung_kunde` (`kundeid`);

--
-- Indexes for table `gebuehr`
--
ALTER TABLE `gebuehr`
  ADD PRIMARY KEY (`gebuehrid`);

--
-- Indexes for table `gewinnklasse`
--
ALTER TABLE `gewinnklasse`
  ADD PRIMARY KEY (`gewinnklasseid`),
  ADD KEY `idx_fk_gewinnklasse_spiel` (`spielid`);

--
-- Indexes for table `gewinnklasseziehungquote`
--
ALTER TABLE `gewinnklasseziehungquote`
  ADD PRIMARY KEY (`gewinnklasseziehungquoteid`),
  ADD UNIQUE KEY `uq_gewinnklasseid_ziehungid` (`gewinnklasseid`,`ziehungid`),
  ADD KEY `idx_fk_gewinnklasseziehungquote_ziehung` (`ziehungid`),
  ADD KEY `idx_fk_gewinnklasseziehungquote_gewinnklasse` (`gewinnklasseid`);

--
-- Indexes for table `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`group_id`);

--
-- Indexes for table `jackpot`
--
ALTER TABLE `jackpot`
  ADD PRIMARY KEY (`jackpotid`),
  ADD UNIQUE KEY `uq_gewinnklasseid_ziehungid` (`gewinnklasseid`,`ziehungid`),
  ADD KEY `idx_fk_jackpot_ziehung` (`ziehungid`),
  ADD KEY `idx_fk_jackpot_gewinnklasse` (`gewinnklasseid`);

--
-- Indexes for table `kunde`
--
ALTER TABLE `kunde`
  ADD PRIMARY KEY (`kundeid`);

--
-- Indexes for table `lottoschein`
--
ALTER TABLE `lottoschein`
  ADD PRIMARY KEY (`lottoscheinid`),
  ADD UNIQUE KEY `uq_belegnummer` (`belegnummer`),
  ADD KEY `idx_fk_lottoschein_kunde` (`kundeid`);

--
-- Indexes for table `lottoscheinziehung`
--
ALTER TABLE `lottoscheinziehung`
  ADD PRIMARY KEY (`lottoscheinziehungid`),
  ADD UNIQUE KEY `uq_lottoscheinid_ziehungnr` (`lottoscheinid`,`ziehungnr`),
  ADD KEY `idx_fk_lottoscheinziehung_lottoschein` (`lottoscheinid`),
  ADD KEY `idx_fk_lottoscheinziehung_ziehung` (`ziehungid`),
  ADD KEY `idx_fk_lottoscheinziehung_gewinnklasse_spiel77` (`gewinnklasseidspiel77`),
  ADD KEY `idx_fk_lottoscheinziehung_gewinnklasse_super6` (`gewinnklasseidsuper6`);

--
-- Indexes for table `lottoscheinziehung6aus49`
--
ALTER TABLE `lottoscheinziehung6aus49`
  ADD PRIMARY KEY (`lottoscheinziehung6aus49id`),
  ADD UNIQUE KEY `uq_lottoscheinziehungid_tippnr` (`lottoscheinziehungid`,`tippnr`),
  ADD KEY `idx_fk_lottoscheinziehung6aus49_gewinnklasse` (`gewinnklasseid`),
  ADD KEY `idx_fk_lottoscheinziehung6aus49_lottoscheinziehung` (`lottoscheinziehungid`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `spiel`
--
ALTER TABLE `spiel`
  ADD PRIMARY KEY (`spielid`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `kundeid` (`kundeid`),
  ADD UNIQUE KEY `kundeid_2` (`kundeid`);

--
-- Indexes for table `users2`
--
ALTER TABLE `users2`
  ADD PRIMARY KEY (`user_id`),
  ADD KEY `kundeid` (`kundeid`);

--
-- Indexes for table `user_groups`
--
ALTER TABLE `user_groups`
  ADD PRIMARY KEY (`user_id`,`group_id`),
  ADD KEY `fk_users_has_groups_groups1` (`group_id`),
  ADD KEY `fk_users_has_groups_users` (`user_id`);

--
-- Indexes for table `user_roles`
--
ALTER TABLE `user_roles`
  ADD PRIMARY KEY (`user_rolesid`),
  ADD KEY `fk_user` (`user_id`);

--
-- Indexes for table `ziehung`
--
ALTER TABLE `ziehung`
  ADD PRIMARY KEY (`ziehungid`),
  ADD UNIQUE KEY `uq_ziehungsdatum` (`ziehungsdatum`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `adresse`
--
ALTER TABLE `adresse`
  MODIFY `adresseid` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `bankverbindung`
--
ALTER TABLE `bankverbindung`
  MODIFY `bankverbindungid` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `gebuehr`
--
ALTER TABLE `gebuehr`
  MODIFY `gebuehrid` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `gewinnklasse`
--
ALTER TABLE `gewinnklasse`
  MODIFY `gewinnklasseid` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `gewinnklasseziehungquote`
--
ALTER TABLE `gewinnklasseziehungquote`
  MODIFY `gewinnklasseziehungquoteid` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `jackpot`
--
ALTER TABLE `jackpot`
  MODIFY `jackpotid` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `kunde`
--
ALTER TABLE `kunde`
  MODIFY `kundeid` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `lottoschein`
--
ALTER TABLE `lottoschein`
  MODIFY `lottoscheinid` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `lottoscheinziehung`
--
ALTER TABLE `lottoscheinziehung`
  MODIFY `lottoscheinziehungid` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `lottoscheinziehung6aus49`
--
ALTER TABLE `lottoscheinziehung6aus49`
  MODIFY `lottoscheinziehung6aus49id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `role_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `spiel`
--
ALTER TABLE `spiel`
  MODIFY `spielid` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users2`
--
ALTER TABLE `users2`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user_roles`
--
ALTER TABLE `user_roles`
  MODIFY `user_rolesid` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ziehung`
--
ALTER TABLE `ziehung`
  MODIFY `ziehungid` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `adresse`
--
ALTER TABLE `adresse`
  ADD CONSTRAINT `fk_adresse_kunde` FOREIGN KEY (`kundeid`) REFERENCES `kunde` (`kundeid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `bankverbindung`
--
ALTER TABLE `bankverbindung`
  ADD CONSTRAINT `fk_bankverbindung_kunde` FOREIGN KEY (`kundeid`) REFERENCES `kunde` (`kundeid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `gewinnklasse`
--
ALTER TABLE `gewinnklasse`
  ADD CONSTRAINT `fk_gewinnklasse_spiel` FOREIGN KEY (`spielid`) REFERENCES `spiel` (`spielid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `gewinnklasseziehungquote`
--
ALTER TABLE `gewinnklasseziehungquote`
  ADD CONSTRAINT `fk_gewinnklasseziehungquote_gewinnklasse` FOREIGN KEY (`gewinnklasseid`) REFERENCES `gewinnklasse` (`gewinnklasseid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_gewinnklasseziehungquote_ziehung` FOREIGN KEY (`ziehungid`) REFERENCES `ziehung` (`ziehungid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `jackpot`
--
ALTER TABLE `jackpot`
  ADD CONSTRAINT `fk_jackpot_gewinnklasse` FOREIGN KEY (`gewinnklasseid`) REFERENCES `gewinnklasse` (`gewinnklasseid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_jackpot_ziehung` FOREIGN KEY (`ziehungid`) REFERENCES `ziehung` (`ziehungid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `lottoschein`
--
ALTER TABLE `lottoschein`
  ADD CONSTRAINT `fk_lottoschein_kunde` FOREIGN KEY (`kundeid`) REFERENCES `kunde` (`kundeid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `lottoscheinziehung`
--
ALTER TABLE `lottoscheinziehung`
  ADD CONSTRAINT `fk_lottoscheinziehung_gewinnklasse_spiel77` FOREIGN KEY (`gewinnklasseidspiel77`) REFERENCES `gewinnklasse` (`gewinnklasseid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_lottoscheinziehung_gewinnklasse_super6` FOREIGN KEY (`gewinnklasseidsuper6`) REFERENCES `gewinnklasse` (`gewinnklasseid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_lottoscheinziehung_lottoschein` FOREIGN KEY (`lottoscheinid`) REFERENCES `lottoschein` (`lottoscheinid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_lottoscheinziehung_ziehung` FOREIGN KEY (`ziehungid`) REFERENCES `ziehung` (`ziehungid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `lottoscheinziehung6aus49`
--
ALTER TABLE `lottoscheinziehung6aus49`
  ADD CONSTRAINT `fk_lottoscheinziehung6aus49_gewinnklasse` FOREIGN KEY (`gewinnklasseid`) REFERENCES `gewinnklasse` (`gewinnklasseid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_lottoscheinziehung6aus49_lottoscheinziehung` FOREIGN KEY (`lottoscheinziehungid`) REFERENCES `lottoscheinziehung` (`lottoscheinziehungid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `fk_users_kunde` FOREIGN KEY (`kundeid`) REFERENCES `kunde` (`kundeid`);

--
-- Constraints for table `users2`
--
ALTER TABLE `users2`
  ADD CONSTRAINT `users2_ibfk_1` FOREIGN KEY (`kundeid`) REFERENCES `kunde` (`kundeid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `user_groups`
--
ALTER TABLE `user_groups`
  ADD CONSTRAINT `fk_groups` FOREIGN KEY (`group_id`) REFERENCES `groups` (`group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users2` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
