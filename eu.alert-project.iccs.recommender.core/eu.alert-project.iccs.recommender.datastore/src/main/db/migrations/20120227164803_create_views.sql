drop view if exists all_issues;
create view all_issues
as
select distinct issue_id from issue_subject;


drop view if exists all_uuid;
create view all_uuid
as
select distinct uuid from uuid_subject;
