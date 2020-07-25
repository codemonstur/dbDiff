
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE `babyshower` ADD COLUMN `site_template` varchar(45) NOT NULL AFTER `last_updated`, ADD COLUMN `hostname` varchar(45) NOT NULL AFTER `site_template`, ADD UNIQUE INDEX `hostname_UNIQUE` (`hostname`) USING BTREE;
SET FOREIGN_KEY_CHECKS = 1;
