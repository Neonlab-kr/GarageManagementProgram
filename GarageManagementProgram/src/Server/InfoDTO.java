package Server;
import java.io.*;
import java.sql.ResultSet;

import javax.sql.RowSet;

import oracle.jdbc.rowset.OracleCachedRowSet;

enum Info {
	EXIT,TEST,LOGIN,CONFIRM,JOIN,COMMIT,BUSINFO,PROFILE,USER,RECORD,SEARCH,BUSIN,BUSOUT
}

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