package models

case class Validation(errorMessage: String, validationLogic: () => Boolean)