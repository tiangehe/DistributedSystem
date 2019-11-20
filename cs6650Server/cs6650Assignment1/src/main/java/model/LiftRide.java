package model;

public class LiftRide {
  private int liftTime;
  private int resortID;
  private int seasonID;
  private int dayID;
  private int skierID;
  private String primaryKey;

  public LiftRide(int liftTime, int resortID, int seasonID, int dayID, int skierID,
      String primaryKey) {
    this.liftTime = liftTime;
    this.resortID = resortID;
    this.seasonID = seasonID;
    this.dayID = dayID;
    this.skierID = skierID;
    this.primaryKey = primaryKey;
  }

  public int getLiftTime() {
    return liftTime;
  }

  public void setLiftTime(int liftTime) {
    this.liftTime = liftTime;
  }

  public int getResortID() {
    return resortID;
  }

  public void setResortID(int resortID) {
    this.resortID = resortID;
  }

  public int getSeasonID() {
    return seasonID;
  }

  public void setSeasonID(int seasonID) {
    this.seasonID = seasonID;
  }

  public int getDayID() {
    return dayID;
  }

  public void setDayID(int dayID) {
    this.dayID = dayID;
  }

  public int getSkierID() {
    return skierID;
  }

  public void setSkierID(int skierID) {
    this.skierID = skierID;
  }

  public String getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(String primaryKey) {
    this.primaryKey = primaryKey;
  }

  @Override
  public String toString() {
    return "LiftRide{" +
        "liftTime=" + liftTime +
        ", resortID=" + resortID +
        ", seasonID=" + seasonID +
        ", dayID=" + dayID +
        ", skierID=" + skierID +
        ", primaryKey='" + primaryKey + '\'' +
        '}';
  }
}
