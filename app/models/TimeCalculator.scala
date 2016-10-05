package models

import org.joda.time.DateTime

case class TimeCalculator() {
  private val END_OF_WEEK = 6

  def getStartOfWeek(day: DateTime): DateTime = {
    val dayOfWeek: Integer = day.getDayOfWeek
    day.minusDays(dayOfWeek)
  }

  def getEndOfWeek(day: DateTime): DateTime = {
    val dayOfWeek: Integer = day.getDayOfWeek
    val daysToAdd = END_OF_WEEK - dayOfWeek
    day.plusDays(daysToAdd)
  }
}
