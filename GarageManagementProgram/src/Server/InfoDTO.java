package Server;
import java.io.*;

enum Info {
	EXIT,TEST
}

public class InfoDTO implements Serializable{
	private Info command;
	
	public Info getCommand() {
		return command;
	}
	public void setCommand(Info command) {
		this.command = command;
	}
}