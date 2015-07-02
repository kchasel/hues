package hues

import solar.Sunrise

class Runner(latitude: Option[Double], longitude: Option[Double]) {
  def start = {
    new Sunrise(latitude.getOrElse(Runner.NYC._1), longitude.getOrElse(Runner.NYC._2))
  }
}

object Runner {
  val NYC = (40.7127, -74.0059)
}
