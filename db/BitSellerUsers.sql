-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Окт 25 2021 г., 16:24
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
-- Структура таблицы `BitSellerUsers`
--

CREATE TABLE `BitSellerUsers` (
  `id` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `subscription` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `BitSellerUsers`
--

INSERT INTO `BitSellerUsers` (`id`, `name`, `subscription`) VALUES
('134534516', 'Andrey', 1),
('283453453420', 'Vladimir', 1),
('46345345345', 'Valentin', 1),
('693345349829', 'Sergey', 1);

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `BitSellerUsers`
--
ALTER TABLE `BitSellerUsers`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
