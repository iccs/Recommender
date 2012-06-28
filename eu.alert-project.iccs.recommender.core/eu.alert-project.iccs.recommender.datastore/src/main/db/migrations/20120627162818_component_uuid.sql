create table uuid_component(
  uuid varchar (256) not null,
  component varchar(255) not null,
  similarity double
) engine=innodb;