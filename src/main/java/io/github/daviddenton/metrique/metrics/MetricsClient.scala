package io.github.daviddenton.metrique.metrics

trait MetricsClient {
  def decrement(name: MetricName): Unit

  def increment(name: MetricName): Unit

  def gauge(name: MetricName, value: Long): Unit

  def time(name: MetricName, value: Long): Unit

  def count(name: MetricName, value: Long): Unit
}
