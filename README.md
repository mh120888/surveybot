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

## Slack API Tokens
In order for the application to work with Slack, you must first have a Slack team, then you must set up a `slash command` through Slack's admin interface - see [this](https://api.slack.com/slash-commands) for more information.
- There are currently two API tokens that need to be set as environment variables.
    - `SURVEY_SLASH_COMMAND_TOKEN` is used by this application to validate that a request came from Slack. Its value can be found on the Configuration page for the `/survey` slash command.
    - `BOT_USER_TOKEN` is used by Slack to authenticate an API request from this application. Its value can be found by setting up the `Slack API Tester` for your Slack team.

## Data Storage
The preferred way to run the application locally is to use Play's H2 in-memory database for data storage. To do so, two environment variables must first be set:
- `export DATABASE_URL='jdbc:h2:mem:play;MODE=PostgreSQL;DB_CLOSE_DELAY=-1'`
- `export DATABASE_DRIVER='org.h2.Driver'`

In the staging and production environments on Heroku, PostgreSQL is used instead. Deploying a Play application to Heroku automatically sets the `DATABASE_URL` to the appropriate value, but `DATABASE_DRIVER` must be set to `org.postgresql.Driver`.

## Run tests
- `sbt` to enter interactive mode
    - `test` to run tests once
    - `~test` to run tests continuously (on any change to a source file)
