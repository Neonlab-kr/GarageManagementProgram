package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main{
	public static void main(String[] args) throws UnknownHostException, IOException {
			new Main_Frame().service();
	}
}