package ultriscalendar.values

import org.joda.time._


class Daily(val startDate:LocalDate, val step:Int, val endCondition:EndCondition)
  extends Recurrence(Period.days){

  override def firstEventSince(date:LocalDate) =
    if (startDate.isAfter(date))
      startDate
    else {
      val period = new Period(startDate,date)
      if (period.getDays() % step == 0)
        date
      else date.plus (Period.days(step - period.getDays() % step))
    }
}
