package ultriscalendar.values

import org.joda.time._
import org.joda.time.DateTimeConstants._
import ultriscalendar.extensions.Joda._


abstract class Recurrence(from: (Int=> Period)) {

  def startDate:LocalDate

  def step:Int
  if(step<1){
    throw new IllegalArgumentException("Step has to be greater than zero")
  }

  def endCondition:EndCondition

  def endDate : Option[LocalDate] = endCondition match {
    case NeverEnds => None
    case Occurences(n) => Some(getByOrdinal(n))
    case EndDate(date) => Some(date)
  }

  def getEventsSince(date: LocalDate) : Stream[LocalDate] = {
    val eventDate = firstEventSince(date)
    def outsideRange = (e:LocalDate) => endDate.map(_.isBefore(e)) getOrElse false

    def getEventsInRange(ed: LocalDate, sequence: Stream[LocalDate]): Stream[LocalDate] = {
      if ( outsideRange(ed) ) sequence else ed #:: getEventsInRange(next(ed),sequence)
    }
    getEventsInRange(eventDate, Stream.empty[LocalDate])
  }

  def firstEventSince(date:LocalDate) : LocalDate

  protected def next(date:LocalDate) = date.plus(from(step))
  protected def getByOrdinal(n:Int) = startDate.plus(from((n - 1)*step))
}
