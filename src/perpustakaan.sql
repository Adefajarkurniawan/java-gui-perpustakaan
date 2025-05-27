-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 27, 2025 at 03:40 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `perpustakaan`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `year_published` year(4) DEFAULT NULL,
  `genre` varchar(100) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `stock` int(11) DEFAULT 2
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`id`, `title`, `author`, `year_published`, `genre`, `created_at`, `stock`) VALUES
(3, 'fffffffffffffffff', 'a', '1990', 'Science', '2025-05-25 20:38:58', 2),
(4, 'v dasdddd', 'wd', '1993', 'Fantasi', '2025-05-25 20:42:17', 2),
(6, 'harry', 'harry', '2000', 'Science', '2025-05-26 15:14:09', 2),
(7, 'poterw', 'dwa', '2022', 'Petualangan', '2025-05-26 15:14:29', 3),
(11, 'mmmmm', 'jjj', '1990', 'Fantasi', '2025-05-26 21:41:07', 13),
(12, 'gren', 'gkkkk', '1998', 'Fantasi', '2025-05-26 21:41:26', 3);

-- --------------------------------------------------------

--
-- Table structure for table `loans`
--

CREATE TABLE `loans` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `loan_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  `status` enum('dipinjam','dikembalikan') DEFAULT 'dipinjam'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `loans`
--

INSERT INTO `loans` (`id`, `user_id`, `book_id`, `loan_date`, `return_date`, `status`) VALUES
(1, 3, 4, '2025-05-26', '2025-05-26', 'dikembalikan'),
(2, 4, 4, '2025-05-26', '2025-05-26', 'dikembalikan'),
(4, 3, 3, '2025-05-26', '2025-05-26', 'dikembalikan'),
(5, 3, 4, '2025-05-26', '2025-05-26', 'dikembalikan'),
(6, 3, 4, '2025-05-26', '2025-05-26', 'dikembalikan'),
(7, 3, 4, '2025-05-26', '2025-05-26', 'dikembalikan'),
(8, 3, 4, '2025-05-26', '2025-05-26', 'dikembalikan'),
(9, 3, 3, '2025-05-26', '2025-05-26', 'dikembalikan'),
(10, 3, 3, '2025-05-26', '2025-05-26', 'dikembalikan'),
(11, 3, 4, '2025-05-26', '2025-05-26', 'dikembalikan'),
(12, 3, 3, '2025-05-26', '2025-05-26', 'dikembalikan'),
(13, 3, 4, '2025-05-26', '2025-05-26', 'dikembalikan'),
(14, 3, 7, '2025-05-27', '2025-05-27', 'dikembalikan'),
(15, 4, 7, '2025-05-27', '2025-05-27', 'dikembalikan'),
(16, 3, 7, '2025-05-27', '2025-05-27', 'dikembalikan'),
(17, 3, 7, '2025-05-27', '2025-05-27', 'dikembalikan'),
(18, 4, 7, '2025-05-27', '2025-05-27', 'dikembalikan'),
(19, 3, 7, '2025-05-27', '2025-05-27', 'dikembalikan'),
(20, 3, 6, '2025-05-27', '2025-05-27', 'dikembalikan'),
(21, 4, 7, '2025-05-27', '2025-05-27', 'dikembalikan'),
(22, 5, 7, '2025-05-27', '2025-05-27', 'dikembalikan'),
(23, 7, 7, '2025-05-27', '2025-05-27', 'dikembalikan'),
(24, 8, 6, '2025-05-27', '2025-05-27', 'dikembalikan'),
(25, 3, 6, '2025-05-27', '2025-05-27', 'dikembalikan'),
(26, 3, 6, '2025-05-27', '2025-05-27', 'dikembalikan'),
(27, 9, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(28, 9, 11, '2025-05-27', '2025-05-27', 'dikembalikan'),
(29, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(30, 8, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(31, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(32, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(33, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(34, 3, 3, '2025-05-27', '2025-05-27', 'dikembalikan'),
(35, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(36, 8, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(37, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(38, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(39, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(40, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(41, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(42, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(43, 14, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(44, 14, 11, '2025-05-27', '2025-05-27', 'dikembalikan'),
(45, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(46, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(47, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(48, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(49, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(50, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(51, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(52, 15, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(53, 15, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(54, 3, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(55, 16, 11, '2025-05-27', '2025-05-27', 'dikembalikan'),
(56, 8, 11, '2025-05-27', '2025-05-27', 'dikembalikan'),
(57, 17, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(58, 17, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(59, 17, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(60, 17, 12, '2025-05-27', '2025-05-27', 'dikembalikan'),
(61, 18, 6, '2025-05-27', '2025-05-27', 'dikembalikan'),
(62, 18, 4, '2025-05-27', '2025-05-27', 'dikembalikan'),
(63, 8, 4, '2025-05-27', '2025-05-27', 'dikembalikan');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','user') NOT NULL DEFAULT 'user',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`, `created_at`) VALUES
(3, 'ade', '96e6102e9841005704ba015e2d6900fb3ea6a10223d2c39e841685931ce251d6', 'user', '2025-05-25 17:42:58'),
(4, 'fajar', '489690ca5c8ac7e87523e6aad16b7f6b6f878b5d2d6a185508e30277d198b7db', 'user', '2025-05-25 17:44:55'),
(5, 'kurni', '78c87178aecbdb9d0a946a88692d511ff4c5c4d3059b564421f264c7b665f14e', 'user', '2025-05-25 18:11:21'),
(6, 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin', '2025-05-25 18:18:34'),
(7, 'laa', '5f12ca4a862a4545fc94b059db2f1ce91c3b568d62f31aef1e39ad39b6c4b596', 'user', '2025-05-26 21:15:09'),
(8, 'add', '7e9e5ac30f2216fd0fd6f5faed316f2d5983361a4203c3330cfa46ef65bb4767', 'user', '2025-05-26 21:26:47'),
(9, 'mon', 'e6d3efc6abfd85ceacedc20ecbb866e564fe16742930c48f8618bff70c5028e6', 'user', '2025-05-26 21:43:10'),
(10, 'wdw', 'da88dd1b2b820360d4155162e657f84ea1394076faa1ce2909d8338811cb308d', 'user', '2025-05-26 22:09:22'),
(11, 'a', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'user', '2025-05-27 02:48:09'),
(12, 'ac', 'f45de51cdef30991551e41e882dd7b5404799648a0a00753f44fc966e6153fc1', 'user', '2025-05-27 03:25:50'),
(13, 'chiss', 'ea29b6bca6045aa1068df48dc45cefa0ccebaa5ef9057463c597c32dc8fedcb8', 'user', '2025-05-27 03:26:33'),
(14, 'pin', '64f46a7526a186d2346552453ae478ca51244674f5b21ba150bd483b39f7c812', 'user', '2025-05-27 05:13:47'),
(15, 'z', '594e519ae499312b29433b7dd8a97ff068defcba9755b6d5d00e84c524d67b06', 'user', '2025-05-27 06:28:53'),
(16, 'k', '8254c329a92850f6d539dd376f4816ee2764517da5e0235514af433164480d7a', 'user', '2025-05-27 11:34:07'),
(17, 'kakkev', '250774af6c5e641c2650603197cc4e4f36f231c6b6f247ab2e7b49182817005d', 'user', '2025-05-27 12:56:12'),
(18, 'alip', 'fe23609614e1128e86ba8f9e60874fe02972243570a4929484b8871a84d19883', 'user', '2025-05-27 13:37:24');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `loans`
--
ALTER TABLE `loans`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `book_id` (`book_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `loans`
--
ALTER TABLE `loans`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=64;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `loans`
--
ALTER TABLE `loans`
  ADD CONSTRAINT `loans_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `loans_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
