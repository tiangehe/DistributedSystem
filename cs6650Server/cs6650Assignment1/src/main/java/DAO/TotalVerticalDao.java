package DAO;
import connectionManager.DBCPDataSource;
import java.sql.*;
import model.LiftRide;
import model.TotalVertical;
import org.apache.commons.dbcp2.*;

public class TotalVerticalDao {
  private static BasicDataSource dataSource;

  public TotalVerticalDao() {
    dataSource = DBCPDataSource.getDataSource();
  }

  public void createTotalVertical(TotalVertical newTotalVertical) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String insertQueryStatement = "INSERT INTO total_vertical (totalDistance, primaryKey) " +
        "VALUES(?,?)";
    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      preparedStatement.setInt(1, newTotalVertical.getTotalDistance());
      preparedStatement.setString(2, newTotalVertical.getPrimaryKey());

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

  public int getTotalVertical(String primaryKey) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String selectQueryStatement = "SELECT totalDistance FROM total_vertical WHERE primaryKey=?;";
    ResultSet resultSet = null;
    int totalVertical = 0;
    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(selectQueryStatement);
      preparedStatement.setString(1, primaryKey);
      resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        totalVertical = resultSet.getInt(1);
      }
      return totalVertical;
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
    return 0;
  }
}
