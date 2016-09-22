# Submissions schema

# --- !Ups

CREATE SEQUENCE submission_id_seq;
CREATE TABLE submission (
    id integer NOT NULL DEFAULT nextval('submission_id_seq'),
    user_response varchar(255),
    user_name varchar(255)
);

# --- !Downs

DROP TABLE submission;
DROP SEQUENCE submission_id_seq;