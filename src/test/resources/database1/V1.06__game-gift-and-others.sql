
SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE `game` ( `game_id` int(10) unsigned NOT NULL AUTO_INCREMENT, `babyshower_id` int(10) unsigned NOT NULL, `identifier` bigint(20) unsigned NOT NULL, `created_date` bigint(20) NOT NULL, `updated_date` bigint(20) NOT NULL, `votes` int(10) unsigned NOT NULL, `definitive` bit(1) NOT NULL, `deleted` bit(1) NOT NULL, `deleted_date` bigint(20) DEFAULT NULL, PRIMARY KEY (`game_id`), KEY `fk_game_babyshower_idx` (`babyshower_id`), CONSTRAINT `fk_game_babyshower` FOREIGN KEY (`babyshower_id`) REFERENCES `babyshower` (`babyshower_id`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
ALTER TABLE `participant` ADD COLUMN `first_name` varchar(45) NOT NULL AFTER `email`, ADD COLUMN `last_name` varchar(45) NOT NULL AFTER `first_name`, DROP COLUMN `name`;
ALTER TABLE `invitation` ADD COLUMN `babyshower_id` int(10) unsigned NOT NULL AFTER `invitation_id`, ADD INDEX `fk_invitation_babyshower_idx` (`babyshower_id`) USING BTREE, ADD CONSTRAINT `fk_invitation_babyshower` FOREIGN KEY `fk_invitation_babyshower` (`babyshower_id`) REFERENCES `babyshower` (`babyshower_id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `gift` ADD COLUMN `babyshower_id` int(10) unsigned NOT NULL AFTER `gift_id`, DROP COLUMN `participant_id`, ADD INDEX `fk_gift_babyshower_idx` (`babyshower_id`) USING BTREE, ADD CONSTRAINT `fk_gift_babyshower` FOREIGN KEY `fk_gift_babyshower` (`babyshower_id`) REFERENCES `babyshower` (`babyshower_id`) ON DELETE CASCADE ON UPDATE CASCADE, DROP FOREIGN KEY `fk_gift_participant`, DROP INDEX `fk_gift_participant_idx`;
SET FOREIGN_KEY_CHECKS = 1;