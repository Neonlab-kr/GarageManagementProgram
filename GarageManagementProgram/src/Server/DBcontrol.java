package Server;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.RowSet;

import oracle.jdbc.rowset.OracleCachedRowSet;

public class DBcontrol {
	public OracleCachedRowSet test() throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT 버스번호 FROM 버스";
		OracleCachedRowSet rs = db.executeQuery(sql);
		return rs;
	}
}
