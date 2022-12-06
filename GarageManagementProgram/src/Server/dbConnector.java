package Server;

import java.sql.*;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.swing.JOptionPane;

import oracle.jdbc.rowset.OracleCachedRowSet;

public class dbConnector {

	Connection conn; // java.sql.Connection
	Statement stmt = null;

	public dbConnector() {

		// �����ڰ� ����Ǹ� DB�� �ڵ� ����Ǿ� Connection ��ü ����

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@58.239.89.56:1299:ORCL", "c##bus", "1234");
			System.out.println("DB ���� �Ϸ�");
			stmt = conn.createStatement();

		} catch (ClassNotFoundException e) {
			System.out.println("JDBC ����̹� �ε� ����");
		} catch (SQLException e) {
			System.out.println("DB ���� ����");
			JOptionPane.showMessageDialog(null, "DB ���� ����", "DB ���� ����", JOptionPane.ERROR_MESSAGE);
		}
	}

	public OracleCachedRowSet executeQuery(String sql) throws SQLException {
		// SQL�� �����ϱ� ���� �޼ҵ� - Parameter : String��ü�� ���� SQL��
		// �������� ResultSet���� ��ȯ
		System.out.println(sql);
		ResultSet src = null;
		OracleCachedRowSet rs = new OracleCachedRowSet();
		try {
			src = stmt.executeQuery(sql);
			rs.populate(src);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println(src);
			System.out.println("SQL ���� ����");
			JOptionPane.showMessageDialog(null, "SQL ���� ����", "SQL ���� ����", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return rs;
	}

	public Connection getConnection() {
		//PreparedStatement�̿��� SQL �ۼ��� ��� Connection ��ü�� �ʿ��� ���� �޼ҵ�
		
		if(conn!=null) {
			return conn;
		}else {
			return null;
		}
		
	}
}