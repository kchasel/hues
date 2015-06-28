package hues

import java.util._
import java.util.concurrent.TimeUnit

object Clock {
  val julianStart = {
    val cal = Calendar.getInstance
    cal.set(4713, 0, 1, 0, 0)
    cal.set(Calendar.ERA, GregorianCalendar.BC)
    cal.getTime
  }
  def julianDate(start: Date = new Date) = {
    val diff = start.getTime - julianStart.getTime

    println(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))
  }
}
