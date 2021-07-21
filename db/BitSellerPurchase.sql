-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Июл 21 2021 г., 00:08
-- Версия сервера: 10.3.29-MariaDB-0+deb10u1
-- Версия PHP: 7.3.27-1~deb10u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `orthanc`
--

-- --------------------------------------------------------

--
-- Структура таблицы `BitSellerPurchase`
--

CREATE TABLE `BitSellerPurchase` (
  `purchaseid` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `clientinn` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `BitSellerPurchase`
--

INSERT INTO `BitSellerPurchase` (`purchaseid`, `clientinn`) VALUES
('32110446729', '7536150145'),
('0891200000621007315', '7524006073'),
('0891200000621007063', '7524006073'),
('0891200000621007105', '7524006073'),
('0891200000621007235', '7524006073'),
('0891200000621007231', '7524006073'),
('0891200000621007188', '7524006073'),
('0891200000621007144', '7524006073'),
('32110447717', '7524006073'),
('0891200000621007082', '7524006073'),
('0891200000621007060', '7524006073'),
('0891200000621007012', '7534004283'),
('0891200000621007098', '7534004283'),
('0891200000621007094', '7534004283'),
('0891200000621007084', '7534004283'),
('0891200000621007077', '7534004283'),
('0891200000621007064', '7534004283'),
('0891200000621007039', '7534004283'),
('0891200000621006981', '7534004283'),
('32110432429', '7534004283'),
('32110421633', '7534004283'),
('0891200000621007273', '7536012226'),
('0891200000621007262', '7536012226'),
('0891200000621007217', '7536012226'),
('0891200000621007202', '7536012226'),
('0891200000621007160', '7536012226'),
('0891200000621007156', '7536012226'),
('0891200000621007153', '7536012226'),
('0891200000621007239', '7536120581'),
('32110453748', '7536120581'),
('0891200000621007274', '7536149975'),
('0891200000621007272', '7536149975'),
('0891200000621007245', '7536149975'),
('0891200000621007233', '7536149975'),
('0891200000621007143', '7536149975'),
('0891200000621007138', '7536149975'),
('0891200000621007034', '7524006073'),
('0891200000621006736', '7524006073'),
('0891200000621007341', '7524006073'),
('32110476056', '7536150145'),
('0891200000621007345', '7523002220'),
('0891200000621007342', '7523002220'),
('0891200000621007282', '7523002220'),
('0891200000621007259', '7523002220'),
('0891200000621007110', '7523002220'),
('32110472862', '7527002221'),
('32110472635', '7527002221'),
('32110468849', '7527002221'),
('31806815394', '7527002221'),
('31806833494', '7527002221'),
('31806815788', '7527002221'),
('31806815621', '7527002221'),
('0891200000621007360', '7529001223'),
('0891200000621007276', '7529001223'),
('0891200000621007264', '7529001223'),
('0891200000621007224', '7529001223'),
('0891200000621007222', '7529001223'),
('0891200000621007306', '7538001766'),
('0891200000621007218', '7538001766'),
('0891200000621007176', '7538001766'),
('0891200000621007053', '7538001766'),
('0891200000621007007', '7538001766'),
('0891200000621007380', '7524006073'),
('0891200000621007407', '7523002220'),
('32110476187', '7536120581'),
('0891200000621007413', '7534004283'),
('0891200000621007443', '7529001223'),
('0891200000621007445', '7524006073'),
('0891200000621007444', '7538001766'),
('0891200000621007451', '7529001223'),
('0891200000621007448', '7534004283'),
('0891200000621007471', '7536120581'),
('0891200000621007465', '7524006073'),
('0891200000621007478', '7523002220'),
('32110483773', '7527002221'),
('32110483777', '8000027046'),
('32110483824', '8000027046'),
('32110483810', '7527002221'),
('0891200000621007493', '7538001766'),
('0891200000621007484', '7538001766'),
('0891200000621007450', '7509000790'),
('0891200000621007449', '7509000790'),
('0891200000621007335', '7509000790'),
('0891200000621007334', '7509000790'),
('0891200000621007261', '7509000790'),
('0391300013111000009', '7509000790'),
('0391300013111000002', '7509000790'),
('0891200000621007447', '7513000922'),
('0891200000621007424', '7513000922'),
('0891200000621007414', '7513000922'),
('0891200000621007412', '7513000922'),
('0891200000621007299', '7513000922'),
('0891200000621007281', '7513000922'),
('0891200000621007266', '7513000922'),
('0891200000621007500', '7515002587'),
('0891200000621007479', '7515002587'),
('0891200000621007418', '7515002587'),
('0891200000621007391', '7515002587'),
('0891200000621007346', '7515002587'),
('0891200000621007343', '7515002587'),
('0891200000621007328', '7515002587'),
('0891200000621007327', '7515002587'),
('0891200000621007325', '7515002587'),
('0891200000621007324', '7515002587'),
('0891200000621007311', '7518000980'),
('0891200000621007258', '7518000980'),
('0891200000621007251', '7518000980'),
('0891200000621007208', '7518000980'),
('0891200000621007313', '7519001802'),
('0891200000621007268', '7519001802'),
('0891200000621007265', '7519001802'),
('0891200000621007267', '7519001802'),
('0891200000621007489', '7521000429'),
('0891200000621007284', '7521000429'),
('0891200000621007492', '7525002184'),
('0891200000621007485', '7525002184'),
('0891200000621007446', '7531006589'),
('0891200000621007405', '7531006589'),
('0891200000621007385', '7531006589'),
('0891200000621007356', '7531006589'),
('0891200000621007309', '7531006589'),
('0891200000621007303', '7531006589'),
('0891200000621007290', '7531006589'),
('0891200000621007289', '7531006589'),
('0891200000621007277', '7536115704'),
('32110461021', '7536115704'),
('32110460997', '7536115704'),
('32110460983', '7536115704'),
('32110460954', '7536115704'),
('32110460947', '7536115704'),
('0891200000621007408', '7536130149'),
('0891200000621007416', '7536130149'),
('0891200000621007415', '7536130149'),
('0891200000621007409', '7536130149'),
('0891200000621007216', '7536130149'),
('0891200000621007498', '7518000980'),
('32110465645', '7536026170'),
('0891200000621007426', '7536098400'),
('0891200000621007423', '7536098400'),
('0891200000621007305', '7502000896'),
('0891200000621007246', '7504000718'),
('0891200000621007257', '7506000142'),
('0891200000621007499', '7513000922'),
('0891200000621007504', '7521000429'),
('0891200000621007503', '7521000429'),
('0891200000621007502', '7521000429'),
('0891200000621007248', '7507000106'),
('0891200000621007505', '7507000106'),
('0891200000621007506', '7521000429'),
('0102200001621002627', '0323054148'),
('0102200001621002624', '0323054148'),
('0102200001621002608', '0323054148'),
('0102200001621002604', '0323054148'),
('0102200001621002580', '0323054148'),
('0102200001621002552', '0323054148'),
('0102200001621002516', '0323054148'),
('0102200001621002587', '0323054148'),
('0302200012921000056', '0323054148'),
('0102200001621002577', '0323054148'),
('0891200000621007509', '7509000790'),
('0891200000621007510', '7538001766'),
('32110484186', '7536115704'),
('0891200000621007513', '7538001766');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;