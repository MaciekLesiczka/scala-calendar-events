import org.scalatest._
import org.scalatest.Matchers._
import ultriscalendar.values._
import ultriscalendar.entities._
import org.joda.time._


class RecurrenceSpec extends FunSpec{
  describe("constructor"){
    it("raises exception if step is less than 1"){
      an [IllegalArgumentException] should be thrownBy {     // Ensure a particular exception type is thrown
        new Daily(LocalDate.now(), -10, NeverEnds)
      }
    }
  }
}
