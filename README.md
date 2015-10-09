Metrique [![Build Status](https://api.travis-ci.org/daviddenton/metrique.svg)](https://travis-ci.org/daviddenton/metrique) [![Coverage Status](https://coveralls.io/repos/daviddenton/metrique/badge.svg?branch=master)](https://coveralls.io/r/daviddenton/metrique?branch=master) [![Download](https://api.bintray.com/packages/daviddenton/maven/metrique/images/download.svg) ](https://bintray.com/daviddenton/maven/metrique/_latestVersion) [ ![Watch](https://www.bintray.com/docs/images/bintray_badge_color.png) ](https://bintray.com/daviddenton/maven/metrique/view?source=watch)
=========

Hierarchical metrics sent to StatsD.

###Get it:
Currently, the library is published in JCenter (and synced to Maven Central).

####Maven:
```XML
<dependency>
  <groupId>io.github.daviddenton</groupId>
  <artifactId>metrique</artifactId>
  <version>X.X.X</version>
</dependency>
```

####Release notes

#####v2.0.0
  - Split out Histogram and Gauge (was Gauge previously). Gauges are now used when you want to track the last specific value of a metric (such as a circuit being open). Use Histogram when you want all the Percentile stuff (for request latencies 
  etc..)