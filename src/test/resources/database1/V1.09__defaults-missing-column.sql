
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE `babyshower` ADD COLUMN `has_name_of_baby` bit(1) NOT NULL DEFAULT b'0' AFTER `has_address`, ADD COLUMN `has_theme` bit(1) NULL DEFAULT NULL AFTER `has_gift_for_baby`, MODIFY COLUMN `has_name_of_party` bit(1) NOT NULL DEFAULT b'0' AFTER `site_template`, MODIFY COLUMN `has_date_of_party` bit(1) NOT NULL DEFAULT b'0' AFTER `has_name_of_party`, MODIFY COLUMN `has_time_of_party` bit(1) NOT NULL DEFAULT b'0' AFTER `has_date_of_party`, MODIFY COLUMN `has_dob_baby` bit(1) NOT NULL DEFAULT b'0' AFTER `has_time_of_party`, MODIFY COLUMN `has_name_of_mom` bit(1) NOT NULL DEFAULT b'0' AFTER `has_dob_baby`, MODIFY COLUMN `has_address` bit(1) NOT NULL DEFAULT b'0' AFTER `has_name_of_mom`;
ALTER TABLE `game` MODIFY COLUMN `identifier` bigint(20) NOT NULL AFTER `babyshower_id`;
SET FOREIGN_KEY_CHECKS = 1;
