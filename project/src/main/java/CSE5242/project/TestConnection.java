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
	    	String columnsAndTable = "SELECT * FROM invoice";
	    	String query = columnsAndTable + " " +  "WHERE invoiceId IN (32, 184, 206, 390)";
	    	System.out.println("Target set generated with query \"" + query + "\"");
	    	TargetSet ts = new TargetSet(connection, query);
	    	System.out.println(generateQuery(ts, columnsAndTable));
	        connection.close();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
    }
    
    private static String generateQuery(TargetSet ts, String columnsAndTable) throws SQLException {
    	String query = "";
    	for (int i = 0; i < ts.getSize(); i++) {
//    	for (int i = 0; i < 2; i++) {
    		for (String where : ts.generateWhereClauses(i)) {
    			query = columnsAndTable + " " + where;
    			if (ts.testQuery(connection, query)) {
    				return query;
    			} else {
//    				System.out.println("Query " + query + " did not match the target set");
    			}
    		}
    	}
    	return "No query found";
    }
}
