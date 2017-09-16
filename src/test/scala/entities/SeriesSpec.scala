import org.scalatest._
import org.scalatest.Matchers._
import ultriscalendar.entities._
import ultriscalendar.values._
import org.joda.time.LocalDate

class SeriesSpec extends FunSpec{

  describe("getEvents"){
    val target = new Series(new Daily( new LocalDate(2017,9,16)
                                     , 4
                                     , EndDate(new LocalDate(2020,1,1))))

    it("returns events from given range"){
      target.getEvents(new DateRange(
          new LocalDate(2017,9,17)
        , new LocalDate(2017,9,29)
      )).map(_.date) should equal(List(
          new LocalDate(2017,9,20)
        , new LocalDate(2017,9,24)
        , new LocalDate(2017,9,28)
      ))
    }

    it("returns empty list of given range is before series"){
      target.getEvents(new DateRange(
          new LocalDate(2017,9,1)
        , new LocalDate(2017,9,15)
      )) shouldBe empty
    }

    it("returns empty list of given range is after series"){
      target.getEvents(new DateRange(
          new LocalDate(2027,9,1)
        , new LocalDate(2027,9,15)
      )) shouldBe empty
    }

    it("returns empty list of given range is between events"){
      target.getEvents(new DateRange(
          new LocalDate(2027,9,17)
        , new LocalDate(2027,9,19)
      )) shouldBe empty
    }

    it("applies shifts"){
      val shiftTarget = new Series(new Daily( new LocalDate(2017,9,16)
                                       , 2
                                       , Occurences(3)))
      shiftTarget.shift(new LocalDate(2017,9,16) -> new LocalDate(2017,9,27))
      shiftTarget.shift(new LocalDate(2017,9,18) -> new LocalDate(2017,9,15))

      shiftTarget.getEvents(new DateRange(
          new LocalDate(2017,9,1)
        , new LocalDate(2017,9,30)
      )).map(_.date) should equal(List(
          new LocalDate(2017,9,20)
        , new LocalDate(2017,9,27)
        , new LocalDate(2017,9,15)        
      ))
    }
  }

  describe("shift"){

    val createTarget = () => new Series(new Daily( new LocalDate(2017,9,16)
                                     , 2
                                     , Occurences(2)))

    it("shifts original event"){
      val target = createTarget()
      target.shift(new LocalDate(2017,9,16) -> new LocalDate(2017,9,17))

      target.eventExists(new LocalDate(2017,9,16)) shouldBe false
      target.eventExists(new LocalDate(2017,9,17)) shouldBe true

    }

    it("shifts shifted event"){
      val target = createTarget()
      target.shift(new LocalDate(2017,9,16) -> new LocalDate(2017,9,17))
      target.shift(new LocalDate(2017,9,17) -> new LocalDate(2017,9,19))

      target.eventExists(new LocalDate(2017,9,16)) shouldBe false
      target.eventExists(new LocalDate(2017,9,17)) shouldBe false
      target.eventExists(new LocalDate(2017,9,19)) shouldBe true
    }

    it("throws error if source event does not exists"){
      val target = createTarget()
      an [EventNotExistsException] should be thrownBy {
        target.shift(new LocalDate(2017,9,17) -> new LocalDate(2017,9,19))
      }
    }

    it("throws error if source event is shifted"){
      val target = createTarget()
      target.shift(new LocalDate(2017,9,16) -> new LocalDate(2017,9,17))
      an [EventNotExistsException] should be thrownBy {
        target.shift(new LocalDate(2017,9,16) -> new LocalDate(2017,9,19))
      }
    }

    it("throws error if target data contains event"){
      val target = createTarget()
      an [EventAlreadyExistsException] should be thrownBy {
        target.shift(new LocalDate(2017,9,16) -> new LocalDate(2017,9,18))
      }
    }

    it("throws error if target data contains shifted event"){
      val target = createTarget()
      target.shift(new LocalDate(2017,9,16) -> new LocalDate(2017,9,19))
      an [EventAlreadyExistsException] should be thrownBy {
        target.shift(new LocalDate(2017,9,18) -> new LocalDate(2017,9,19))
      }
    }

    it("shifts event back and forth"){
      val target = createTarget()
      target.shift(new LocalDate(2017,9,16) -> new LocalDate(2017,9,17))
      target.shift(new LocalDate(2017,9,17) -> new LocalDate(2017,9,16))
      target.shift(new LocalDate(2017,9,16) -> new LocalDate(2017,9,21))

      target.eventExists(new LocalDate(2017,9,16)) shouldBe false
      target.eventExists(new LocalDate(2017,9,17)) shouldBe false
      target.eventExists(new LocalDate(2017,9,21)) shouldBe true
    }
  }

  describe("eventExists"){
    val createTarget = () => new Series(new Daily( new LocalDate(2017,9,16)
                                     , 2
                                     , Occurences(2)))

    it("returns true if event is in original recurrence"){
      val target = createTarget()
      target.eventExists(new LocalDate(2017,9,18)) shouldBe true
    }

    it("returns false if event is not in original recurrence"){
      val target = createTarget()
      target.eventExists(new LocalDate(2017,9,19)) shouldBe false
      target.eventExists(new LocalDate(2017,9,20)) shouldBe false
    }

    it("returns true if event is shifted"){
      val target = createTarget()
      target.shift(new LocalDate(2017,9,18) -> new LocalDate(2017,9,19))
      target.eventExists(new LocalDate(2017,9,19)) shouldBe true
    }

    it("returns false if argument points to original date"){
      val target = createTarget()
      target.shift(new LocalDate(2017,9,18) -> new LocalDate(2017,9,19))
      target.eventExists(new LocalDate(2017,9,18)) shouldBe false
    }
  }
}
