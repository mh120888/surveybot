## SurveyBot

#### Purpose
SurveyBot is an app that automates data collection through a Slack interface. This will give managers the ability to collect and view information about the accuracy of estimates and impediments to progress on stories among development teams. Furthermore, the system will allow admins to customize a list of recipients, create custom survey templates with unique questions, and view survey data.

#### Setup
Requires Java 1.8, Scala 2.11.8, and SBT

##### Installation
- `git clone git@gitlab.com:matthew.alan.higgins/SurveyBot.git`
- `sbt` to enter interactive mode
    - `compile` to compile source code

##### Tests
- `sbt` to enter interactive mode
    - `test` to run tests once
    - `~test` to run tests continuously (on any change to a source file)

##### Start Server
- `sbt` to enter interactive mode
    - `run` to run server
