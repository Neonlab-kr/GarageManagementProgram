package Server;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.rowset.OracleCachedRowSet;

public class DBcontrol {
	public OracleCachedRowSet test() throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT 버스번호 FROM 버스";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public OracleCachedRowSet login(String id, String pw) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT * FROM 직원 WHERE 아이디 =\'" + id + "\' AND 비밀번호 = \'" + pw + "\'";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public void confirm(String id, String conid) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "update 직원 set 대표자아이디 = '" + id + "' where 아이디 = '" + conid + "'";
		db.stmt.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
	}

	public void join(String id, String job, String company, String pw, String name, String address, String age,
			String phone) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "INSERT INTO 직원 (아이디, 직책, 회사이름, 비밀번호, 이름, 주소, 나이, 전화번호) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
		String sql = "select 버스번호 from 차고";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public OracleCachedRowSet bussearch(String SearchInfo, String SearchMethod) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "select * from 버스 where " + SearchMethod + " like '%" + SearchInfo + "%'";
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
		String sql = "select 버스번호, 출차시간, 입차시간, 예정입차시간, 비고 from 조회,기록 where 기록.기록번호 = 조회.기록번호 and 입차시간 is null";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public OracleCachedRowSet search(String busnum) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT 예정입차시간 FROM 기록,조회 WHERE 버스번호 = '" + busnum + "' and 기록.기록번호=조회.기록번호";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public void busin(String busnum, String secnum, String gnum) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "UPDATE 차고 SET 버스번호 = '" + busnum + "' WHERE 차고번호 = '" + secnum + gnum + "'";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
	}

	public void bussell(String busnum) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "DELETE FROM 버스 WHERE 버스번호 ='" + busnum + "'";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
	}

	public OracleCachedRowSet user(String id) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT * FROM 직원 WHERE 대표자아이디 is NULL AND 회사이름 = (SELECT 회사이름 FROM 직원 WHERE 아이디 = '" + id + "')";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	public OracleCachedRowSet idcheck(String ID) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT 아이디 FROM 직원 WHERE 아이디 ='" + ID + "'";
		OracleCachedRowSet rs = db.executeQuery(sql);
		db.stmt.close();
		db.conn.close();
		return rs;
	}

	// preparedStatement
	public void busbuy(String busnum, String kindnum, String yearnum, String ID) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "INSERT INTO 버스 (버스번호,종류,연식,회사이름) VALUES(?,?,?,?)";
		OracleCachedRowSet rs=comcheck(ID);
		PreparedStatement pstmt = db.conn.prepareStatement(sql);
		pstmt.setString(1, busnum);
		pstmt.setString(2, kindnum);
		pstmt.setString(3, yearnum);
		if(rs.next()) {
			pstmt.setString(4,rs.getString("회사이름") );			
		}
		pstmt.executeUpdate();
		db.stmt.close();
		db.conn.close();
		pstmt.close();
	}
	//직원의 회사 이름 SELECT
	public OracleCachedRowSet comcheck(String ID) throws SQLException {
		dbConnector db = new dbConnector();
		String sql = "SELECT 회사이름 FROM 직원 WHERE 아이디 ='" + ID + "'";
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
