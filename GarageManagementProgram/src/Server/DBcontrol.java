package Server;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.rowset.OracleCachedRowSet;

public class DBcontrol {
	public OracleCachedRowSet test() throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT ������ȣ FROM ����";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public OracleCachedRowSet login(String id, String pw) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT * FROM ���� WHERE ���̵� =\'" + id + "\' AND ��й�ȣ = \'" + pw + "\'";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public void confirm(String id, String conid) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "update ���� set ��ǥ�ھ��̵� = '" + id + "' where ���̵� = '" + conid + "'";
		db.stmt.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
	}

	public void join(String id, String job, String company, String pw, String name, String address, String age,
			String phone) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "INSERT INTO ���� (���̵�, ��å, ȸ���̸�, ��й�ȣ, �̸�, �ּ�, ����, ��ȭ��ȣ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = db.conn.prepareStatement(sql);
		pstmt.setString(1, id);
		pstmt.setString(2, job);
		pstmt.setString(3, company);
		pstmt.setString(4, pw);
		pstmt.setString(5, name);
		pstmt.setString(6, address);
		pstmt.setString(7, age);
		pstmt.setString(8, phone);
		pstmt.executeUpdate();
		pstmt.close();
		db.stmt.close();
		db.conn.close();
	}

	public OracleCachedRowSet businfo() throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "select ������ȣ from ����";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public OracleCachedRowSet bussearch(String SearchInfo, String SearchMethod) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "select * from ���� where " + SearchMethod + " like '%" + SearchInfo + "%'";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public OracleCachedRowSet profile(String id) throws SQLException {
		dbConnector db = new dbConnector();
		CallableStatement cstmt = db.conn.prepareCall("begin SEARCHBYID(?,?); end;");
		cstmt.setString(1, id);
		cstmt.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
		cstmt.execute();
		ResultSet rstmp = (ResultSet) cstmt.getObject(2);
		OracleCachedRowSet rs = new OracleCachedRowSet();
		rs.populate(rstmp);
		cstmt.close();
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public OracleCachedRowSet record() throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "select ������ȣ, �����ð�, �����ð�, ���������ð�, ��� from ��ȸ,��� where ���.��Ϲ�ȣ = ��ȸ.��Ϲ�ȣ and �����ð� is null";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public OracleCachedRowSet search(String busnum) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT ���������ð� FROM ���,��ȸ WHERE ������ȣ = '" + busnum + "' and ���.��Ϲ�ȣ=��ȸ.��Ϲ�ȣ";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public void busin(String busnum, String secnum, String gnum) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "UPDATE ���� SET ������ȣ = '" + busnum + "' WHERE �����ȣ = '" + secnum + gnum + "'";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
	}

	public void bussell(String busnum) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "DELETE FROM ���� WHERE ������ȣ ='" + busnum + "'";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
	}

	public OracleCachedRowSet user(String id) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT * FROM ���� WHERE ��ǥ�ھ��̵� is NULL AND ȸ���̸� = (SELECT ȸ���̸� FROM ���� WHERE ���̵� = '" + id + "')";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public OracleCachedRowSet idcheck(String ID) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT ���̵� FROM ���� WHERE ���̵� ='" + ID + "'";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	// preparedStatement
	public void busbuy(String busnum, String kindnum, String yearnum, String ID) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "INSERT INTO ���� (������ȣ,����,����,ȸ���̸�) VALUES(?,?,?,?)";
		OracleCachedRowSet rs=comcheck(ID);
		PreparedStatement pstmt = db.conn.prepareStatement(sql);
		pstmt.setString(1, busnum);
		pstmt.setString(2, kindnum);
		pstmt.setString(3, yearnum);
		if(rs.next()) {
			pstmt.setString(4,rs.getString("ȸ���̸�") );			
		}
		pstmt.executeUpdate();
		db.stmt.close();
		db.conn.close();
		pstmt.close();
	}
	//������ ȸ�� �̸� SELECT
	public OracleCachedRowSet comcheck(String ID) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT ȸ���̸� FROM ���� WHERE ���̵� ='" + ID + "'";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}
	
	// callableStatement
	public void busout(String busnum, String out_time, String pre_in_time) {
		try {
			dbConnector db = new dbConnector();
			CallableStatement cstmt = db.conn.prepareCall("{call GarageExiting_Procedure(?,?,?)}");
			cstmt.setString(1, busnum);
			cstmt.setString(2, out_time);
			cstmt.setString(3, pre_in_time);
			cstmt.executeUpdate();
			db.stmt.close();
			db.conn.close();
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
