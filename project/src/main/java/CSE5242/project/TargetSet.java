package CSE5242.project;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TargetSet {
	private List<Tuple> tuples;
	private Map<String, List<String>> columnRanges;
	
	public TargetSet(Connection connection, String table, List<String> ids, List<String> columns, String idColumn) throws SQLException {
    	String query = "SELECT ";
//    	String selectColumns = "";
//    	for (String column : columns) {
//    		selectColumns += ", " + column;
//    	}
//    	query += selectColumns.substring(2) + " FROM " + table + " WHERE " + idColumn + " IN ";
    	query += "* FROM " + table + " WHERE " + idColumn + " IN ";
    	String idList = "";
    	for (String id : ids) {
    		idList += ", " + id;
    	}
		query += "(" + idList.substring(2) + ")";
        setTuplesFromQuery(connection, query);
        
        
    }
	
	public TargetSet(Connection connection, String query) throws SQLException {
		setTuplesFromQuery(connection, query);
	}
	
	
	private void setTuplesFromQuery(Connection connection, String query) throws SQLException {
		tuples = new ArrayList<Tuple>();
    	Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
	        Tuple t = new Tuple(resultSet);
	        tuples.add(t);
        }
        columnRanges = new HashMap<String, List<String>>();
        if (tuples.size() > 0) {
        	for (String columnName : tuples.get(0).getColumnNames()) {
        		Comparable<Object> min = getMinForField(tuples, columnName);
        		Comparable<Object> max = getMaxForField(tuples, columnName);
        		List<String> comparisons = new ArrayList<String>();
        		// TODO The syntax of the SQL this generates will only work for integers.  Need a way to detect type to add quotes for strings/dates
        		if (min.compareTo(max) == 0) { // They're equal
        			comparisons.add("= " + min.toString());
        		} else {
        			comparisons.add(">= " + min.toString());
        			comparisons.add("<= " + max.toString());
        			comparisons.add("BETWEEN " + min.toString() + " AND " + max.toString()); //TODO Figure out how to do NOT BETWEEN
        		}
        		columnRanges.put(columnName, comparisons);
        	}
        }
	}
	
	public boolean testQuery(Connection connection, String query) throws SQLException {
    	Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.last();
        return tuples.size() == resultSet.getRow();
    }
	
	public List<String> generateWhereClauses(int whereClauseCount) {
		List<String> whereClauses = new ArrayList<String>();
		if (whereClauseCount == 1) {
			for (String columnName : columnRanges.keySet()) {
				for (String rangeClause : columnRanges.get(columnName)) {
					whereClauses.add("WHERE " + columnName + " " + rangeClause);
				}
			}
		} else {
			List<String> baseClauses = generateWhereClauses(whereClauseCount - 1);
			for (String baseClause : baseClauses) {
				for (String columnName : columnRanges.keySet()) {
					String regex = ".*\\b" + columnName + "\\b.*";
					if (!Pattern.matches(regex, baseClause)) { // The word boundary characters guarantee an exact match
						System.out.println("\"" + baseClause + "\" does not match \"" + regex + "\"");
						for (String rangeClause : columnRanges.get(columnName)) {
							whereClauses.add(baseClause + " AND " + columnName + " " + rangeClause);
						}
					} else {
						System.out.println("\"" + baseClause + "\" matches \"" + regex + "\"");
					}
				}
			}
		}
		return whereClauses;
	}
	
	private static Comparable<Object> getMaxForField(List<Tuple> tuples, String fieldName) {
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
    
    private static Comparable<Object> getMinForField(List<Tuple> tuples, String fieldName) {
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
