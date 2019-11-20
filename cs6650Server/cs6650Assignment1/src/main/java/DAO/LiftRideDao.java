package DAO;
import connectionManager.DBCPDataSource;
import java.sql.*;
import model.LiftRide;
import org.apache.commons.dbcp2.*;

public class LiftRideDao {
  private static BasicDataSource dataSource;

  public LiftRideDao() {
    dataSource = DBCPDataSource.getDataSource();
  }

  public void createLiftRide(LiftRide newLiftRide) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String insertQueryStatement = "INSERT IGNORE INTO lift_ride (liftTime, resortID, seasonID, dayID, skierID, primaryKey) " +
        "VALUES (?,?,?,?,?,?)";
    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      preparedStatement.setInt(1, newLiftRide.getSkierID());
      preparedStatement.setInt(2, newLiftRide.getResortID());
      preparedStatement.setInt(3, newLiftRide.getSeasonID());
      preparedStatement.setInt(4, newLiftRide.getDayID());
      preparedStatement.setInt(5, newLiftRide.getLiftTime());
      preparedStatement.setString(6, newLiftRide.getPrimaryKey());

      // execute insert SQL statement
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }
}
