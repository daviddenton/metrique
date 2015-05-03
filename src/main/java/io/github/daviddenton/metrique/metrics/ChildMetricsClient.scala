package io.github.daviddenton.metrique.metrics

class ChildMetricsClient(delegate: MetricsClient, prefix: String) extends MetricsClient {

  private def childMetric(name: MetricName): MetricName = {
    MetricName(prefix + "." + name)
  }

  override def decrement(name: MetricName): Unit = delegate.decrement(childMetric(name))

  override def count(name: MetricName, value: Long): Unit = delegate.count(childMetric(name), value)

  override def increment(name: MetricName): Unit = delegate.increment(childMetric(name))

  override def gauge(name: MetricName, value: Long): Unit = delegate.gauge(childMetric(name), value)

  override def time(name: MetricName, value: Long): Unit = delegate.time(childMetric(name), value)
}
