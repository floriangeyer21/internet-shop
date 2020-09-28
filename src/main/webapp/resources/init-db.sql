CREATE DATABASE internet_shop DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE `internet_shop`.`products` (
  `product_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `price` DOUBLE NOT NULL,
 `deleted` BOOLEAN NOT NULL DEFAULT false,
 PRIMARY KEY (`product_id`));

 CREATE TABLE `internet_shop`.`users` (
  `user_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
   `login` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `salt` BINARY(16) NOT NULL,
 `deleted` BOOLEAN NOT NULL DEFAULT false,
 PRIMARY KEY (`user_id`));

 CREATE TABLE `internet_shop`.`roles` (
  `role_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(255) NOT NULL,
 `deleted` BOOLEAN NOT NULL DEFAULT false,
 PRIMARY KEY (`role_id`));

 INSERT INTO `roles`(role_name)
VALUES ('ADMIN');
INSERT INTO `roles`(role_name)
VALUES ('USER');

 CREATE TABLE `internet_shop`.`shopping_carts` (
  `cart_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(11) NOT NULL,
 `deleted` BOOLEAN NOT NULL DEFAULT false,
 PRIMARY KEY (`cart_id`),
 CONSTRAINT fk_userIDtoSPcart
    FOREIGN KEY (`user_id`)
    REFERENCES internet_shop.users(`user_id`)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
 );

  CREATE TABLE `internet_shop`.`orders` (
  `order_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(11) NOT NULL,
 `deleted` BOOLEAN NOT NULL DEFAULT false,
 PRIMARY KEY (`order_id`),
 CONSTRAINT fk_userIDtoOrder
    FOREIGN KEY (`user_id`)
    REFERENCES internet_shop.users(`user_id`)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
 );

 CREATE TABLE `internet_shop`.`users_roles`(
 `user_id` BIGINT(11) NOT NULL,
  `role_id` BIGINT(11) NOT NULL,
    CONSTRAINT fk_userId
    FOREIGN KEY (`user_id`)
    REFERENCES internet_shop.users(`user_id`)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    CONSTRAINT fk_roleId
    FOREIGN KEY (`role_id`)
    REFERENCES internet_shop.roles(`role_id`)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
);

CREATE TABLE `internet_shop`.`shopping_carts_products`(
 `cart_id` BIGINT(11) NOT NULL,
  `product_id` BIGINT(11) NOT NULL,
  `deleted` BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT fk_cartId
    FOREIGN KEY (`cart_id`)
    REFERENCES internet_shop.shopping_carts(`cart_id`)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    CONSTRAINT fk_productID
    FOREIGN KEY (`product_id`)
    REFERENCES internet_shop.products(`product_id`)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
);

CREATE TABLE `internet_shop`.`orders_products`(
 `order_id` BIGINT(11) NOT NULL,
  `product_id` BIGINT(11) NOT NULL,
    CONSTRAINT fk_orderID
    FOREIGN KEY (`order_id`)
    REFERENCES internet_shop.orders(`order_id`)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    CONSTRAINT fk_productIDtoOrders
    FOREIGN KEY (`product_id`)
    REFERENCES internet_shop.products(`product_id`)
       ON UPDATE RESTRICT
        ON DELETE RESTRICT
);
