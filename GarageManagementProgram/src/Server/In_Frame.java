package Server;

import javax.sql.RowSet;
import javax.swing.*;
import oracle.jdbc.rowset.OracleCachedRowSet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class In_Frame extends JFrame implements ActionListener,Runnable{
	private String ID;
	private JPanel pan=new JPanel(new GridLayout(3,1));
	private JPanel subpan1=new JPanel();
	private JLabel bus_lbl=new JLabel("버스 번호");
	private JComboBox bus_box=new JComboBox();
	private JButton search_btn=new JButton("조회");
	private JLabel pre_in_lbl=new JLabel("예상 입차 시간 : ");
	private JPanel subpan2=new JPanel();
	private JButton in_btn=new JButton("입차");
	private JButton back_btn=new JButton("뒤로가기");
	
	private Socket socket;
	private ObjectInputStream reader=null;
	private ObjectOutputStream writer=null;
	
	public In_Frame(String str, Socket socket,ObjectInputStream reader,ObjectOutputStream writer){
		this.ID=str;
		this.socket=socket;
		this.reader=reader;
		this.writer=writer;
		
		setTitle("Bus In");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		bus_box.setPreferredSize(new Dimension(150,20));
		setSize(350,300);
		
		//조회 버튼 ActionListener
		search_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try{
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.SEARCH);
					writer.writeObject(dto);
					writer.flush();
					String[] argument= {bus_box.getSelectedItem().toString()};
					dto.setArgument(argument);
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		});
		//입차 버튼 ActionListener
		in_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try{
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.BUSIN);
					writer.writeObject(dto);
					writer.flush();
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		});
		//뒤로가기 버튼 ActionListener
		back_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				new Bus_Frame(ID,socket,reader,writer).service();
				setVisible(false);
			}
		});
		subpan1.add(bus_lbl);
		subpan1.add(bus_box);
		subpan1.add(search_btn);
		subpan2.add(in_btn);
		subpan2.add(back_btn);
		pan.add(subpan1);
		pan.add(pre_in_lbl);
		pan.add(subpan2);
		add(pan);
		setVisible(true);
	}
	public void service(){
		System.out.println("연결 완료!");
		
		try{
			InfoDTO dto = new InfoDTO();
			dto.setCommand(Info.RECORD);
			writer.writeObject(dto);
			writer.flush();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		Thread t = new Thread(this);
		t.start();
	}
	@Override
	public void run() {
		InfoDTO dto=null;
		while(true) {
			try {
				dto=(InfoDTO)reader.readObject();
				if(dto.getCommand().equals(Info.RECORD)) {
					OracleCachedRowSet rs=dto.getRs();
					try {
						while(rs.next()) {
							if(!rs.getString("예상입차시간").equals(null))
							{
								bus_box.addItem(rs.getString("버스번호"));
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
				else if(dto.getCommand().equals(Info.SEARCH)) {
					OracleCachedRowSet rs=dto.getRs();
					try {
						pre_in_lbl.setText(bus_box.getSelectedItem().toString()+" "
										  +pre_in_lbl.getText()+rs.getString("예상입차시간"));
					}catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if (rs != null)
							try {
								rs.close();
							} catch (Exception e) {}
					}
				}
				else if(dto.getCommand().equals(Info.BUSIN)) {
					JOptionPane.showMessageDialog(null, "입차 성공","Message",JOptionPane.PLAIN_MESSAGE);
				}
			}catch(IOException e){
				e.printStackTrace();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}