# seed data

# --- !Ups

INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-6-06T11:39:50.640-07:00', 'STORY 10 4 15%', 'fakebob');
INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-6-07T11:39:50.640-07:00', 'MEETING 3', 'fakeangus');
INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-6-08T11:39:50.640-07:00', 'BUG 30 2 20%', 'fakeangus');

INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-10-07T11:39:50.640-07:00', 'MEETING 7', 'fakedave');
INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-10-07T11:39:50.640-07:00', 'BUG 1 2 20%', 'fakemalina');
INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-10-07T11:39:50.640-07:00', 'STORY TSF-488 3 30%', 'fakebob');

# --- !Downs

DELETE FROM submissions WHERE user_name LIKE 'fake%';