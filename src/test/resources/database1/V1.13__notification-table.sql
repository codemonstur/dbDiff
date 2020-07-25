
SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE `notification` ( `notification_id` int(10) unsigned NOT NULL AUTO_INCREMENT, `babyshower_id` int(10) unsigned NOT NULL, `created_date` bigint(20) NOT NULL, `trigger_date` bigint(20) NOT NULL, `type` int(11) NOT NULL, `is_sent` bit(1) NOT NULL DEFAULT b'0', `deleted` bit(1) NOT NULL DEFAULT b'0', `deleted_date` bigint(20) DEFAULT NULL, PRIMARY KEY (`notification_id`), KEY `fk_notification_babyshower_idx` (`babyshower_id`), CONSTRAINT `fk_notification_babyshower` FOREIGN KEY (`babyshower_id`) REFERENCES `babyshower` (`babyshower_id`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SET FOREIGN_KEY_CHECKS = 1;
