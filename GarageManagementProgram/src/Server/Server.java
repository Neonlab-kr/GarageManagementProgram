package Server;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

class Server 
{
	private ServerSocket serverSocket;
	static List <HandlerObject> list;
	
	public Server() throws InterruptedException {
		try{
			serverSocket= new ServerSocket (9500);
			System.out.println("서버 준비 완료");
			list = new  ArrayList<HandlerObject>();
			while(true){
				Socket socket = serverSocket.accept();
				HandlerObject handler = new  HandlerObject(socket);
				handler.start();
				list.add(handler);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException 
	{
		new Server();
	}
}