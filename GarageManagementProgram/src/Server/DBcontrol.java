package Server;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBcontrol {
	public void test() {
		dbConnector db = new dbConnector();
		String sql = "SELECT �̸� FROM ����";
		ResultSet rs = db.executeQuery(sql);
		try {
			while (rs.next()) {
				System.out.println(rs.getString("�̸�"));
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
