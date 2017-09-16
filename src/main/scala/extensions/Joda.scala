package ultriscalendar.extensions
import org.joda.time._

object Joda{

  implicit class LocalDateExt(date: LocalDate) {
      def next(dayOfWeek: Int) = {
        val inTheSameWeek = date.withDayOfWeek(dayOfWeek)
        if(!inTheSameWeek.isAfter(date)) inTheSameWeek.plusWeeks(1) else inTheSameWeek
      }
      def previous(dayOfWeek: Int) = {
        val inTheSameWeek = date.withDayOfWeek(dayOfWeek)
        if(!inTheSameWeek.isBefore(date)) inTheSameWeek.minusWeeks(1) else inTheSameWeek
      }
    }

  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.fromLessThan(_ isBefore _)
}
