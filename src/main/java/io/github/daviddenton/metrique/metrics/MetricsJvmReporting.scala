package io.github.daviddenton.metrique.metrics

import java.lang.management.ManagementFactory.{getGarbageCollectorMXBeans, getMemoryMXBean}
import java.lang.management.{GarbageCollectorMXBean, MemoryMXBean}
import java.util.concurrent.{Executors, TimeUnit}

import org.joda.time.{DateTime, Duration}
import springagle.util.Clock

import scala.collection.JavaConverters._

class MetricsJvmReporting(private val metrics: Metrics, private val frequency: Duration, private val clock: Clock) {

  private val jvm = metrics.child("jvm")
  private val uptime = jvm.metric("uptime")

  private val heapMemoryMax = jvm.child("heapMemory").metric("max")
  private val heapMemoryUsed = jvm.child("heapMemory").metric("used")
  private val nonHeapMemoryMax = jvm.child("nonHeapMemory").metric("max")
  private val nonHeapMemoryUsed = jvm.child("nonHeapMemory").metric("used")

  private val executorService = Executors.newScheduledThreadPool(1, Executors.defaultThreadFactory())

  def start(): Unit = {
    getGarbageCollectorMXBeans.asScala.foreach {
      b => executorService.scheduleWithFixedDelay(reportGc(b), 0, frequency.getMillis, TimeUnit.MILLISECONDS)
    }
    executorService.scheduleWithFixedDelay(reportMemory(getMemoryMXBean), 0, frequency.getMillis, TimeUnit.MILLISECONDS)
    executorService.scheduleWithFixedDelay(reportUptime(clock.now), 0, frequency.getMillis, TimeUnit.MILLISECONDS)
  }

  private def reportGc(mxBean: GarbageCollectorMXBean): Runnable = new Runnable {
    val child = jvm.child("gc", mxBean.getName.replace(" ", ""))

    def run(): Unit = {
      child.metric("time").gauge(mxBean.getCollectionTime)
      child.metric("count").count(mxBean.getCollectionCount)
    }
  }

  private def reportMemory(mxBean: MemoryMXBean): Runnable = new Runnable {
    def run(): Unit = {
      heapMemoryMax.gauge(mxBean.getHeapMemoryUsage.getMax)
      heapMemoryUsed.gauge(mxBean.getHeapMemoryUsage.getUsed)
      nonHeapMemoryMax.gauge(mxBean.getNonHeapMemoryUsage.getMax)
      nonHeapMemoryUsed.gauge(mxBean.getNonHeapMemoryUsage.getUsed)
    }
  }

  private def reportUptime(startTime: DateTime): Runnable = new Runnable {
    def run(): Unit = {
      uptime.gauge((clock.now.getMillis - startTime.getMillis) / 1000)
    }
  }

  def stop(): Unit = {
    executorService.awaitTermination(1, TimeUnit.MILLISECONDS)
  }
}
