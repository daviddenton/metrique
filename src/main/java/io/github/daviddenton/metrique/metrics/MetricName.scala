package io.github.daviddenton.metrique.metrics

case class MetricName private[metrics](name: String) {
  override def toString: String = name
}
