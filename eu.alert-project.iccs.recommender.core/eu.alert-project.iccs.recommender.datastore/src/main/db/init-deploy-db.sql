drop database if exists socrates;

create database socrates;

grant all privileges on socrates.* to 'alert'@'localhost' identified by '1234';

use socrates;