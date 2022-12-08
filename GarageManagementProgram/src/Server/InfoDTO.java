package Server;
import java.io.*;
import java.sql.ResultSet;

import javax.sql.RowSet;

import oracle.jdbc.rowset.OracleCachedRowSet;

enum Info {
	EXIT,TEST,LOGIN,CONFIRM,JOIN,BUSINFO,PROFILE,RECORD,SEARCH,BUSIN,BUSOUT,USER,IDCHECK
}
/*LOGIN : 로그인처리
 * CONFIRM : 가입 승인
 * JOIN : 회원가입
 * BUSINFO : 버스 현황 조회
 * PROFILE : 사용자 프로필
 * RECORD : 버스 출입차 기록
 * SEARCH : 예상 입차 시간 조회(버스 번호)
 * BUSIN : 입차
 * BUSOUT : 출차
 * IDCHECK : 아이디 중복 검사
 */

public class InfoDTO implements Serializable{
	private Info command;
	private OracleCachedRowSet rs;
	private String[] Argument;
	
	public Info getCommand() {
		return command;
	}
	public void setCommand(Info command) {
		this.command = command;
	}
	public OracleCachedRowSet getRs() {
		return rs;
	}
	public void setRs(OracleCachedRowSet rs) {
		this.rs = rs;
	}
	public String[] getArgument() {
		return Argument;
	}
	public void setArgument(String[] argument) {
		Argument = argument;
	}
}