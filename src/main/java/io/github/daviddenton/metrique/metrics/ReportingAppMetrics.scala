package io.github.daviddenton.metrique.metrics

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Service, SimpleFilter}
import io.github.daviddenton.fintrospect.FintrospectModule._
import org.joda.time.Duration
import org.joda.time.Duration._
import springagle.finagle.ProvidesFinagleService
import springagle.util.{Clock, ControllableService}

trait ReportingAppMetrics extends ProvidesFinagleService with ControllableService {
  def clock: Clock

  def metrics: Metrics

  def metricsFrequency: Duration = standardSeconds(10)

  abstract override def service: Service[Request, Response] = RequestLatency(metrics, clock).andThen(super.service)

  private val metricsJvmReporting = new MetricsJvmReporting(metrics, metricsFrequency, this.clock)

  override def start(): Unit = {
    super.start()
    metricsJvmReporting.start()
  }

  override def stop(): Unit = {
    super.stop()
    metricsJvmReporting.stop()
  }
}

private case class RequestLatency(metrics: Metrics, clock: Clock) extends SimpleFilter[Request, Response] {
  override def apply(req: Request, service: Service[Request, Response]) = {
    val start = clock.now.getMillis
    for {
      resp <- service(req)
    } yield {
      def toStat(suffix: String): Seq[String] = List("http", req.headerMap.getOrElse(IDENTIFY_SVC_HEADER, req.getMethod().getName + ".UNMAPPED").replace('/', '_'), resp.status.getCode / 100 + "xx", resp.status.getCode.toString, suffix)

      metrics.metric(toStat("count"): _*).increment()
      metrics.metric(toStat("latency"): _*).time(clock.now.getMillis - start)
      resp
    }
  }
}