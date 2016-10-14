package models

import org.joda.time.DateTime
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

@RunWith(classOf[JUnitRunner])
class DateCalculatorSpec extends Specification {
  "#getStartOfWeek" should {
    "returns a DateTime for Sunday, 10/2/2016 when given a DateTime for Wednesday, 10/5/2016" in {
      val wednesday = new DateTime(2016, 10, 5, 0, 0)
      val sunday = new DateTime(2016, 10, 2, 0, 0)

      val result = DateCalculator().getStartOfWeek(wednesday)

      result.toLocalDate.equals(sunday.toLocalDate) must equalTo(true)
    }

    "returns a DateTime for Sunday, 10/9/2016 when given a DateTime for Monday, 10/10/2016" in {
      val monday = new DateTime(2016, 10, 10, 0, 0)
      val sunday = new DateTime(2016, 10, 9, 0, 0)

      val result = DateCalculator().getStartOfWeek(monday)

      result.toLocalDate.equals(sunday.toLocalDate) must equalTo(true)
    }
  }

  "#getEndOfWeek" should {
    "returns a DateTime for Saturday, 10/8/2016 when given a DateTime for Wednesday, 10/5/2016" in {
      val wednesday = new DateTime(2016, 10, 5, 0, 0)
      val saturday = new DateTime(2016, 10, 8, 0, 0)

      val result = DateCalculator().getEndOfWeek(wednesday)

      result.toLocalDate.equals(saturday.toLocalDate) must equalTo(true)
    }
  }
}
