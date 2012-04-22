create index `class_idx` ON uuid_class(class(30));
create index `uuid_idx` ON uuid_class(uuid(256));
create index uuid_uuid_idx on uuid_issue(uuid(256));
create index `uuid_issue_idx` ON uuid_issue(uuid(256),issue_id);
create index issue_subject_idx on issue_subject(issue_id);
create index issue_uuid_idx on uuid_issue(issue_id);


