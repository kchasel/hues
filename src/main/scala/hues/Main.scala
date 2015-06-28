package hues

object Main {
  @volatile var keepRunning: Boolean = true

  def main(args: Array[String]) = {
    val mainThread = Thread.currentThread()
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run = { println("MADE IT HERE"); keepRunning = false; mainThread.join() }
    })

    Runner.beginTimer

    while(keepRunning) { }

    Runner.timer.cancel
    //Network.disconnect or whatever
    //Shutdown procedure? (turn off lights, leave at current, back to setting?)
    println("Nighty night")
  }
}
