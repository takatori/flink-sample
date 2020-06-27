package com.github.takatori

import com.github.takatori.sink.TweetElasticSearchSink
import com.github.takatori.source.TweetSource
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._

object Twitter2ElasticSearch extends App {

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  val stream: DataStream[String] = env
    .addSource(TweetSource.source)

  stream.addSink(TweetElasticSearchSink.builder.build())

  env.execute("twitter to elasticsearch")
}
