
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE `account` MODIFY COLUMN `deleted` bit(1) NOT NULL DEFAULT b'0' AFTER `must_reset_password`;
SET FOREIGN_KEY_CHECKS = 1;
