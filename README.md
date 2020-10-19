Internet shop project
This webshop project was developed to get practice with Java Servlets, Tomcat, JDBC, CSS, and database interaction.

In this project I implemented au

So, this shop works only with Authenticated users (Authentication is implemented via filter). After user is registered it has 'USER' role by default. Authorization is also implemented via filter. In sample case our project has two roles: USER and ADMIN.

USER:
can see all of the products, that exist in the shop (but not deleted, soft deleting is implemented), every USER has one shopping cart, can add product to cart and then complete order, can see all of its orders and details of them.

ADMIN:
role, set by system administrator, can delete user, see all users, see all orders, delete order, add product, delete product. Admin does not have a cart and can not create order.

If you want to enter as Admin, there's address /admin/add which will add new user with Admin's role

login: admin

password: 346558

But first of all, there's file init_db.sql which holds sql script for creating appropriate DB. So run this code in MySQL on your machine first. Also, your database should have UTC time zone.

Requirements
To run this project you need to install the following on your computer: Java, Apache Tomcat, MySQL

Tested on
This project was tested with the following software:

Intellij IDEA Ultimate Edition
Apache Tomcat 9.0.37
MySQL 8.0.20



internet-shop
Internet-shop with basic operations.

In this project used N-tier architecture with DB layer, DAO layer, Service layer, Controllers layer and View layer.
Project was developed according to SOLID principles with authorization and authentication by RBAC filter strategy.

One user can have multiple roles.

No role:
Registration
Authorization
View products
User:
View products
Add / remove products to shopping cart
Make an order
Logout
Admin:
View / delete users
View / add / remove products
View / delete user orders.
Logout
Technologies used
backend: Java, Servlets, Tomcat, JDBC
frontend: HTML, CSS, Bootstrap, JSP, JSTL
database: MySQL

To start the project you need:
Download and install the JDK
Download and install servlet container (for example Apache Tomcat)
Download and install MySQL Server
Setup new connection with
user: "your username"
password: "your password"
url: jdbc:mysql://"your host name":"your port"/"your name db"?useUnicode=true&serverTimezone=UTC
Create schema internet_shop
Create tables using commands from init_db.sql under the path src/main/resources/
Available for admin (login: admin, password: admin)
