package io.github.daviddenton.metrique;

public interface Metric {
  void decrement();

  void increment();

  void gauge(Long value);

  void time(Long value);

  void count(Long value);
}
