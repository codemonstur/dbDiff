
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE `schedule`;
CREATE TABLE `activity` (`activity_id` int(10) unsigned NOT NULL AUTO_INCREMENT, `babyshower_id` int(10) unsigned NOT NULL, `created_date` bigint(20) NOT NULL, `last_updated` bigint(20) NOT NULL, `deleted` bit(1) NOT NULL, `deleted_date` bigint(20) DEFAULT NULL, `start_time` bigint(20) NOT NULL, `end_time` bigint(20) DEFAULT NULL, `name` varchar(45) NOT NULL, `description` varchar(45) DEFAULT NULL, PRIMARY KEY (`activity_id`), KEY `fk_activity_babyshower_idx` (`babyshower_id`), CONSTRAINT `fk_activity_babyshower` FOREIGN KEY (`babyshower_id`) REFERENCES `babyshower` (`babyshower_id`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SET FOREIGN_KEY_CHECKS = 1;
