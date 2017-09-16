package ultriscalendar.entities

import org.joda.time._
import ultriscalendar.values._

class Series(recurrence:Recurrence){
  private var shifts = Map.empty[LocalDate, Event]
  private var shiftReverses = Map.empty[LocalDate, LocalDate]

  def getEvents(dateRange:DateRange) : List[Event] ={
    val stream = recurrence.getEventsSince(dateRange.from)
                           .takeWhile(dateRange.contains)
                           .filter(!shifts.contains(_))
                           .map(x=>new Event(x)) ++
                           shifts.values.filter(x=>dateRange.contains(x.date))
    stream.toList
  }

  def shift(fromTo: (LocalDate, LocalDate)) = fromTo match {
    case (from,to) =>
     val maybeOriginalEvent = recurrence.getEventsSince(from).headOption

     if(eventExists(to)){
       throw new EventAlreadyExistsException
     }
     if(!eventExists(from)){
       throw new EventNotExistsException
     }

     val unshiftedEvent =
       if(shiftReverses.contains(from)){
         val currentShift = shiftReverses(from)
         shifts -= currentShift
         shiftReverses -= from
         currentShift
       }
       else from

     shifts += (unshiftedEvent -> new Event(to))
     shiftReverses += (to -> unshiftedEvent)
  }

  def eventExists(eventDate: LocalDate) = {
    eventExistsAsUnshifted(eventDate) &&
    !shifts.contains(eventDate) ||
    shiftReverses.contains(eventDate)
  }

  private def eventExistsAsUnshifted(eventDate: LocalDate) = {
    val existsAsUnshifted = for {
      first <- recurrence.getEventsSince(eventDate).headOption
    } yield first == eventDate
    existsAsUnshifted getOrElse(false)
  }
}

case class EventAlreadyExistsException( private val message: String = "")
   extends Exception(message, None.orNull)

case class EventNotExistsException( private val message: String = "")
  extends Exception(message, None.orNull)
