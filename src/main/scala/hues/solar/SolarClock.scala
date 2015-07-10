package hues.solar

import java.util._
import java.util.concurrent.TimeUnit

import org.joda.time.DateTimeUtils

import scala.math._

// latitude in fractional degrees, N = +
// lng in fractional degrees, E = +
// julianDate of
class SolarClock(val latitude: Double, lng: Double, val julianDate: Long = SolarClock.currentJulianDate()) {
  // reverse passed lng since these equations are based upon longitude W = +
  val longitude: Double = -lng

  val sinOfDeg = (deg: Double) => sin(toRadians(deg))
  val cosOfDeg = (deg: Double) => cos(toRadians(deg))

  val n = {
    val nStar = { julianDate - SolarClock.julian2000 - (longitude/360) }
    round(nStar + .5)
  }

  val approxNoon: Double =  SolarClock.julian2000 + (longitude/360) + n

  val meanAnomaly: Double = (357.5291 + 0.98560028 * (approxNoon - 2451545)) % 360

  val center: Double = 1.9148*sinOfDeg(meanAnomaly) + 0.01993*sinOfDeg(2*meanAnomaly) + 0.0003*sinOfDeg(3*meanAnomaly)

  val eclipLongitude: Double = (meanAnomaly + 102.9372 + center + 180) % 360

  val solarTransit: Double = approxNoon + 0.0053*sinOfDeg(meanAnomaly) - 0.0069*sinOfDeg(2*eclipLongitude)

  val declination: Double = toDegrees(asin(sinOfDeg(eclipLongitude)*sinOfDeg(23.45)))

  val hourAngle: Double = toDegrees(acos(
    (sinOfDeg(-0.83) - sinOfDeg(latitude)*sinOfDeg(declination))/(cosOfDeg(latitude)*cosOfDeg(declination))
  ))

  val sunrise: Double = solarTransit - hourAngle / 360

  val sunset: Double = solarTransit + hourAngle / 360

  val hoursOfSun: Double = (sunset - sunrise) * 24

  // Calculations follow
}

object SolarClock {
  val julian2000 = 2451545.0009

  def currentJulianDate(start: Date = new Date()): Long =
    floor(DateTimeUtils.toJulianDay(start.getTime)).toLong

  def julianToDate(jul: Double): Date = {
    new Date(DateTimeUtils.fromJulianDay(jul))
  }
}
