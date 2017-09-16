package ultriscalendar.values

import org.joda.time._
import org.joda.time.DateTimeConstants._
import ultriscalendar.extensions.Joda._


class Weekly( val startDateInput:LocalDate
            , val step:Int
            , val endCondition:EndCondition
            , daysOfWeekSet: Set[Int])
  extends Recurrence(Period.weeks){

  def daysOfWeek =
      if (daysOfWeekSet.isEmpty) List(startDateInput.getDayOfWeek)
      else daysOfWeekSet.toList.sorted

  override def startDate =
      if(daysOfWeek.isEmpty || daysOfWeek.contains(startDateInput.getDayOfWeek)) startDateInput
      else daysOfWeek.map(x=> startDateInput.next(x)).min

  override def firstEventSince(sinceDate:LocalDate) =
    if(!startDate.isBefore(sinceDate))
      startDate
    else {
      //the whole-week-distance between recurrence start date and given date
      var sinceDateWeekOrdinal = Weeks.weeksBetween(startDate, sinceDate.withDayOfWeek(startDate.getDayOfWeek)).getWeeks
      //next event in a week of given date
      val nextEventsInSinceDateWeek = daysOfWeek filter(_>=sinceDate.getDayOfWeek)

      if(sinceDateWeekOrdinal % step == 0 && !nextEventsInSinceDateWeek.isEmpty ){
        sinceDate.withDayOfWeek(nextEventsInSinceDateWeek min)
      }
      else {
        var nextWeekWithEvents = sinceDateWeekOrdinal + step - sinceDateWeekOrdinal % step;
        startDate.plusWeeks(nextWeekWithEvents)
                 .previous(SUNDAY)
                 .next(daysOfWeek(0))
      }
  }

  protected override def getByOrdinal(n:Int) = {
    val dayOfWeekShift = daysOfWeek.indexOf(startDate.getDayOfWeek())
    val weekNumber = (n + dayOfWeekShift -1) / daysOfWeek.length
    val dayIndex = (n + dayOfWeekShift -1) % daysOfWeek.length
    val dayOfWeek = daysOfWeek(dayIndex)
    startDate.plusWeeks(weekNumber*step)
             .withDayOfWeek(dayOfWeek)
  }

  protected override def next(eventDate: LocalDate) = {
    val index = daysOfWeek.indexOf(eventDate.getDayOfWeek())
    if(index < daysOfWeek.length - 1)
      eventDate.next(daysOfWeek(index + 1))
    else
      eventDate.plusWeeks(step).previous(daysOfWeek(0))
  }
}
