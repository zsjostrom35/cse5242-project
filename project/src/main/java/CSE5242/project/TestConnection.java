package CSE5242.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TestConnection {
	
    public static void main( String[] args ) {
	    String dbUrl = "jdbc:mysql://172.20.10.2:3306/chinook";
//	    String dbUrl = "jdbc:mysql://192.168.0.3:3306/chinook";
	    String dbClass = "com.mysql.jdbc.Driver";
	    String query = "Select * from employee";
	    String username = "cse5242";
	    String password = "cse5242";
	    try {
	
	        Class.forName(dbClass);
	        Connection connection = DriverManager.getConnection(dbUrl,
	            username, password);
	        Statement statement = connection.createStatement();
	        ResultSet resultSet = statement.executeQuery(query);
	        List<Tuple> tuples = new ArrayList<Tuple>();
	        while (resultSet.next()) {
		        Tuple t = new Tuple(resultSet);
		        tuples.add(t);
		        System.out.println(t);
	        }
	        resultSet.last();
	        connection.close();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
    }
    
    public static Comparable<Object> getMaxForField(List<Tuple> tuples, String fieldName) {
    	if (tuples.size() == 0) {
    		return null; 
    	}
    	Tuple val = tuples.get(0);
    	for (int i = 1; i < tuples.size(); i++) {
    		if (val.compareTo(tuples.get(i), fieldName) < 0) {
    			val = tuples.get(i);
    		}
    	}
    	return val.get(fieldName);
    }
    
    public static Comparable<Object> getMinForField(List<Tuple> tuples, String fieldName) {
    	if (tuples.size() == 0) {
    		return null; 
    	}
    	Tuple val = tuples.get(0);
    	for (int i = 1; i < tuples.size(); i++) {
    		if (val.compareTo(tuples.get(i), fieldName) > 0) {
    			val = tuples.get(i);
    		}
    	}
    	return val.get(fieldName);
    }
}
