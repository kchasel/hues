package hues

import java.util._
import java.util.concurrent.TimeUnit

import scala.math._

class Clock(latitude: Double, longitude: Double, julianDate: Long = Clock.julianDate()) {

  def sunrise = solarTransit - hourAngle / 360

  def sunset = solarTransit + hourAngle / 360

  lazy val hourAngle = toDegrees(acos(
    (sinOfDeg(-0.83) - sinOfDeg(latitude)*sinOfDeg(declination))/(cosOfDeg(latitude)*cosOfDeg(declination))
  ))

  val sinOfDeg = (deg: Double) => sin(toRadians(deg))
  val cosOfDeg = (deg: Double) => cos(toRadians(deg))

  val n = {
    val nStar = { julianDate - Clock.julian2000 - (longitude/360) }
    round(nStar) //nStar + 1/2
  }

  val approxNoon = Clock.julian2000 + (longitude/360) + n

  val meanAnomaly = (357.5291 + 0.98560028 * (approxNoon - 2451545)) % 360

  val center =
    1.9148*sin(toRadians(meanAnomaly)) + 0.0200*sin(toRadians(2*meanAnomaly)) + 0.0003*sin(toRadians(3*meanAnomaly))

  val eclipLongitude = (meanAnomaly + 102.9372 + center + 180) % 360

  lazy val solarTransit = approxNoon + 0.0053*sin(toRadians(meanAnomaly)) - 0.0069*sin(toRadians(2*eclipLongitude))

  val declination = toDegrees(asin(sin(toRadians(eclipLongitude))*sin(toRadians(23.45))))

}

object Clock {
  val julianStart = {
    val cal = Calendar.getInstance
    cal.set(4713, 0, 1, 0, 0)
    cal.set(Calendar.ERA, GregorianCalendar.BC)
    cal.getTime
  }
  lazy val julian1970 = {
    val cal = new GregorianCalendar()
    cal.set(1970, 0, 1, 0, 0, 0)
    julianDate(cal.getTime)
  }
  val julian2000 = 2451545.0009

  def julianDate(start: Date = new Date):Long = {
    val diff = start.getTime - julianStart.getTime

    TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
  }

  def julianToDate(jul: Double): Date = {
    val cal = new GregorianCalendar()
    val exactDays = jul - julian1970
    val exactHours = exactDays * 24
    val hours = floor(exactHours).toInt
    val minutes = ((exactHours % 1) * 60).toInt
    println(hours + " " + minutes)
    cal.setTime(new Date(0))
    cal.add(Calendar.HOUR_OF_DAY, hours)
    cal.add(Calendar.MINUTE, minutes)
    cal.getTime
  }
}
