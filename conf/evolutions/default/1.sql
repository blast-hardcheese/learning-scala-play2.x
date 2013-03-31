# Tasks schema

# --- !Ups

CREATE TABLE task (
    id INTEGER PRIMARY KEY,
    label varchar(255)
);

# --- !Downs

DROP TABLE task;
