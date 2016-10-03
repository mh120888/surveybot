package models

import scala.collection.mutable.ArrayBuffer

trait Validatable {
  def isValid(): Boolean = {
    this.validate()
    if (this.errors.isEmpty) true else false
  }

  private def validate(): Unit = {
    for (validation <- validations) {
      if (!validation.validationLogic(validation.field)) this.errors += validation.errorMessage
    }
  }

  def getErrors: Seq[String] = {
    this.errors
  }

  var errors = ArrayBuffer[String]()

  val validations: Seq[Validation]
}
