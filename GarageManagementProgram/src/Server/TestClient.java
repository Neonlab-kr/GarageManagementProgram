package Server;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

class  TestClient extends JFrame implements ActionListener,Runnable
{
	private Socket socket;
	private ObjectInputStream reader=null;
	private ObjectOutputStream writer=null; 
	public TestClient() {
		//������ â ����
		setBounds(300,300,300,300);
		setVisible(true);

		//������ �̺�Ʈ
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){ 
				//System.exit(0);
				try{
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.EXIT);
					writer.writeObject(dto);
					writer.flush();
				}catch(IOException io){
					io.printStackTrace();
				}
			}
		});
	}

	public void service(){
		//���� IP �Է¹ޱ�
		//String serverIP = JOptionPane.showInputDialog(this, "����IP�� �Է��ϼ���","����IP",JOptionPane.INFORMATION_MESSAGE);
		String serverIP= JOptionPane.showInputDialog(this,"����IP�� �Է��ϼ���","192.168.25.22");  //�⺻������ ������ ���� �ԷµǾ� ���� ��
		if(serverIP==null || serverIP.length()==0){  //���� ���� �Էµ��� �ʾ��� �� â�� ����
			System.out.println("���� IP�� �Էµ��� �ʾҽ��ϴ�.");
			System.exit(0);
		}
		try{
			socket = new Socket(serverIP,9500);
			//���� �߻�
			reader= new ObjectInputStream(socket.getInputStream());
			writer = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("���� �Ϸ�!"); 
			
		} catch(UnknownHostException e ){
			System.out.println("������ ã�� �� �����ϴ�.");
			e.printStackTrace();
			System.exit(0);
		} catch(IOException e){
			System.out.println("������ ������ �ȵǾ����ϴ�.");
			e.printStackTrace();
			System.exit(0);
		}
		try{		
			InfoDTO dto = new InfoDTO();
			dto.setCommand(Info.TEST);//����� �׽�Ʈ ��� ����
			writer.writeObject(dto);  //���������� �ʿ䰡 ����
			writer.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		//������ ����
		
		Thread t = new Thread(this);
		t.start();
	}
	//������ �������̵� 
	@Override
	public void run(){
		//�����κ��� ������ �ޱ�
		InfoDTO dto= null;
		while(true){
			try{
				dto = (InfoDTO) reader.readObject();
				if(dto.getCommand()==Info.EXIT){  //�����κ��� �� �ڽ��� exit�� ������ �����
					reader.close();
					writer.close();
					socket.close();
					System.exit(0);
				}
			}catch(IOException e){
				e.printStackTrace();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}	
		}
	}

	public static void main(String[] args) 
	{
		new TestClient().service();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		   
	}
}