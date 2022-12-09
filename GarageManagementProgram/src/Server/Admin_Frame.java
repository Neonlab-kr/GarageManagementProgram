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
	private JButton back_btn=new JButton("�ڷΰ���");
	private JComboBox ID_box=new JComboBox();
	private String ID;
	//reader, writer ����
	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	
	public Admin_Frame(String str) throws UnknownHostException, IOException{
		this.ID=str;
		socket = new Socket("localhost",9500);
		//���� �߻�
		reader= new ObjectInputStream(socket.getInputStream());
		writer = new ObjectOutputStream(socket.getOutputStream());
		
		setTitle("admin");
		setLayout(new FlowLayout());
		setSize(500,100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		ID_box.setPreferredSize(new Dimension(150,20));
		
		//���̵� ���� ��ư ActionListener
		commit_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try{		
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.CONFIRM);
					String[] argument= {ID,ID_box.getSelectedItem().toString()};
					dto.setArgument(argument);
					writer.writeObject(dto);
					writer.flush();
					JOptionPane.showMessageDialog(null,"���̵� ���� �Ϸ�","COMMIT",JOptionPane.PLAIN_MESSAGE);
					ID_box.removeItem(ID_box.getSelectedItem());
					ID_box.repaint();
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		});
		
		//�α׾ƿ� ��ư ActionListener
		back_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					new Bus_Frame(ID);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				setVisible(false);
			}
		});
		
		//Component ��ġ
		add(ID_lbl);
		add(ID_box);
		add(commit_btn);
		add(back_btn);
		setVisible(true);
	}
	
	public void service(){
			System.out.println("���� �Ϸ�!");
		try {
			InfoDTO dto = new InfoDTO();
			dto.setCommand(Info.USER);
			String[] argument= {ID};
			dto.setArgument(argument);
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
				if(dto.getCommand().equals(Info.USER)) {
					OracleCachedRowSet rs=dto.getRs();
					try {
						while(rs.next()) {
							ID_box.addItem(rs.getString("���̵�"));
							ID_box.repaint();
						}
					}catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if (rs != null)
							try {
								//rs.close();
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