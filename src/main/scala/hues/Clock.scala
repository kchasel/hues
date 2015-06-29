package hues

import java.util._
import java.util.concurrent.TimeUnit

import org.joda.time.DateTimeUtils

import scala.math._

class Clock(val latitude: Double, val longitude: Double, val julianDate: Double = Clock.julianDate()) {

  def sunrise = solarTransit - hourAngle / 360

  def sunset = solarTransit + hourAngle / 360

  lazy val hourAngle: Double = toDegrees(acos(
    (sinOfDeg(-0.83) - sinOfDeg(latitude)*sinOfDeg(declination))/(cosOfDeg(latitude)*cosOfDeg(declination))
  ))

  val sinOfDeg = (deg: Double) => sin(toRadians(deg))
  val cosOfDeg = (deg: Double) => cos(toRadians(deg))

  val n = {
    val nStar = { julianDate - Clock.julian2000 - (longitude/360) }
    round(nStar + .5)
  }

  val approxNoon: Double =  Clock.julian2000 + (longitude/360) + n

  val meanAnomaly: Double = (357.5291 + 0.98560028 * (approxNoon - 2451545)) % 360

  val center: Double = 1.9148*sinOfDeg(meanAnomaly) + 0.01993*sinOfDeg(2*meanAnomaly) + 0.0003*sinOfDeg(3*meanAnomaly)

  val eclipLongitude: Double = (meanAnomaly + 102.9372 + center + 180) % 360

  lazy val solarTransit: Double = approxNoon + 0.0053*sinOfDeg(meanAnomaly) - 0.0069*sinOfDeg(2*eclipLongitude)

  val declination: Double = toDegrees(asin(sinOfDeg(eclipLongitude)*sinOfDeg(23.45)))

}

object Clock {
  val julian2000 = 2451545.0009

  def julianDate(start: Date = new Date()): Double =
    floor(DateTimeUtils.toJulianDay(start.getTime))

  def julianToDate(jul: Double): Date = {
    new Date(DateTimeUtils.fromJulianDay(jul))
  }
}
