package com.github.takatori

import java.util.Properties

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object Twitter2ElasticSearch extends App {

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  val props = new Properties()
  props.setProperty(TwitterSource.CONSUMER_KEY, "")
  props.setProperty(TwitterSource.CONSUMER_SECRET, "")
  props.setProperty(TwitterSource.TOKEN, "")
  props.setProperty(TwitterSource.TOKEN_SECRET, "")
  val input = env.addSource(new TwitterSource(props))

}
