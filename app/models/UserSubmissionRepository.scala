package models

trait UserSubmissionRepository {
  def getAll: List[UserSubmission]
  def create(userSubmission: UserSubmission): Option[Long]
}
