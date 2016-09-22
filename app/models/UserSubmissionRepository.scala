package models

trait UserSubmissionRepository {
  def getAll: List[UserSubmission]
}
