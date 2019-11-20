package model;

public class TotalVertical {
  private int totalDistance;
  private String primaryKey;

  public TotalVertical(int totalDistance, String primaryKey) {
    this.totalDistance = totalDistance;
    this.primaryKey = primaryKey;
  }

  public int getTotalDistance() {
    return totalDistance;
  }

  public void setTotalDistance(int totalDistance) {
    this.totalDistance = totalDistance;
  }

  public String getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(String primaryKey) {
    this.primaryKey = primaryKey;
  }
}
