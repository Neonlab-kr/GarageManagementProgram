package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main{
	public static void main(String[] args) {
		String IPAdress="localhost";//TODO �Է� �޴� ���·� �ٲܰ�
		try {
			Socket socket=new Socket(IPAdress,9500);
			ObjectInputStream reader= new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
			new Main_Frame(socket,reader,writer).service();
		} catch(UnknownHostException e ){
			System.out.println("������ ã�� �� �����ϴ�.");
			e.printStackTrace();
			System.exit(0);
		} catch(IOException e){
			System.out.println("������ ������ �ȵǾ����ϴ�.");
			e.printStackTrace();
			System.exit(0);
		}
	}
}