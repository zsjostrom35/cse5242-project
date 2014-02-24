package CSE5242.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {

    private static Connection connection;
    
	private static void init() throws SQLException, ClassNotFoundException {
//	    String dbUrl = "jdbc:mysql://172.20.10.2:3306/chinook";
	    String dbUrl = "jdbc:mysql://192.168.0.3:3306/chinook";
	    String dbClass = "com.mysql.jdbc.Driver";
	    String username = "cse5242";
	    String password = "cse5242";
		Class.forName(dbClass);
		connection = DriverManager.getConnection(dbUrl, username, password);
	}
	
    public static void main( String[] args ) {
	    try {
	    	init();
	    	TargetSet ts = new TargetSet(connection, "SELECT * FROM artist");
	    	System.out.println(ts.generateWhereClauses(2));
	        connection.close();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
    }
}
