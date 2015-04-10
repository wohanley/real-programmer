package twitter

import com.wohanley.robots.twitter._
import java.util.Timer
import java.util.TimerTask
import scala.None
import scala.Some
import core._
import twitter4j._


object Bot {

  val timer = new Timer()
  val twitter = new TwitterFactory(envConfig).getInstance()
  var listening = true

  def main(args: Array[String]): Unit = {

    val stream = new TwitterStreamFactory(envConfig).getInstance()
    stream.addListener(HoseListener)

    val filter = new FilterQuery()
    filter.language(Array("en"))
    filter.locations(Array(Array(-180, -90), Array(180, 90)))
    filter.filterLevel("low")

    stream.filter(filter)
  }

  class ListenToHose extends TimerTask {

    def run(): Unit = {
      listening = true
    }
  }

  object HoseListener extends StatusListener {

    def onStatus(status: Status): Unit = {
      if (listening) {
        action(status.getText()).map { action =>
          tweet(twitter)("real programmers " + action)
          listening = false
          timer.schedule(new ListenToHose, 3600000)
        }
      }
    }

    def onStallWarning(warning: twitter4j.StallWarning): Unit = {
      println("stall warning: " + warning.getMessage())
      println(warning.getPercentFull() + "% full")
    }

    // fuck all these
    def onDeletionNotice(x$1: twitter4j.StatusDeletionNotice): Unit = {}
    def onScrubGeo(x$1: Long,x$2: Long): Unit = {}
    def onTrackLimitationNotice(x$1: Int): Unit = {}
    def onException(x$1: Exception): Unit = {}
  }
}

