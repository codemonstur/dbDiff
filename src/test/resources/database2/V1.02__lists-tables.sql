
SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE `section_blacklist` ( `section_id` int(10) unsigned NOT NULL AUTO_INCREMENT, `entry` varchar(64) COLLATE utf8mb4_bin NOT NULL, `created_date` bigint(20) NOT NULL, PRIMARY KEY (`section_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
CREATE TABLE `blacklist` ( `blacklist_id` int(10) unsigned NOT NULL AUTO_INCREMENT, `entry` varchar(256) COLLATE utf8mb4_bin NOT NULL, `created_date` bigint(20) NOT NULL, PRIMARY KEY (`blacklist_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
CREATE TABLE `whitelist` ( `whitelist_id` int(10) unsigned NOT NULL AUTO_INCREMENT, `entry` varchar(256) COLLATE utf8mb4_bin NOT NULL, `created_date` bigint(20) NOT NULL, PRIMARY KEY (`whitelist_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
SET FOREIGN_KEY_CHECKS = 1;
