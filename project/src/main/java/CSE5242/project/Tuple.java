package CSE5242.project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSetMetaData;

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
	
	public int compareTo(Tuple t, String fieldName) {
		return this.get(fieldName).compareTo(t.get(fieldName));
	}
}
