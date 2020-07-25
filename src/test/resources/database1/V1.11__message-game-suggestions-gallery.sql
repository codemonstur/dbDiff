
SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE `message` ( `message_id` int(10) unsigned NOT NULL AUTO_INCREMENT, `babyshower_id` int(10) unsigned NOT NULL, `created_date` bigint(20) NOT NULL, `author` varchar(45) NOT NULL, `title` varchar(45) NOT NULL, `message` text NOT NULL, `deleted` bit(1) NOT NULL DEFAULT b'0', `deleted_date` bigint(20) DEFAULT NULL, PRIMARY KEY (`message_id`), KEY `fk_message_babyshower_idx` (`babyshower_id`), CONSTRAINT `fk_message_babyshower` FOREIGN KEY (`babyshower_id`) REFERENCES `babyshower` (`babyshower_id`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `game_suggestion` ( `suggestion_id` int(10) unsigned NOT NULL AUTO_INCREMENT, `babyshower_id` int(10) unsigned NOT NULL, `created_date` bigint(20) NOT NULL, `votes` int(11) NOT NULL DEFAULT 1, `name` varchar(64) NOT NULL, `suggester` varchar(64) DEFAULT NULL, `description` text DEFAULT NULL, `deleted` bit(1) NOT NULL DEFAULT b'0', `deleted_date` bigint(20) DEFAULT NULL, PRIMARY KEY (`suggestion_id`), KEY `fk_suggest_game_babyshower_idx` (`babyshower_id`), CONSTRAINT `fk_suggest_game_babyshower` FOREIGN KEY (`babyshower_id`) REFERENCES `babyshower` (`babyshower_id`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gallery` ( `gallery_id` int(10) unsigned NOT NULL AUTO_INCREMENT, `babyshower_id` int(10) unsigned NOT NULL, `picture` varchar(64) NOT NULL, `caption` varchar(256) DEFAULT NULL, `created_date` bigint(20) NOT NULL, `last_updated` bigint(20) NOT NULL, `deleted` bit(1) NOT NULL DEFAULT b'0', `deleted_date` bigint(20) DEFAULT NULL, PRIMARY KEY (`gallery_id`), KEY `fk_gallery_babyshower_idx` (`babyshower_id`), CONSTRAINT `fk_gallery_babyshower` FOREIGN KEY (`babyshower_id`) REFERENCES `babyshower` (`babyshower_id`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SET FOREIGN_KEY_CHECKS = 1;
