package io.github.daviddenton.metrique.metrics

import springagle.cloudfoundry.CfAppInstanceIndex
import springagle.util.{Environment, SystemName}

class Metrics private(parts: List[String], client: MetricsClient) {

  def this(systemName: SystemName, env: Environment, appInstanceIndex: CfAppInstanceIndex, client: MetricsClient) =
    this(List[String]("services", systemName.value, env.name, appInstanceIndex.index.toString).reverse, client)

  private val name = MetricName(parts.reverse.mkString("."))

  def child(newParts: String*): Metrics = new Metrics(newParts.reverse.toList ++ parts, client)

  def metric(newParts: String*): Metric = {
    val finalName = child(newParts: _*).name

    new Metric {
      override def count(value: Long): Unit = client.count(finalName, value)

      override def increment(): Unit = client.increment(finalName)

      override def decrement(): Unit = client.decrement(finalName)

      override def gauge(value: Long): Unit = client.gauge(finalName, value)

      override def time(value: Long): Unit = client.time(finalName, value)
    }
  }
}
