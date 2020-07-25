
SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE `birthday_bet` ( `birthday_bet_id` int unsigned NOT NULL AUTO_INCREMENT, `babyshower_id` int unsigned NOT NULL, `created_date` bigint NOT NULL, `name` varchar(64) NOT NULL, `moment` bigint NOT NULL, `deleted` bit(1) NOT NULL DEFAULT b'0', `deleted_date` bigint DEFAULT NULL, PRIMARY KEY (`birthday_bet_id`), KEY `fk_birthday_shower_idx` (`babyshower_id`), CONSTRAINT `fk_birthday_shower` FOREIGN KEY (`babyshower_id`) REFERENCES `babyshower` (`babyshower_id`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
SET FOREIGN_KEY_CHECKS = 1;
