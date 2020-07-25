
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE `babyshower` ADD COLUMN `enable_birthday_bets` bit(1) NOT NULL DEFAULT b'1' AFTER `longitude`, ADD COLUMN `enable_game_suggestion` bit(1) NOT NULL DEFAULT b'1' AFTER `enable_birthday_bets`;
SET FOREIGN_KEY_CHECKS = 1;
