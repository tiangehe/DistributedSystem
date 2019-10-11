public class Main {
  public static void main(String[] args) {
    long wallTimeStart = System.currentTimeMillis();
    ClientThreadsGenerate clientThreads = new ClientThreadsGenerate();

    Thread[] startUpPhaseThreads= clientThreads.startUpPhase();
    Thread[] peakPhaseThreads = clientThreads.peakPhase();
    Thread[] coolDownPhaseThreads = clientThreads.coolDownPhase();

    clientThreads.getStartupPhaseLatch().countDown();

    JoinThreads(startUpPhaseThreads);
    JoinThreads(peakPhaseThreads);
    JoinThreads(coolDownPhaseThreads);

    long wallTime = System.currentTimeMillis() - wallTimeStart;

    Data.generateCSV(clientThreads.getData(), Constant.NUMTHREADS);
    Data.displayResult(Constant.NUMTHREADS, clientThreads.getData(),
        clientThreads.getNumSuccessRequest().intValue(),
        clientThreads.getNumFailedRequest().intValue(), wallTime);

    System.out.println("Main executed");
  }

  private static void JoinThreads(Thread[] threads) {
    for (Thread t : threads) {
      try {
        t.join();
      } catch (InterruptedException e) {
        System.out.println(e.getMessage());
      }
    }
  }
}
