import org.scalatest._
import org.scalatest.Matchers._
import ultriscalendar.values._
import ultriscalendar.entities._
import org.joda.time._


class DateRangeSpec extends FunSpec{
  describe("contains"){
    val target = new DateRange(new LocalDate(2017,9,3),new LocalDate(2017,9,13))
    it("true if date in range"){
      target.contains(new LocalDate(2017,9,10)) should be (true)
    }

    it("true if date equals start date"){
      target.contains(new LocalDate(2017,9,3)) should be (true)
    }

    it("true if date equals end date"){
      target.contains(new LocalDate(2017,9,13)) should be (true)
    }

    it("false if date before range"){
      target.contains(new LocalDate(2017,9,1)) should be (false)
    }

    it("false if date after range"){
      target.contains(new LocalDate(2017,9,30)) should be (false)
    }
  }

}
