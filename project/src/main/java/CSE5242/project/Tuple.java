package CSE5242.project;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Tuple {
	private Map<String, Comparable<Object>> fields = new HashMap<String, Comparable<Object>>();
	
	@SuppressWarnings("unchecked")
	public Tuple(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
//			System.out.println(rsmd.getColumnClassName(i));
			Comparable<Object> obj = (Comparable<Object>) rs.getObject(i);
			fields.put(rsmd.getColumnName(i), obj);
		}
	}
	
	public String toString() {
		return fields.toString();
	}
	
	public Comparable<Object> get(String fieldName) {
		return fields.get(fieldName);
	}
	
	public int compareTo(Tuple t, String fieldName) { //TODO Decide how we want to handle null values
//		System.out.println("Comparing field name " + fieldName + " of tuple " + this.get(fieldName) + "\nto tuple " + t.get(fieldName));
		if (this.get(fieldName) == null) {
			return (t.get(fieldName) == null ? 0 : -1);
		} else if (t.get(fieldName) == null) {
			return 1;
		}
		return this.get(fieldName).compareTo(t.get(fieldName));
	}
	
	public Set<String> getColumnNames() {
		return fields.keySet();
	}
}
