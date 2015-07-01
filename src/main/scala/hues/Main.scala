package hues

object Main {
  @volatile var keepRunning: Boolean = true

  def main(args: Array[String]) = {
    val mainThread = Thread.currentThread()
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run = { println("MADE IT HERE"); keepRunning = false; mainThread.join() }
    })

    val runner = new Runner(args.lift(0).map(_.toDouble), args.lift(1).map(_.toDouble))

    runner.start

    while(keepRunning) { }

    //runner.timer.cancel
    //Network.disconnect or whatever
    //Shutdown procedure? (turn off lights, leave at current, back to setting?)
    println("Nighty night")
  }
}
