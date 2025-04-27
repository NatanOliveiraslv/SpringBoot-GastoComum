alter table spending add title VARCHAR(255);
update spending set title = "sem título" where title is null;