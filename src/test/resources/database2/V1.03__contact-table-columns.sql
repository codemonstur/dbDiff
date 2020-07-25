
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE `contact` ADD COLUMN `name` varchar(64) NULL DEFAULT NULL AFTER `email`, ADD COLUMN `language` varchar(16) NULL DEFAULT NULL AFTER `country`, ADD COLUMN `contact_me` bit(1) NULL DEFAULT NULL AFTER `created_date`, ADD COLUMN `send_newsletter` bit(1) NULL DEFAULT NULL AFTER `contact_me`, ADD COLUMN `hostname_typed` varchar(256) NULL DEFAULT NULL AFTER `send_newsletter`, ADD COLUMN `uri_scanned` varchar(256) NULL DEFAULT NULL AFTER `hostname_typed`, ADD COLUMN `result_cookies` varchar(45) NULL DEFAULT NULL AFTER `uri_scanned`, ADD COLUMN `result_forms` varchar(45) NULL DEFAULT NULL AFTER `result_cookies`, ADD COLUMN `result_redirect` varchar(45) NULL DEFAULT NULL AFTER `result_forms`, ADD COLUMN `result_impressum` varchar(45) NULL DEFAULT NULL AFTER `result_redirect`, ADD COLUMN `result_shop` varchar(45) NULL DEFAULT NULL AFTER `result_impressum`, DROP COLUMN `first_name`, DROP COLUMN `last_name`;
SET FOREIGN_KEY_CHECKS = 1;
