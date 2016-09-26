## SurveyBot

## Purpose
SurveyBot is an app that automates data collection through a Slack interface. This will give managers the ability to collect and view information about the accuracy of estimates and impediments to progress on stories among development teams. Furthermore, the system will allow admins to customize a list of recipients, create custom survey templates with unique questions, and view survey data.

## Requirements
Requires Java 1.8, Scala 2.11.8, and SBT

## Installation
- `git clone git@gitlab.com:matthew.alan.higgins/SurveyBot.git`
- `sbt` to enter interactive mode
    - `compile` to compile source code

## Run Application
- `sbt` to enter interactive mode
    - `run` to run server

## Data Storage
The preferred way to run the application locally is to use Play's H2 in-memory database for data storage. To do so, two environment variables must first be set:
- `DATABASE_URL='jdbc:h2:mem:play;MODE=PostgreSQL;DB_CLOSE_DELAY=-1'`
- `DATABASE_DRIVER='org.h2.Driver'`

In the staging and production environments on Heroku, PostgreSQL is used instead. Deploying a Play application to Heroku automatically sets the `DATABASE_URL` to the appropriate value, but `DATABASE_DRIVER` must be set to `org.postgresql.Driver`.

## Run tests
- `sbt` to enter interactive mode
    - `test` to run tests once
    - `~test` to run tests continuously (on any change to a source file)
