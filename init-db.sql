CREATE DATABASE internet_shop DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE `internet_shop`.`products` (
  `product_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `price` DOUBLE NOT NULL,
  'deleted' BOOLEAN NOT NULL DEFAULT false,
  PRIMARY KEY (`product_id`));