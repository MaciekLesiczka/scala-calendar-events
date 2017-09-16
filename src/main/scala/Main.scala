import org.joda.time._
import org.joda.time.DateTimeConstants._
import ultriscalendar.entities._
import ultriscalendar.values._

object Main extends App {

  val series = new Series( new Weekly( new LocalDate(2017, 9, 18)
                                     , 2
                                    , NeverEnds
                                    , Set(TUESDAY, FRIDAY)
                                    )
                         )
  series.shift(new LocalDate(2018,1,26) -> new LocalDate(2018,1,25))

  val january2018calendar = series.getEvents(new DateRange( new LocalDate(2018, 1,  1)
                                                          , new LocalDate(2018, 1, 31)
                                                          )
                                            )

  println(january2018calendar.map(_.date))
}
