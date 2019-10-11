import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Data {
  private long startTime;
  private long latency;
  private String requestMethod;
  private int responseCode;
  private static final int SECMINRATIO = 60*4;

  public Data(long startTime, long latency, String requestMethod, int responseCode) {
    this.startTime = startTime;
    this.latency = latency;
    this.requestMethod = requestMethod;
    this.responseCode = responseCode;
  }

  public static void generateCSV(BlockingQueue<Data> data, int numThread) {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream("./src/main/java/outputData/" +
          numThread +"threads_data.csv");
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,
          StandardCharsets.UTF_8);
      BufferedWriter writer = new BufferedWriter(outputStreamWriter);

      writer.write("Start Time,Request Method,Latency,Response Code");
      writer.newLine();

      for (Data entry: data) {
        StringBuffer currentLine = new StringBuffer();
        currentLine.append("" + entry.getStartTime() + "," + entry.getRequestMethod() + "," +
            entry.getLatency() + "," + entry.getResponseCode());
        writer.write(currentLine.toString());
        writer.newLine();
      }
      writer.flush();
      writer.close();
    } catch (IOException e) {
      System.out.println("IO Exception:" + e.getMessage());
    }
  }

  public static void displayResult(int numThread, BlockingQueue<Data> data, int numSuccessRequest,
      int numFailRequest, long wallTime) {
    List<Long> latencies = new ArrayList<>();
    int sumLatency = 0;
    int numRequest = 0;
    for (Data entry : data) {
      latencies.add(entry.getLatency());
      sumLatency += entry.getLatency();
      numRequest ++;
    }
    Collections.sort(latencies);

    System.out.println("=========================Result=========================");
    System.out.println("Number of Threads: " + numThread);
    System.out.println("Number of Successful Requests: " + numSuccessRequest);
    System.out.println("Number of Unsuccessful Requests: " + numFailRequest);
    System.out.println("Wall Time: " + wallTime*SECMINRATIO + "ms");
    System.out.println("Mean of All Latencies: " + sumLatency/numRequest*SECMINRATIO + "ms");
    System.out.println("Median of All Latencies: " + latencies.get(numRequest/2 - 1)*SECMINRATIO + "ms");
    System.out.println("Throughput: " + numRequest/wallTime + "requests/ms");
    System.out.println("99 Percentile of latencies: " + latencies.get(numRequest*99/100 - 1)*SECMINRATIO +
        "ms");
    System.out.println("Max Response Time: " + latencies.get(numRequest - 1)*SECMINRATIO + "ms");
    System.out.println("=========================================================");
  }

  public long getStartTime() {
    return startTime;
  }

  public long getLatency() {
    return latency;
  }

  public String getRequestMethod() {
    return requestMethod;
  }

  public int getResponseCode() {
    return responseCode;
  }
}
