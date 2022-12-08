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
	private JLabel ID_lbl=new JLabel("승인 대기 직원");
	private JButton commit_btn=new JButton("아이디 승인");
	private JButton back_btn=new JButton("뒤로가기");
	private JComboBox ID_box=new JComboBox();
	private String ID;
	//reader, writer 설정
	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	
	public Admin_Frame(String str,Socket socket,ObjectInputStream reader,ObjectOutputStream writer){
		this.ID=str;
		this.socket=socket;
		this.reader=reader;
		this.writer=writer;
		
		setTitle("admin");
		setLayout(new FlowLayout());
		setSize(500,100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		ID_box.setPreferredSize(new Dimension(150,20));
		
		//아이디 승인 버튼 ActionListener
		commit_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try{		
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.CONFIRM);
					writer.writeObject(dto);
					writer.flush();
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		});
		
		//로그아웃 버튼 ActionListener
		back_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new Bus_Frame(ID,socket,reader,writer);
				setVisible(false);
			}
		});
		
		//Component 배치
		add(ID_lbl);
		add(ID_box);
		add(commit_btn);
		add(back_btn);
		setVisible(true);
	}
	
	public void service(){
			System.out.println("연결 완료!");
		try {
			InfoDTO dto = new InfoDTO();
			dto.setCommand(Info.USER);
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
				if(dto.getCommand().equals(Info.CONFIRM)){
					JOptionPane.showMessageDialog(null,"아이디 승인 완료","COMMIT",JOptionPane.PLAIN_MESSAGE);
					ID_box.removeItem(ID_box.getSelectedItem());
					break;
				}
				else if(dto.getCommand().equals(Info.USER)) {
					OracleCachedRowSet rs=dto.getRs();
					try {
						while(rs.next()) {
							ID_box.addItem(rs.getString("아이디"));
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