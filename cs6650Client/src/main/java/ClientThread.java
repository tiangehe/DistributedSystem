import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientThread implements Runnable {

  private AtomicInteger numSuccessRequest;
  private AtomicInteger numFailRequest;
  private int skierIDStart;
  private int skierIDEnd;
  private int startTime;
  private int endTime;
  private int numLift;
  private int runTime;
  private CountDownLatch currentLatch;
  private CountDownLatch nextLatch;
  private String serverUrl;
  private BlockingQueue<Data> data;

  public ClientThread(AtomicInteger numSuccessRequest,
      AtomicInteger numFailRequest, int skierIDStart, int skierIDEnd, int startTime, int endTime,
      int numLift, int runTime, CountDownLatch currentLatch,
      CountDownLatch nextLatch, String serverUrl, BlockingQueue<Data> data) {
    this.numSuccessRequest = numSuccessRequest;
    this.numFailRequest = numFailRequest;
    this.skierIDStart = skierIDStart;
    this.skierIDEnd = skierIDEnd;
    this.startTime = startTime;
    this.endTime = endTime;
    this.numLift = numLift;
    this.runTime = runTime;
    this.currentLatch = currentLatch;
    this.nextLatch = nextLatch;
    this.serverUrl = serverUrl;
    this.data = data;
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used to create a thread,
   * starting the thread causes the object's
   * <code>run</code> method to be called in that separately executing
   * thread.
   * <p>
   * The general contract of the method <code>run</code> is that it may take any action whatsoever.
   *
   * @see Thread#run()
   */
  @Override
  public void run() {
    try {
      currentLatch.await();

      SkiersApi skiersInstance = new SkiersApi();
      ApiClient client = skiersInstance.getApiClient();
      client.setBasePath(serverUrl);

      for (int i = 0; i < runTime; i++) {
        LiftRide body = new LiftRide();
        body.setTime(ThreadLocalRandom.current().nextInt(endTime-startTime+1) + startTime);
        body.setLiftID(ThreadLocalRandom.current().nextInt(numLift - 1) + 1);

        long requestSentTime = System.currentTimeMillis();
        try {
          skiersInstance.writeNewLiftRide(body, 3, "2019", "22",
              ThreadLocalRandom.current().nextInt(skierIDEnd - skierIDStart + 1) + skierIDStart);
          data.add(new Data(requestSentTime, System.currentTimeMillis()-requestSentTime,
              "POST",201));
          numSuccessRequest.getAndIncrement();
        } catch (ApiException e) {
          data.add(new Data(requestSentTime, System.currentTimeMillis() - requestSentTime,
              "Post", e.getCode()));
          numFailRequest.getAndIncrement();
          numFailRequest.getAndIncrement();
        }
      }

    } catch (InterruptedException e) {
      System.out.println("Interrupted Exception: " + e.getMessage());
    } finally {
      if (nextLatch != null) {
        nextLatch.countDown();
      }
    }
  }

  public AtomicInteger getNumSuccessRequest() {
    return numSuccessRequest;
  }

  public AtomicInteger getNumFailRequest() {
    return numFailRequest;
  }

  public int getSkierIDStart() {
    return skierIDStart;
  }

  public int getSkierIDEnd() {
    return skierIDEnd;
  }

  public int getStartTime() {
    return startTime;
  }

  public int getEndTime() {
    return endTime;
  }

  public int getNumLift() {
    return numLift;
  }

  public int getRunTime() {
    return runTime;
  }

  public CountDownLatch getCurrentLatch() {
    return currentLatch;
  }

  public CountDownLatch getNextLatch() {
    return nextLatch;
  }

  public String getServerUrl() {
    return serverUrl;
  }

  public BlockingQueue<Data> getData() {
    return data;
  }
}
