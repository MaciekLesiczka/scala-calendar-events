package ultriscalendar.values
import org.joda.time._

trait EndCondition{}
case object NeverEnds extends EndCondition{}
case class Occurences(number:Int) extends EndCondition{}
case class EndDate(date:LocalDate) extends EndCondition{}
