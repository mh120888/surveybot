package controllers

case class SlackPostDataRepository() {
  def getAll = {
    List("story: 5, total: 4, add: 0, remove: 1")
  }
}
