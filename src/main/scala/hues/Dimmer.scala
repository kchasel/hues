package hues

object Dimmer {
  def by(brighter: Boolean, percentage: Float) =
    println(s"SEND REQUEST FOR $brighter by ${percentage}%")
}
