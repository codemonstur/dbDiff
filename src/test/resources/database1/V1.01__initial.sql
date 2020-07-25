
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `account` (
 `account_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `email` varchar(64) COLLATE utf8mb4_bin NOT NULL,
 `algorithm` bigint(20) NOT NULL,
 `strategy` bigint(20) NOT NULL,
 `iterations` bigint(20) NOT NULL,
 `salt` varchar(64) COLLATE utf8mb4_bin NOT NULL,
 `password` varchar(64) COLLATE utf8mb4_bin NOT NULL,
 `type` bigint(20) NOT NULL,
 `name` varchar(64) COLLATE utf8mb4_bin NOT NULL,
 `created_date` bigint(20) NOT NULL,
 `last_login` bigint(20) NOT NULL,
 `enabled` bit(1) NOT NULL,
 `verified` bit(1) NOT NULL,
 PRIMARY KEY (`account_id`), UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `events` (
 `event_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `timestamp` bigint(20) NOT NULL,
 `type` int(11) NOT NULL,
 `email_template` int(11) DEFAULT NULL,
 `email_recipient` varchar(64) COLLATE utf8mb4_bin NOT NULL,
 `account_id` int(11) unsigned DEFAULT NULL,
 PRIMARY KEY (`event_id`),
 KEY `fk_emails_account_idx` (`account_id`),
 CONSTRAINT `fk_emails_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
