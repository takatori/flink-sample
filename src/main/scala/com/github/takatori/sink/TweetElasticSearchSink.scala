package com.github.takatori.sink

import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.streaming.connectors.elasticsearch.{
  ActionRequestFailureHandler,
  ElasticsearchSinkFunction,
  RequestIndexer
}
import org.apache.flink.streaming.connectors.elasticsearch7.ElasticsearchSink
import org.apache.http.HttpHost
import org.elasticsearch.action.ActionRequest
import org.elasticsearch.client.Requests

object TweetElasticSearchSink {

  val builder: ElasticsearchSink.Builder[String] = {
    val httpHost = new java.util.ArrayList[HttpHost]
    httpHost.add(new HttpHost("localhost", 9200, "http"))
    val sink = new ElasticsearchSink.Builder[String](
      httpHost,
      new SinkFunction()
    )
    sink.setFailureHandler(new ElasticSearchFailureHandler())
    sink.setBulkFlushBackoff(false)
    sink
  }

  private class SinkFunction extends ElasticsearchSinkFunction[String] {

    override def process(element: String, ctx: RuntimeContext, indexer: RequestIndexer): Unit = {
      indexer.add(Requests.indexRequest().index("tweet").source(element))
    }
  }

  private class ElasticSearchFailureHandler extends ActionRequestFailureHandler {

    override def onFailure(
        action: ActionRequest,
        failure: Throwable,
        restStatusCode: Int,
        indexer: RequestIndexer
    ): Unit = {}
  }
}
