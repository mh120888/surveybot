# seed data

# --- !Ups

INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-6-06T11:39:50.640-07:00', 'STORY 10 4 15%', 'fakebob');
INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-6-07T11:39:50.640-07:00', 'MEETING 3', 'fakeangus');
INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-6-08T11:39:50.640-07:00', 'BUG 30 2 20%', 'fakeangus');

INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2017-6-08T11:39:50.640-07:00', 'BUG 130 2 20%', 'fakeangus');
INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-10-09T11:39:50.640-07:00', 'MEETING 7', 'fakedave');
INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-10-10T11:39:50.640-07:00', 'MEETING 7', 'fakedave');
INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-10-11T11:39:50.640-07:00', 'MEETING 8', 'fakedave');
INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-10-10T11:39:50.640-07:00', 'BUG 10 2 20%', 'fakemalina');
INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-10-10T11:39:50.640-07:00', 'BUG 1 6 40%', 'fakemalina');
INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-10-10T11:39:50.640-07:00', 'STORY TSF-490 5 30%', 'fakebob');
INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-10-10T11:39:50.640-07:00', 'STORY TSF-488 3 50%', 'fakebob');

# --- !Downs

DELETE FROM submissions WHERE user_name LIKE 'fake%';