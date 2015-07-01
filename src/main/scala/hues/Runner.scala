package hues

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

class Runner(latitude: Option[Double], longitude: Option[Double]) {
  val solarClock = new SolarClock(latitude.getOrElse(40.0), longitude.getOrElse(-73.3))

  val periodInDays = (45.0/60)/24

  def start = {
    scheduleSunset()
  }

  val cal = java.util.Calendar.getInstance
  cal.set(2015, 6, 1, 16, 59, 0)

  def scheduleSunset(): Unit = {
    (new java.util.Timer(true)).schedule(new java.util.TimerTask() {
      override def run = {
        scheduleTimer(5000) {
          println("SETTING THE SUN")
        }
        scheduleSunrise()
      }
    }, cal.getTime)//SolarClock.julianToDate(solarClock.sunset - periodInDays))
  }

  def scheduleSunrise() = {
    Thread.sleep(15000)
    (new java.util.Timer(true)).schedule(new java.util.TimerTask() {
      override def run: Unit = {
        scheduleTimer(5000) {
          println("RAISING THE SUN")
        }
        scheduleSunset()
      }
    }, SolarClock.julianToDate(solarClock.sunrise + periodInDays))
  }


  def scheduleTimer(period: Int)(timerFn: => Unit) =
    (new java.util.Timer(true)).scheduleAtFixedRate(new java.util.TimerTask() {
      override def run = timerFn
    }, 0, period)
}
