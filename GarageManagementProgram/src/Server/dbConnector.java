package Server;

import java.sql.*;

import javax.swing.JOptionPane;

public class dbConnector {

	Connection conn; // java.sql.Connection
	Statement stmt = null;

	
	public dbConnector() {
		
		// �����ڰ� ����Ǹ� DB�� �ڵ� ����Ǿ� Connection ��ü ����
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@58.239.89.56:1299:ORCL", "c##bus","1234"); //�츮 DB�� ���� ���� �ʿ�
			System.out.println("DB ���� �Ϸ�");
			stmt = conn.createStatement();

		} catch (ClassNotFoundException e) {
			System.out.println("JDBC ����̹� �ε� ����");
		} catch (SQLException e) {
			System.out.println("DB ���� ����");
			JOptionPane.showMessageDialog(null, "DB ���� ����", "DB ���� ����", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	public ResultSet executeQuery(String sql) {
		//SQL�� �����ϱ� ���� �޼ҵ� - Parameter : String��ü�� ���� SQL��
		//�������� ResultSet���� ��ȯ
		System.out.println(sql);
		ResultSet src = null;
		try {
			src = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(src);
			System.out.println("SQL ���� ����");
			JOptionPane.showMessageDialog(null, "SQL ���� ����", "SQL ���� ����", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		return src;
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
