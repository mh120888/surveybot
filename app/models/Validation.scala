package models

case class Validation(field: String = "", errorMessage: String, validationLogic: Function[String, Boolean])
