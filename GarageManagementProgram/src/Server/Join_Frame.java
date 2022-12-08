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

public class Join_Frame extends JFrame implements ActionListener,Runnable
{
	private JPanel ID_panel=new JPanel();
	private JPanel job_panel=new JPanel();
	private JPanel password_panel=new JPanel();
	private JPanel name_panel=new JPanel();
	private JPanel address_panel=new JPanel();
	private JPanel age_panel=new JPanel();
	private JPanel tel_panel=new JPanel();
	private JPanel btn_panel=new JPanel();
	private JLabel ID_lbl=new JLabel("ID");
	private JLabel job_lbl=new JLabel("��å");
	private JLabel password_lbl=new JLabel("��й�ȣ");
	private JLabel name_lbl=new JLabel("�̸�");
	private JLabel address_lbl=new JLabel("�ּ�");
	private JLabel age_lbl=new JLabel("����");
	private JLabel tel_lbl=new JLabel("����ó");
	private JTextField ID_tf=new JTextField(18);
	private JTextField job_tf=new JTextField(20);
	private JTextField password_tf=new JTextField(20);
	private JTextField name_tf=new JTextField(20);
	private JTextField address_tf=new JTextField(20);
	private JTextField age_tf=new JTextField(20);
	private JTextField tel_tf=new JTextField(20);
	private JButton confirm_btn=new JButton("Ȯ��");
	private JButton join_btn=new JButton("���� ��û");
	private JButton cancel_btn=new JButton("���");
	
	private Socket socket;
	private ObjectInputStream reader=null;
	private ObjectOutputStream writer=null;
	
	private boolean isConfirm=false;
	
	public Join_Frame(Socket socket,ObjectInputStream reader,ObjectOutputStream writer){
		this.socket=socket;
		this.reader=reader;
		this.writer=writer;
		
		setTitle("Member Join");
		setSize(450,300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(8,1));
		
		ID_panel.add(ID_lbl);
		ID_panel.add(ID_tf);
		ID_panel.add(confirm_btn);
		add(ID_panel);
		
		job_panel.add(job_lbl);
		job_panel.add(job_tf);
		add(job_panel);
		
		password_panel.add(password_lbl);
		password_panel.add(password_tf);
		add(password_panel);
		
		name_panel.add(name_lbl);
		name_panel.add(name_tf);
		add(name_panel);
		
		address_panel.add(address_lbl);
		address_panel.add(address_tf);
		add(address_panel);
		
		age_panel.add(age_lbl);
		age_panel.add(age_tf);
		add(age_panel);
		
		tel_panel.add(tel_lbl);
		tel_panel.add(tel_tf);
		add(tel_panel);
		
		//������ �̺�Ʈ
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){ 
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
				
		//Ȯ�� ��ư ActionListener
		confirm_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.LOGIN);
					writer.writeObject(dto);
					writer.flush();
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		});
		//���� ��û ��ư ActionListener
		join_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(isConfirm==false)
				{
					JOptionPane.showMessageDialog(null, "���̵� �ߺ� üũ Ȯ��!","Warning",JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					try {
						InfoDTO dto = new InfoDTO();
						dto.setCommand(Info.JOIN);//Info.JOIN ȸ�� ���� ��û ���
						writer.writeObject(dto);
						writer.flush();
						String[] argument=new String[]{ID_tf.getText(),job_tf.getText(),password_tf.getText(),name_tf.getText(),
													   address_tf.getText(),age_tf.getText(),tel_tf.getText()};
						dto.setArgument(argument);
						JOptionPane.showMessageDialog(null,"���� ���� ��û �Ϸ�","Message",JOptionPane.PLAIN_MESSAGE);
					}catch(IOException ioe){
						ioe.printStackTrace();
					}
					isConfirm=false;
				}
			}
		});
		//��� ��ư ActionListener
		cancel_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new Main_Frame(socket,reader,writer).service();
				setVisible(false);
			}
		});
		btn_panel.add(join_btn);
		btn_panel.add(cancel_btn);
		add(btn_panel);
		
		setVisible(true);		
	}
	
	public void service(){
		System.out.println("���� �Ϸ�!"); 
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		InfoDTO dto=null;
		while(true) {
			try {
				dto=(InfoDTO)reader.readObject();
				if(dto.getCommand().equals(Info.LOGIN)) {
					OracleCachedRowSet rs=dto.getRs();
					try {
						while(rs.next()) {
							if(ID_lbl.getText().equals(rs.getString("���̵�"))) {
								JOptionPane.showMessageDialog(null, "���̵� �ߺ�","Warning",JOptionPane.ERROR_MESSAGE);
								isConfirm=false;
								break;
							}
							else {
								isConfirm=true;
							}
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
			}catch(IOException ioe){
				ioe.printStackTrace();
			}catch(ClassNotFoundException cnfe){
				cnfe.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
