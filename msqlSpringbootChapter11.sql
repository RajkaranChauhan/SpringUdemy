create database APIDevlopmentSpringBoot;

use APIDevlopmentSpringBoot;

create table LibraryDemo(book_name varchar(50), 
id varchar(50),
isbn varchar(50),
aisle int,
author varchar(50),
primary key (id));

insert into LibraryDemo(book_name,id,isbn,aisle,author) values("Appium","abcd1234","abcd1","234","Raj");

select * from LibraryDemo