package hues.solar

import java.util.{Date, Timer, Calendar}

abstract class SunEvent(lat: Double, lng: Double, on: Long = SolarClock.currentJulianDate()) {
  val solarClock = new SolarClock(lat, lng, on)

  val cal = java.util.Calendar.getInstance

  cal.add(Calendar.MINUTE, 1)

  val triggerTime: Double

  def enqueue(): Unit = {
    println(s"ENQUEEING $this AT ${SolarClock.julianToDate(triggerTime)}")
    Sun.timer.schedule(new java.util.TimerTask() {
      override def run: Unit = {
        trigger()
      }
    }, SolarClock.julianToDate(triggerTime))
  }

  def trigger() = {
    println(s"EVENT TRIGGERED: ${this}")
    val next = Sun.prepareNext(this)
    next.enqueue
  }

  def cancel() = Sun.timer.cancel
}

object Sun {
  val periodInDays = (45.0/60)/24
  val timer = new Timer(true)

  def prepareNext(existing: SunEvent): SunEvent = existing match {
    case Sunrise(lat, lng, _) => Sunset(lat, lng)
    case Sunset(lat, lng, _) => Sunrise(lat, lng)
  }
}

case class Sunrise(lat: Double, lng: Double, on: Long = SolarClock.currentJulianDate()) extends SunEvent(lat, lng, on) {
  val triggerTime = solarClock.sunrise
}

case class Sunset(lat: Double, lng: Double, on: Long = SolarClock.currentJulianDate()) extends SunEvent(lat, lng, on) {
  val triggerTime = solarClock.sunset - Sun.periodInDays
}
