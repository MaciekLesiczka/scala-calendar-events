import org.joda.time._
import org.scalatest._
import org.scalatest.Matchers._
import ultriscalendar.values._
import ultriscalendar.extensions.Joda._
import org.joda.time.DateTimeConstants._

class JodaSpec extends FunSpec{

  describe("LocalDateExt"){
    describe("next"){
      val target = new LocalDate(2017,9,14)

      it("returns date in the next week if weekday is before argument"){
        target.next(1) should equal(new LocalDate(2017,9,18))
        target.next(2) should equal(new LocalDate(2017,9,19))
        target.next(3) should equal(new LocalDate(2017,9,20))
      }

      it("returns date in the current next week if weekday is after argument"){
        target.next(5) should equal(new LocalDate(2017,9,15))
        target.next(6) should equal(new LocalDate(2017,9,16))
        target.next(7) should equal(new LocalDate(2017,9,17))
      }

      it("returns date week before if day of week is same as in target"){
        val target = new LocalDate(2017,9,15)
        target.next(FRIDAY) should equal(new LocalDate(2017,9,22))
      }
    }

    describe("previous"){
      it("returns date week before if day of week is same as in target"){
        val target = new LocalDate(2016,7,3)
        target.previous(SUNDAY) should equal(new LocalDate(2016,6,26))
      }
    }
  }
}
