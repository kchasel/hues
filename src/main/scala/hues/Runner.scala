package hues

import solar.Sunrise

class Runner(latitude: Option[Double], longitude: Option[Double]) {
  def start = {
    new Sunrise(latitude.getOrElse(40.3), longitude.getOrElse(-73.3))
  }
}
