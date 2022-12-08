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
	private JLabel name_lbl=new JLabel("�̸� : ");
	private JLabel job_lbl=new JLabel("��å : ");
	private JLabel address_lbl=new JLabel("�ּ� : ");
	private JLabel age_lbl=new JLabel("���� : ");
	private JLabel tel_lbl=new JLabel("��ȭ��ȣ : ");
	private JLabel admit_lbl=new JLabel("������ : ");
	private JPanel btn_panel=new JPanel();
	private JPanel btn_panel2=new JPanel();
	private JButton confirm_btn=new JButton("���Խ���");
	private JButton back_btn=new JButton("�ڷΰ���");
	
	private boolean isManager=true;
	private Socket socket;
	private ObjectInputStream reader=null;
	private ObjectOutputStream writer=null;
	
	public profile_Frame(String str) throws UnknownHostException, IOException{
		this.ID=str;
		socket = new Socket("localhost",9500);
		//���� �߻�
		reader= new ObjectInputStream(socket.getInputStream());
		writer = new ObjectOutputStream(socket.getOutputStream());
		
		setTitle(ID+"'s Profile");
		setLayout(new GridLayout(8,1));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(500,380);
		confirm_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new Admin_Frame(ID).service();
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
		
		//�ڷΰ��� ��ư ActionListener
		back_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					new Bus_Frame(ID).service();
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
		btn_panel.add(back_btn);
		
		add(job_lbl);
		add(name_lbl);
		add(address_lbl);
		add(age_lbl);
		add(tel_lbl);
		add(admit_lbl);
		btn_panel2.add(confirm_btn);
		add(btn_panel2);
		add(btn_panel);
		
		setVisible(true);
	}
	
	public void service(){
			System.out.println("���� �Ϸ�!");
		try{
			InfoDTO dto = new InfoDTO();
			dto.setCommand(Info.PROFILE);
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
		InfoDTO dto=null;
		while(true){
			try{
				dto=(InfoDTO) reader.readObject();
				if(dto.getCommand().equals(Info.PROFILE)){
					OracleCachedRowSet rs=dto.getRs();
					try{
						job_lbl.setText(job_lbl.getText()+rs.getString("��å"));
						if(rs.getString("��å").equals("������"))isManager=true;
						name_lbl.setText(name_lbl.getText()+rs.getString("�̸�"));
						address_lbl.setText(address_lbl.getText()+rs.getString("�ּ�"));
						age_lbl.setText(age_lbl.getText()+rs.getString("����"));
						tel_lbl.setText(tel_lbl.getText()+rs.getString("��ȭ��ȣ"));
						admit_lbl.setText(admit_lbl.getText()+rs.getString("���ο���"));
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