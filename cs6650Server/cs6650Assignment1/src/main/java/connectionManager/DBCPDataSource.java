package connectionManager;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBCPDataSource {
  private static BasicDataSource dataSource = new BasicDataSource();

  // NEVER store sensitive information below in plain text!
  private static final String HOST_NAME = "database-1.cibzxxfj4vcl.us-east-1.rds.amazonaws.com";
  private static final String PORT = "3306";
  private static final String DATABASE = "DistributedSystem";
  private static final String USERNAME = "admin";
  private static final String PASSWORD = "asdf469912573";

  static {
    // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);
    dataSource.setUrl(url);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    dataSource.setInitialSize(10);
    dataSource.setMinIdle(0);
//    dataSource.setMaxIdle(-1);
    dataSource.setMaxTotal(1000);
    dataSource.setMaxOpenPreparedStatements(1000);
  }
  public static BasicDataSource getDataSource() {
    return dataSource;
  }
}
