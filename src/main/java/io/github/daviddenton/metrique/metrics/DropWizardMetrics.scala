package io.github.daviddenton.metrique.metrics

import java.util.concurrent.TimeUnit

import com.codahale.metrics.MetricRegistry
import com.readytalk.metrics.StatsDReporter
import org.joda.time.Duration
import springagle.cloudfoundry.CfAppInstanceIndex
import springagle.util._

object DropWizardMetrics {

  def from(systemName: SystemName,
           env: Environment,
           appInstanceIndex: CfAppInstanceIndex,
           host: Host, port: Port, interval: Duration): Metrics with ControllableService = {
    val registry = new MetricRegistry()

    val client = new MetricsClient {
      override def decrement(name: MetricName): Unit = registry.counter(name.name).dec(1)

      override def count(name: MetricName, value: Long): Unit = registry.meter(name.name).mark(value)

      override def increment(name: MetricName): Unit = {
        registry.counter(name.name).inc(1)
      }

      override def gauge(name: MetricName, value: Long): Unit = registry.histogram(name.name).update(value)

      override def time(name: MetricName, value: Long): Unit = {
        registry.timer(name.name).update(value, TimeUnit.MILLISECONDS)
      }
    }

    new Metrics(systemName, env, appInstanceIndex, client)
      with ControllableService {

      private val reporter = StatsDReporter.forRegistry(registry).build(host.value, port.value)

      override def start() = reporter.start(interval.getMillis, TimeUnit.MILLISECONDS)

      override def stop() = reporter.stop()
    }
  }
}