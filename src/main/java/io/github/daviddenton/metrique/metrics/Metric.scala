package io.github.daviddenton.metrique.metrics

trait Metric {
  def decrement(): Unit

  def increment(): Unit

  def gauge(value: Long): Unit

  def time(value: Long): Unit

  def count(value: Long): Unit
}
