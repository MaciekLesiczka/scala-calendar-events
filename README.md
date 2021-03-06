# ultriscalendar

## Domain modeling sample for event series

This project is an example of modeling techniques in Scala. The domain in question is event series that are one of the most important features of calendar applications like Outlook or Google Calendar. The project shows approach to recurring events that can be occasionally shifted. 

This sample doesn't take into consideration any architectural topics but rather implements the core logic that can be good starting point for building an application on top of it: persistence, UI/management module, alerting etc.

This is a port from identical project written in C# 5.0 that can be found here: https://github.com/MaciekLesiczka/ultricalendar/tree/master/Ultricalendar.Domain


### Example - infinite weekly events with one event instance shift

Let's create an event that, starting from September 18th, 2017, will occur every every other week, on Tuesday and Friday. Series never ends. Additionally, an event on January 26th, 2018 is moved to one day before. Let's print all events in January 2018:

``` scala
import org.joda.time._
import org.joda.time.DateTimeConstants._
import ultriscalendar.entities._
import ultriscalendar.values._

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
```

yields:

```
List(2018-01-09, 2018-01-12, 2018-01-23, 2018-01-25)
```
