create table sequence(
  sequence_name varchar(100) primary key unique,
  sequence_index INT NOT NULL
) engine=innodb;

create table schema_history(
  history_date timestamp default now(),
  description text not null
) engine=innodb;

create table issue_subject(
  issue_id int not null,
  subject text not null,
  weight double
) engine= innodb;

create table uuid_class(
  uuid varchar (256) not null,
  class text not null,
  weight double
)engine=innodb;

create table uuid_issue(
  uuid varchar (256) not null,
  issue_id int not null,
  similarity double
) engine=innodb;

create table uuid_subject(
  uuid varchar(256) not null,
  subject text not null,
  weight double
) engine=innodb;



