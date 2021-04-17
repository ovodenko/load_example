package org.load;

public class Data {
  private final double avg;
  private final double max;

  public Data(double avg, double max) {
    this.avg = avg;
    this.max = max;
  }

  public double getAvg() {
    return avg;
  }

  public double getMax() {
    return max;
  }
}
