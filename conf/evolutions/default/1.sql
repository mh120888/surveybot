# submissions schema

# --- !Ups

CREATE TABLE submissions (
    id BIGSERIAL PRIMARY KEY,
    user_response varchar(255),
    user_name varchar(255)
);

# --- !Downs

DROP TABLE submissions;
