package com.github.takatori.source

import java.util.Properties
import org.apache.flink.streaming.connectors.twitter.TwitterSource

object TweetSource {

  private val props = new Properties()
  props.setProperty(TwitterSource.CONSUMER_KEY, "")
  props.setProperty(TwitterSource.CONSUMER_SECRET, "")
  props.setProperty(TwitterSource.TOKEN, "")
  props.setProperty(TwitterSource.TOKEN_SECRET, "")

  val source: TwitterSource = new TwitterSource(props)

}
