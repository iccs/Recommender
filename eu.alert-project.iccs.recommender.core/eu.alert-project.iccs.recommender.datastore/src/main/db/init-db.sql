drop database if exists socrates_dev;
drop database if exists socrates_test;

create database socrates_dev;
create database socrates_test;

grant all privileges on socrates_dev.* to 'alert'@'localhost' identified by '1234';
grant all privileges on socrates_test.* to 'alert'@'localhost' identified by '1234';
