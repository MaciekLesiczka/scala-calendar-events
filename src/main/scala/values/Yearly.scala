package ultriscalendar.values

import org.joda.time._

class Yearly(val startDate:LocalDate, val step:Int, val endCondition:EndCondition)
  extends Recurrence(Period.years){
  override def firstEventSince(date:LocalDate) = throw new NotImplementedError
}
