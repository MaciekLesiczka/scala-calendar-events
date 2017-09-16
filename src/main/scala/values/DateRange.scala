package ultriscalendar.values

import org.joda.time._


class DateRange(val from:LocalDate, val to:LocalDate){
  def contains(date:LocalDate):Boolean = !(date.isBefore(from) || date.isAfter(to))
}
