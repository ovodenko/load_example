package org.load;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class LoadTests {
  private static final int MAX_THREAD_COUNT = 5;
  public static final int MAX_HTTP_REQUEST_NUMBER = 10;

  @Test
  public void checkMeanAndMaxResponseTimePositive() {
    final String END_POINT = "https://api.covid19api.com/summary";
    Worker.setEndpoint(END_POINT);

    List<Thread> workers = new ArrayList<>();

    for (int threadNumber = 0; threadNumber < MAX_THREAD_COUNT; threadNumber++) {
      Worker worker = new Worker();
      Thread thread = new Thread(worker);
      thread.start();
      workers.add(thread);
    }

    for (Thread worker : workers) {
      try {
        worker.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    List<Data> listResponseDetails = Worker.getListResponseDetails();

    assertMeanAndMaxResponseTime(listResponseDetails, 900, 2500);
  }

  private static void assertMeanAndMaxResponseTime(
      List<Data> lstResponseDetails, double expectedAvgMillis, double expectedMaxResponseMillis) {
    double actualAvgMillis = 0.0d;

    List<Double> maxes = new ArrayList<>();

    for (Data data : lstResponseDetails) {
      actualAvgMillis = actualAvgMillis + data.getAvg();
      maxes.add(data.getMax());
    }

    actualAvgMillis = actualAvgMillis / lstResponseDetails.size();
    Collections.sort(maxes);

    assertThat(expectedAvgMillis).as("mean delay time").isGreaterThan(actualAvgMillis);

    Double actualMaxResponseMillis = maxes.get(maxes.size() - 1);

    assertThat(expectedMaxResponseMillis).as("max delay time").isGreaterThan(actualMaxResponseMillis);

    System.out.println("Mean response time [ms]: " + actualAvgMillis);
    System.out.println("Max response time [ms]: " + actualMaxResponseMillis);
  }
}
