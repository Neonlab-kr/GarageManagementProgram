package Server;
import java.io.*;
import java.sql.ResultSet;

enum Info {
	EXIT,TEST
}

public class InfoDTO implements Serializable{
	private Info command;
	private ResultSet rs;
	private String[] Argument;
	
	public Info getCommand() {
		return command;
	}
	public void setCommand(Info command) {
		this.command = command;
	}
	public ResultSet getRs() {
		return rs;
	}
	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
	public String[] getArgument() {
		return Argument;
	}
	public void setArgument(String[] argument) {
		Argument = argument;
	}
}