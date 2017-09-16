import org.scalatest._
import org.scalatest.Matchers._
import org.joda.time._
import org.joda.time.DateTimeConstants._
import ultriscalendar.values._
import ultriscalendar.entities._


class WeeklySpec extends FunSpec{
  val everyOtherWorkday = Set(MONDAY,WEDNESDAY,FRIDAY)

  describe("StartDate"){
    it("is after entered one if day of week doesn't match"){
      val target = new Weekly(new LocalDate(2016,6,9), 2, Occurences(2), everyOtherWorkday)

      target.startDate should equal(new LocalDate(2016,6,10))
    }

    it("is a date from another week if there is no dates in a week of startDate argument"){
      val target = new Weekly(new LocalDate(2017,9,10), 2, Occurences(2), Set(THURSDAY))

      target.startDate should equal(new LocalDate(2017,9,14))
    }
  }

  describe("daysOfWeek"){
    it("is determined by startDate if days of week are not specified"){
      val target = new Weekly(new LocalDate(2016,6,9), 2, Occurences(2), Set.empty[Int])

      target.daysOfWeek should equal(List(THURSDAY))
    }
  }

  describe("endDate"){
    it("determined by occurences counting every single event (not weeks)"){
      val target = new Weekly(new LocalDate(2016,6,10), 1, Occurences(5), everyOtherWorkday)

      target.endDate should equal(Some(new LocalDate(2016,6,20)))
    }

    it("determined by step counting weeks"){
      val target = new Weekly(new LocalDate(2016,6,10), 2, Occurences(5), everyOtherWorkday)

      target.endDate should equal(Some(new LocalDate(2016,7,4)))
    }
  }

  describe("firstEventSince"){
    val target = new Weekly(new LocalDate(2016,6,10), 2, NeverEnds, everyOtherWorkday)

    it("returns startDate as a first event if sinceDate is before startDate"){
       target.firstEventSince(new LocalDate(2016, 6, 3)) should equal(new LocalDate(2016,6,10))
    }

    it("returns startDate as a first event if sinceDate equals startDate"){
       target.firstEventSince(new LocalDate(2016, 6, 10)) should equal(new LocalDate(2016,6,10))
    }

    it("returns first date after sinceDate if it is between events"){
      target.firstEventSince(new LocalDate(2016, 6, 21)) should equal(new LocalDate(2016,6,22))
    }

    it("returns first date from next week if sinceDate if is between weeks"){
      target.firstEventSince(new LocalDate(2016, 6, 12)) should equal(new LocalDate(2016,6,20))
      target.firstEventSince(new LocalDate(2016, 6, 15)) should equal(new LocalDate(2016,6,20))
      target.firstEventSince(new LocalDate(2016, 6, 19)) should equal(new LocalDate(2016,6,20))
    }

    it("returns sinceDate if an events occurs then"){
      val target = new Weekly(new LocalDate(2017,9,4), 3, NeverEnds, Set(THURSDAY))
      target.firstEventSince(new LocalDate(2017, 9, 28)) should equal(new LocalDate(2017,9,28))
    }
  }
  describe("getEventsSince"){
    it("returns first 4 expected dates when recurrence never ends"){
      val target = new Weekly(new LocalDate(2016,6,1), 3, NeverEnds, everyOtherWorkday)

      target.getEventsSince(new LocalDate(2016,6,1))
            .take(4).toList should equal(
              List( new LocalDate(2016, 6, 1)
                  , new LocalDate(2016, 6, 3)
                  , new LocalDate(2016, 6, 20)
                  , new LocalDate(2016, 6, 22))
            )
    }

    it("returns all remaining occurences if they are finite and start date is in the middle"){
      val target = new Weekly( new LocalDate(2016,6,11)
                             , 3
                             , Occurences(9)
                             , Set(MONDAY,SUNDAY))

      target.getEventsSince(new LocalDate(2016,8,18))
            .toList should equal(List( new LocalDate(2016,8,29)
                                     , new LocalDate(2016,9,4)))

    }

    it("returns empty list if startDate is after recurrence end date"){
      val target = new Weekly( new LocalDate(2016,6,11)
                             , 3
                             , Occurences(9)
                             , Set.empty[Int])

      target.getEventsSince(new LocalDate(2020,1,18))
            .toList should equal(List.empty[LocalDate])
    }
  }
}
