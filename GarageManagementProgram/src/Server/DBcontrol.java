package Server;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.RowSet;

import oracle.jdbc.rowset.OracleCachedRowSet;

public class DBcontrol {
	public OracleCachedRowSet test() throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT ������ȣ FROM ����";
		OracleCachedRowSet rs = db.executeQuery(sql);
		return rs;
	}
}
