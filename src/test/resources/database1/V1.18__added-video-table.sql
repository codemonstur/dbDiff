
SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE `video` ( `video_id` int unsigned NOT NULL AUTO_INCREMENT, `babyshower_id` int unsigned NOT NULL, `created_date` bigint NOT NULL, `last_updated` bigint NOT NULL, `youtube_id` varchar(45) NOT NULL, `caption` varchar(256) DEFAULT NULL, `deleted` bit(1) NOT NULL DEFAULT b'0', `deleted_date` bigint DEFAULT NULL, PRIMARY KEY (`video_id`), KEY `fk_video_babyshower_idx` (`babyshower_id`), CONSTRAINT `fk_video_babyshower` FOREIGN KEY (`babyshower_id`) REFERENCES `babyshower` (`babyshower_id`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
SET FOREIGN_KEY_CHECKS = 1;
