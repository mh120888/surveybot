# Submissions schema

# --- !Ups

CREATE TABLE Submission (
    id BIGSERIAL PRIMARY KEY,
    user_response varchar(255),
    user_name varchar(255)
);

# --- !Downs

DROP TABLE Submission;
