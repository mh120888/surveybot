# surveys schema
# survey_respondents schema

# --- !Ups

CREATE TABLE surveys (
    id BIGSERIAL PRIMARY KEY
);

CREATE TABLE survey_respondents (
    id BIGSERIAL PRIMARY KEY,
    user_name varchar(255),
    survey_id integer REFERENCES surveys
);

INSERT INTO survey_respondents (user_name) VALUES ('malina');
INSERT INTO survey_respondents (user_name) VALUES ('matt');

# --- !Downs

DROP TABLE surveys;
DROP TABLE survey_respondents;
