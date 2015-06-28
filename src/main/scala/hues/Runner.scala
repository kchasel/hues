package hues

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

object Runner {
  val timer = new java.util.Timer

  def beginTimer =
    timer.schedule(new java.util.TimerTask() {
      override def run = {
        Future {
          Dimmer.by(true, 5)
        }
      }
    }, 0, 5000)
}
