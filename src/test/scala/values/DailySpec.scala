import org.scalatest._
import org.scalatest.Matchers._
import ultriscalendar.values._
import ultriscalendar.entities._
import org.joda.time._


class DailySpec extends FunSpec{

  describe("firstEventSince"){
    val target = new Daily(new LocalDate(2016, 6, 9),2,NeverEnds)

    it("result is startDate if argument is before startDate"){
      target.firstEventSince(new LocalDate(2016, 6, 3)) should equal(new LocalDate(2016,6,9))
    }

    it("result is first date if argument is between events"){
      target.firstEventSince(new LocalDate(2016, 6, 12)) should equal(new LocalDate(2016,6,13))
    }

    it("result is equals argument if argument is in event"){
      target.firstEventSince(new LocalDate(2016, 6, 15)) should equal(new LocalDate(2016,6,15))
    }
  }

  describe("getEventsSince"){

    describe("with infinite recurrence"){
      val target = new Daily(new LocalDate(2016, 6, 9),2,NeverEnds)
      val events = target.getEventsSince(new LocalDate(2016, 6, 8)) take(3) toArray

      it("returns as many days as taken"){
        events.length should equal(3)
      }

      it("considers step"){
        Days.daysBetween(events(1), events(2))
            .getDays() should equal(2)
      }

      it("returns events in ascedning order"){
        events(0).isBefore(events(1)) should equal(true)
        events(1).isBefore(events(2)) should equal(true)
      }
    }

    describe("with number of occurences"){
      val target = new Daily(new LocalDate(2016, 6, 9),2,Occurences(5))
      val events = target.getEventsSince(new LocalDate(2016, 6, 8)) take(10) toArray

      it("returns as many days as number of Occurences"){
          events.length should equal(5)
      }
    }

    describe("with end date"){
      val target = new Daily(new LocalDate(2017, 9, 10),2,EndDate(new LocalDate(2017, 9, 15)))
      val events = target.getEventsSince(new LocalDate(2016, 6, 8)) take(10) toArray

      it("returns events until date"){
          events.length should equal(3)
      }

      it("last date is before end date"){
          events.last should equal(new LocalDate(2017, 9, 14))
      }
    }
  }
}
