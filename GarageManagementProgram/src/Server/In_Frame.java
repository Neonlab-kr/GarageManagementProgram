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
import java.awt.FlowLayout;

public class In_Frame extends JFrame implements ActionListener,Runnable{
	private String ID;
	private JPanel pan=new JPanel(new GridLayout(3,1));
	private JPanel subpan1=new JPanel();
	private JLabel bus_lbl=new JLabel("버스 번호");
	private JComboBox bus_box=new JComboBox();
	private JButton search_btn=new JButton("조회");
	private JLabel pre_in_lbl=new JLabel("\uC608\uC815 \uC785\uCC28 \uC2DC\uAC04 : ");
	private JRadioButton sb1=new JRadioButton("A",true);
	private JRadioButton sb2=new JRadioButton("B");
	private JRadioButton sb3=new JRadioButton("C");
	private ButtonGroup sbg=new ButtonGroup();
	private JPanel subpan2=new JPanel();
	private JButton in_btn=new JButton("입차");
	private JButton back_btn=new JButton("뒤로가기");
	private JPanel subpan3=new JPanel();
	private JComboBox sbox=new JComboBox();
	
	
	private Socket socket;
	private ObjectInputStream reader=null;
	private ObjectOutputStream writer=null;
	private final JPanel panel = new JPanel();
	
	public In_Frame(String str) throws UnknownHostException, IOException{
		this.ID=str;
		socket = new Socket("localhost",9500);
		//에러 발생
		reader= new ObjectInputStream(socket.getInputStream());
		writer = new ObjectOutputStream(socket.getOutputStream());
		
		setTitle("Bus In");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		bus_box.setPreferredSize(new Dimension(150,20));
		setSize(300,300);
		for (int i=0;i<8;i++) {
			sbox.addItem(i+1);
		}

		
		//조회 버튼 ActionListener
		search_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try{
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.SEARCH);
					String[] argument= {bus_box.getSelectedItem().toString()};
					dto.setArgument(argument);
					writer.writeObject(dto);
					writer.flush();	
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		});
		//입차 버튼 ActionListener
		in_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try{
					String sec="";
					if(sb1.isSelected())sec=sb1.getText();
					else if(sb2.isSelected())sec=sb2.getText();
					else if(sb3.isSelected())sec=sb3.getText();
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.BUSIN);
					String[] argument=new String[] {bus_box.getSelectedItem().toString(),sec,sbox.getSelectedItem().toString()};
					dto.setArgument(argument);
					writer.writeObject(dto);
					writer.flush();
					JOptionPane.showMessageDialog(null, "입차 성공","Message",JOptionPane.PLAIN_MESSAGE);
					new Bus_Frame(ID).service();
					setVisible(false);
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		});
		//뒤로가기 버튼 ActionListener
		back_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try {
					new Bus_Frame(ID).service();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setVisible(false);
			}
		});
		subpan1.add(bus_lbl);
		subpan1.add(bus_box);
		subpan1.add(search_btn);
		subpan2.add(in_btn);
		subpan2.add(back_btn);
		pan.add(subpan1);
		sbg.add(sb1);
		sbg.add(sb2);
		sbg.add(sb3);
		subpan3.add(sb1);
		subpan3.add(sb2);
		subpan3.add(sb3);
		subpan3.add(sbox);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setHgap(70);
		flowLayout.setVgap(10);
		
		subpan3.add(panel);
		panel.add(pre_in_lbl);
		pan.add(subpan3);
		pan.add(subpan2);
		getContentPane().add(pan);
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
							if(rs.getString("예정입차시간")!=null)
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
						rs.next();
						pre_in_lbl.setText(bus_box.getSelectedItem().toString()+" "
										  +pre_in_lbl.getText()+rs.getString("예정입차시간"));
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