# folder-access-rest
Find movie files in folder and stores in database, uses ffmpeg or avconv


#SQL to create table and user

use datos;

create table movies(

	id int not null auto_increment,
	name varchar(500),
	directory varchar(500),
	duration varchar(200),
	size varchar(20),
	thumb varchar(200),
	fecha timestamp default current_timestamp,
	constraint pk_movies primary key (id)	
)ENGINE=innodb;


create table data_movies_tags(

	cod int not null auto_increment,
	tag varchar(200) unique,
	fecha timestamp default current_timestamp on update current_timestamp,
	constraint pk_movies_tags primary key (cod)
)ENGINE=innodb;

create table rel_movies_tags(

	cod_tag int not null,
	cod_movie int not null,
	fecha timestamp default current_timestamp on update current_timestamp,
	constraint pk_movies_tags_rel primary key (codTag,codMovie),
	CONSTRAINT fk_movies_tags_rel FOREIGN KEY(codMovie) references movies(id) on delete cascade on update cascade,
	CONSTRAINT fk_tags_movies_rel FOREIGN KEY(codTag) references data_movies_tags(cod) on delete cascade on update cascade
)ENGINE=innodb;



CREATE USER data_movies identified by 'data_com_180269';
grant select,update,insert,delete on table movies to data_movies identified by 'data_com_180269';
grant select,update,insert,delete on table data_movies_tags to data_movies identified by 'data_com_180269';
grant select,update,insert,delete on table rel_movies_tags to data_movies identified by 'data_com_180269';
