import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.Thread;

public class ClientThreadsGenerate {
  private static final int NUMLIFT = 20;
  private static final String url = "http://localhost:8080/cs6650Server_war_exploded";

  private CountDownLatch startupPhaseLatch = new CountDownLatch(1);
  private CountDownLatch peakPhaseLatch =
      new CountDownLatch(Constant.NUMTHREADS/Constant.THREADSSCALEFACTOR/Constant.NEXTPHASEFACTOR);
  private CountDownLatch coolDownPhaseLatch =
      new CountDownLatch(Constant.NUMTHREADS/Constant.NEXTPHASEFACTOR);
  private AtomicInteger numSuccessRequest = new AtomicInteger(0);
  private AtomicInteger numFailedRequest = new AtomicInteger(0);
  private BlockingQueue<Data> data = new ArrayBlockingQueue<Data>(Constant.DATASIZE) {
  };

  public Thread[] startUpPhase() {
    int startTime = 0;
    int endTime = 90;
    int skierIDrange = Constant.NUMSKIERS/(Constant.NUMTHREADS/Constant.THREADSSCALEFACTOR);
    Thread[] threads = new Thread[Constant.NUMTHREADS/Constant.THREADSSCALEFACTOR];

    for (int i = 0; i < Constant.NUMTHREADS/Constant.THREADSSCALEFACTOR; i++) {
      threads[i] = new Thread(new ClientThread(numSuccessRequest, numFailedRequest,
          i*skierIDrange + 1, (i+1)*skierIDrange, startTime, endTime,NUMLIFT,
          Constant.NUMRUNS/Constant.NEXTPHASEFACTOR*skierIDrange,
          startupPhaseLatch, peakPhaseLatch, url, data));
      threads[i].start();
    }
    return threads;
  }

  public Thread[] peakPhase() {
    int startTime = 91;
    int endTime = 360;
    int skierIDrange = Constant.NUMSKIERS/Constant.NUMTHREADS;
    Thread[] threads = new Thread[Constant.NUMTHREADS];

    for (int i = 0; i < Constant.NUMTHREADS; i++) {
      threads[i] = new Thread(new ClientThread(numSuccessRequest, numFailedRequest,
          i*skierIDrange + 1, (i+1)*skierIDrange, startTime, endTime, NUMLIFT,
          (int) 0.8*Constant.NUMRUNS*skierIDrange,
          peakPhaseLatch, coolDownPhaseLatch, url, data));
      threads[i].start();
    }
    return threads;
  }

  public Thread[] coolDownPhase() {
    int startTime = 361;
    int endTime = 420;
    int skierIDrange = Constant.NUMSKIERS/(Constant.NUMTHREADS/Constant.THREADSSCALEFACTOR);
    Thread[] threads = new Thread[Constant.NUMTHREADS/Constant.THREADSSCALEFACTOR];

    for (int i = 0; i < Constant.NUMTHREADS/Constant.THREADSSCALEFACTOR; i++) {
      threads[i] = new Thread(new ClientThread(numSuccessRequest, numFailedRequest,
          i*skierIDrange + 1, (i+1)*skierIDrange, startTime, endTime, NUMLIFT,
          Constant.NUMRUNS/Constant.NEXTPHASEFACTOR,
          coolDownPhaseLatch, null, url, data));
      threads[i].start();
    }
    return threads;
  }

  public CountDownLatch getStartupPhaseLatch() {
    return startupPhaseLatch;
  }

  public CountDownLatch getPeakPhaseLatch() {
    return peakPhaseLatch;
  }

  public CountDownLatch getCoolDownPhaseLatch() {
    return coolDownPhaseLatch;
  }

  public AtomicInteger getNumSuccessRequest() {
    return numSuccessRequest;
  }

  public AtomicInteger getNumFailedRequest() {
    return numFailedRequest;
  }

  public BlockingQueue<Data> getData() {
    return data;
  }
}
