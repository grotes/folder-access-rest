# folder-access-rest
Find movie files in folder and stores in database, uses ffmpeg or avconv


#SQL to create table and user(

use datos;

create table movies(
	id int not null auto_increment,
	name varchar(500),
	directory varchar(500),
	duration varchar(200),
	size varchar(20),
	thumb1 mediumblob,
	thumb2 mediumblob,
	thumb3 mediumblob,
	thumb4 mediumblob,
	fecha timestamp default current_timestamp,
	constraint pk_movies primary key (id)	
)ENGINE=innodb;


CREATE USER data_movies identified by 'data_com_180269';

grant select,update,insert,delete on table movies to data_movies identified by 'data_com_180269';
)
