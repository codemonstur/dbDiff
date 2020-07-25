
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE `account` ADD COLUMN `username` binary(32) NOT NULL AFTER `account_id`, ADD COLUMN `must_reset_password` bit(1) NOT NULL AFTER `verified`, MODIFY COLUMN `email` varchar(64) NOT NULL AFTER `type`, MODIFY COLUMN `salt` binary(32) NOT NULL AFTER `iterations`, MODIFY COLUMN `password` binary(32) NOT NULL AFTER `salt`;
SET FOREIGN_KEY_CHECKS = 1;
