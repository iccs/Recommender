create table annotation(
  id int primary key not null unique,
  annotation varchar(255),
  weight int not null default 0
)engine innodb;
insert into sequence values('annotation_sequence',0);

create table issue(
  id int primary key not null unique,
  `sourceId` varchar(255) null,
  `source` varchar(255) null
)engine innodb;
insert into sequence values('issue_sequence',0);


create table issue_annotation(
  issue_id int not null,
  annotation_id int not null,
  weight int not null default 0,
  FOREIGN KEY(issue_id) REFERENCES `issue`(id) ON DELETE CASCADE,
  FOREIGN KEY(annotation_id) REFERENCES `annotation`(id) ON DELETE CASCADE
) engine innodb;

create table identity_annotation(
  identity_id int not null,
  annotation_id int not null,
  weight int not null default 0,
  FOREIGN KEY(identity_id) REFERENCES `identity`(id) ON DELETE CASCADE,
  FOREIGN KEY(annotation_id) REFERENCES `annotation`(id) ON DELETE CASCADE
) engine innodb;



