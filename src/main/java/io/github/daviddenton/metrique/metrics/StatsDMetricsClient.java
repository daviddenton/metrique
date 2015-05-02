package io.github.daviddenton.metrique.metrics;

import com.timgroup.statsd.NonBlockingStatsDClient;

  public class StatsDMetricsClient implements MetricsClient {

      //        def fromConfig(systemName:SystemName,env:Environment,cfAppInstanceIndex:CfAppInstanceIndex,statsDHost:Host,statsDPort:Port)={
//        new Metrics(systemName,env,cfAppInstanceIndex,new StatsDMetricsClient(statsDHost,statsDPort))
//        }

      private final String statsDHost;
      private final int statsDPort;
      private final NonBlockingStatsDClient  statsD = new NonBlockingStatsDClient("", statsDHost.value, statsDPort.value)

      StatsDMetricsClient(String statsDHost, int statsDPort) {


          this.statsDHost = statsDHost;
          this.statsDPort = statsDPort;
      }


    override def decrement(metricName: MetricName): Unit = statsD.decrement(metricName.name)

    override def increment(metricName: MetricName): Unit = statsD.increment(metricName.name)

    override def gauge(metricName: MetricName, value: Long): Unit = statsD.gauge(metricName.name, value)

    override def time(metricName: MetricName, value: Long): Unit = statsD.time(metricName.name, value)

    override def count(metricName: MetricName, value: Long): Unit = statsD.count(metricName.name, value)
  }

}
