-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Окт 25 2021 г., 16:23
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
-- Структура таблицы `BitSellerClients`
--

CREATE TABLE `BitSellerClients` (
  `INN` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `BitSellerClients`
--

INSERT INTO `BitSellerClients` (`INN`, `Name`) VALUES
('0323054148', 'РБ Онкологический диспансер');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `BitSellerClients`
--
ALTER TABLE `BitSellerClients`
  ADD PRIMARY KEY (`INN`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
