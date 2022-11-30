package Server;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBcontrol {
	public void test() {
		dbConnector db = new dbConnector();
		String sql = "SELECT 이름 FROM 교수";
		ResultSet rs = db.executeQuery(sql);
		try {
			while (rs.next()) {
				System.out.println(rs.getString("이름"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
					
				}
		}
	}
}
