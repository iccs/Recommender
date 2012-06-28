create table component_subject(
  component varchar(255) not null,
  subject text not null,
  weight double
) engine= innodb;


drop view if exists all_components;
create view all_components
as
select distinct component from component_subject;

