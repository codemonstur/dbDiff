
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE `babyshower` MODIFY COLUMN `hostname` varchar(64) NULL DEFAULT NULL AFTER `deleted_date`, MODIFY COLUMN `name_of_party` varchar(64) NULL DEFAULT NULL AFTER `hostname`;
SET FOREIGN_KEY_CHECKS = 1;
