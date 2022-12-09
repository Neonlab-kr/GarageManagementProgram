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
	
	public Admin_Frame(String str) throws UnknownHostException, IOException{
		this.ID=str;
		socket = new Socket("localhost",9500);
		//에러 발생
		reader= new ObjectInputStream(socket.getInputStream());
		writer = new ObjectOutputStream(socket.getOutputStream());
		
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
					String[] argument= {ID,ID_box.getSelectedItem().toString()};
					dto.setArgument(argument);
					writer.writeObject(dto);
					writer.flush();
					JOptionPane.showMessageDialog(null,"아이디 승인 완료","COMMIT",JOptionPane.PLAIN_MESSAGE);
					ID_box.removeItem(ID_box.getSelectedItem());
					ID_box.repaint();
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		});
		
		//로그아웃 버튼 ActionListener
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
							ID_box.addItem(rs.getString("아이디"));
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