package org.load;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Worker implements Runnable {
  private static String endpoint;

  public static void setEndpoint(String endpoint) {
    Worker.endpoint = endpoint;
  }

  public static List<Data> getListResponseDetails() {
    return lstResponseDetails;
  }

  private static List<Data> lstResponseDetails = new CopyOnWriteArrayList();

  public Worker() {}

  @Override
  public void run() {
    System.out.println("Sending request to "  + endpoint +  " by thread number: " + Thread.currentThread().getId());
    Data responseDetails = performRequestToAPI(endpoint, LoadTests.MAX_HTTP_REQUEST_NUMBER);
    System.out.println("Received response thread number: " + Thread.currentThread().getId());
    lstResponseDetails.add(responseDetails);
  }

  public Data performRequestToAPI(String endPoint, int requestNumber) {
    ArrayList<Long> delay = new ArrayList<Long>();
    double sum = 0;

    for (int i = 0; i < requestNumber; i++) {
      delay.add(given().get(endPoint).then().extract().time());
      sum += delay.get(i);
    }

    Collections.sort(delay);

    return new Data(sum / delay.size(), delay.get(delay.size() - 1));
  }
}
