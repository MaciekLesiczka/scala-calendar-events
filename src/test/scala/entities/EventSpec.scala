import org.scalatest._
import org.scalatest.Matchers._
import ultriscalendar.entities._
import org.joda.time.LocalDate

class EventSpec extends FunSpec{
  describe("constructor"){
    val target = new Event(new LocalDate(2017,9,3))    
    it("initializes date"){
      target.date should equal(new LocalDate(2017,9,3))
    }
  }
}
