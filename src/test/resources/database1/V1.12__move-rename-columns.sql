
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE `message` ADD COLUMN `body` text NOT NULL AFTER `title`, DROP COLUMN `message`;
ALTER TABLE `gallery` MODIFY COLUMN `created_date` bigint(20) NOT NULL AFTER `babyshower_id`, MODIFY COLUMN `last_updated` bigint(20) NOT NULL AFTER `created_date`;
SET FOREIGN_KEY_CHECKS = 1;
