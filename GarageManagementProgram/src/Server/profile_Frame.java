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

public class profile_Frame extends JFrame implements ActionListener,Runnable
{
	private String ID;
	private JLabel ID_lbl=new JLabel("아이디 : ");
	private JLabel job_lbl=new JLabel("직책 : ");
	private JLabel password_lbl=new JLabel("비밀번호 : ");
	private JLabel name_lbl=new JLabel("이름 : ");
	private JLabel address_lbl=new JLabel("주소 : ");
	private JLabel age_lbl=new JLabel("나이 : ");
	private JLabel tel_lbl=new JLabel("전화번호 : ");
	private JLabel admit_lbl=new JLabel("승인여부 : ");
	private JPanel btn_panel=new JPanel();
	private JButton back_btn=new JButton("뒤로가기");
	
	private Socket socket;
	private ObjectInputStream reader=null;
	private ObjectOutputStream writer=null;
	
	public profile_Frame(String str, Socket socket,ObjectInputStream reader,ObjectOutputStream writer){
		this.ID=str;
		this.socket=socket;
		this.reader=reader;
		this.writer=writer;
		
		setTitle(ID+"'s Profile");
		setLayout(new GridLayout(10,1));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500,380);
		
		//뒤로가기 버튼 ActionListener
		back_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new Bus_Frame(ID,socket,reader,writer).service();
				setVisible(false);
			}
		});
		btn_panel.add(back_btn);
		
		add(ID_lbl);
		add(job_lbl);
		add(password_lbl);
		add(name_lbl);
		add(address_lbl);
		add(age_lbl);
		add(tel_lbl);
		add(admit_lbl);
		add(btn_panel);
		
		setVisible(true);
	}
	
	public void service(){
			System.out.println("연결 완료!");
		try{
			InfoDTO dto = new InfoDTO();
			dto.setCommand(Info.PROFILE);
			writer.writeObject(dto);
			writer.flush();
			String[] argument= {ID};
			dto.setArgument(argument);
		}catch(IOException e){
			e.printStackTrace();
		}
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		InfoDTO dto=null;
		while(true){
			try{
				dto=(InfoDTO) reader.readObject();
				if(dto.getCommand().equals(Info.PROFILE)){
					OracleCachedRowSet rs=dto.getRs();
					try{
						ID_lbl.setText(ID_lbl.getText()+rs.getString("아이디"));
						job_lbl.setText(job_lbl.getText()+rs.getString("직책"));
						password_lbl.setText(password_lbl.getText()+rs.getString("비밀번호"));
						name_lbl.setText(name_lbl.getText()+rs.getString("이름"));
						address_lbl.setText(address_lbl.getText()+rs.getString("주소"));
						age_lbl.setText(age_lbl.getText()+rs.getString("나이"));
						tel_lbl.setText(tel_lbl.getText()+rs.getString("전화번호"));
						admit_lbl.setText(admit_lbl.getText()+rs.getString("승인여부"));
					}catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if (rs != null)
							try {
								rs.close();
							} catch (Exception e) {}
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
