package Server;

import java.awt.*;
import javax.sql.RowSet;
import javax.swing.*;
import oracle.jdbc.rowset.OracleCachedRowSet;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin_Frame extends JFrame implements ActionListener,Runnable{
	private JLabel ID_lbl=new JLabel("���� ��� ����");
	private JButton commit_btn=new JButton("���̵� ����");
	private JButton logout_btn=new JButton("�α׾ƿ�");
	private JComboBox ID_box=new JComboBox();
	
	//reader, writer ����
	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	
	public Admin_Frame(Socket socket,ObjectInputStream reader,ObjectOutputStream writer){
		this.socket=socket;
		this.reader=reader;
		this.writer=writer;
		
		setTitle("admin");
		setLayout(new FlowLayout());
		setSize(500,100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		ID_box.setPreferredSize(new Dimension(150,20));
		
		//���̵� ���� ��ư ActionListener
		commit_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try{		
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.COMMIT);
					writer.writeObject(dto);
					writer.flush();
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		});
		
		//�α׾ƿ� ��ư ActionListener
		logout_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new Main_Frame(socket,reader,writer).service();
				setVisible(false);
			}
		});
		
		//Component ��ġ
		add(ID_lbl);
		add(ID_box);
		add(commit_btn);
		add(logout_btn);
		setVisible(true);
	}
	
	public void service(){
			System.out.println("���� �Ϸ�!");
		try {
			InfoDTO dto = new InfoDTO();
			dto.setCommand(Info.USER);//Info.USER ���ο�û�ؾ��ϴ� ����� ��� ���
			writer.writeObject(dto);
			writer.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		try {
			InfoDTO dto=(InfoDTO)reader.readObject();
			while(true) {
				if(dto.getCommand().equals(Info.COMMIT)){
					JOptionPane.showMessageDialog(null,"���̵� ���� �Ϸ�","COMMIT",JOptionPane.PLAIN_MESSAGE);
					ID_box.removeItem(ID_box.getSelectedItem());
					break;
				}
				else if(dto.getCommand().equals(Info.USER)) {
					OracleCachedRowSet rs=dto.getRs();
					try {
						while(rs.next()) {
							ID_box.addItem(rs.getString("���̵�"));
						}
					}catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if (rs != null)
							try {
								rs.close();
							} catch (Exception e) {}
					}
				}
			}			
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
