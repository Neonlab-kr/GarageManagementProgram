package Server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.swing.*;

import oracle.jdbc.rowset.OracleCachedRowSet;

public class Sell_Frame extends JFrame implements ActionListener,Runnable{
	private String ID;
	private JPanel bus_panel=new JPanel();
	private JLabel bus_lbl=new JLabel("버스 번호");
	private JComboBox bus_box=new JComboBox();
	private JButton search_btn=new JButton("조회");
	private JLabel kind_lbl=new JLabel("버스 종류 : ");
	private JLabel year_lbl=new JLabel("버스 연식 : ");
	private JLabel company_lbl=new JLabel("회사 이름 : ");
	private JPanel btn_panel=new JPanel();
	private JButton sell_btn=new JButton("매도");
	private JButton back_btn=new JButton("뒤로가기");
	private JPanel search_panel=new JPanel();
	
	private Socket socket;
	private ObjectInputStream reader=null;
	private ObjectOutputStream writer=null;
	
	public Sell_Frame(String str) throws UnknownHostException, IOException{
		this.ID=str;
		socket = new Socket("localhost",9500);
		//에러 발생
		reader= new ObjectInputStream(socket.getInputStream());
		writer = new ObjectOutputStream(socket.getOutputStream());
		
		setTitle("Bus Sell");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(3,1));
		setSize(400,300);
		
		bus_box.setPreferredSize(new Dimension(105,20));
		
		search_panel.setLayout(new GridLayout(3,1));
		search_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.TEST);
					writer.writeObject(dto);
					writer.flush();
				}catch(IOException io){
					io.printStackTrace();
				}
			}
		});
		sell_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.BUSSELL);
					String[] argument=new String[] {bus_box.getSelectedItem().toString()};
					dto.setArgument(argument);
					writer.writeObject(dto);
					writer.flush();
				}catch(IOException io){
					io.printStackTrace();
				}
			}
		});
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
		
		bus_panel.add(bus_lbl);
		bus_panel.add(bus_box);
		bus_panel.add(search_btn);
		add(bus_panel);
		search_panel.add(kind_lbl);
		search_panel.add(year_lbl);
		search_panel.add(company_lbl);
		add(search_panel);
		btn_panel.add(sell_btn);
		btn_panel.add(back_btn);
		add(btn_panel);
		setVisible(true);
	}

	public void service(){
		System.out.println("연결 완료!");
		try{
			InfoDTO dto = new InfoDTO();
			dto.setCommand(Info.SELLINFO);
			String[] argument=new String[] {ID};
			dto.setArgument(argument);
			writer.writeObject(dto);
			writer.flush();
		}catch(IOException io){
			io.printStackTrace();
		}
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		InfoDTO dto= null;
		while(true){
			try{
				dto = (InfoDTO) reader.readObject();
				if(dto.getCommand()==Info.SELLINFO){
					OracleCachedRowSet rs=dto.getRs();
					try {
						while(rs.next()) {
							bus_box.addItem(rs.getString("버스번호"));
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
				else if(dto.getCommand().equals(Info.TEST)) {
					OracleCachedRowSet rs=dto.getRs();
					try {
						while(rs.next()) {
							if(bus_box.getSelectedItem().equals(rs.getString("버스번호")))
							{
								kind_lbl.setText("버스 종류 : "+rs.getString("종류"));
								year_lbl.setText("버스 연식 : "+rs.getString("연식"));
								company_lbl.setText("회사 이름 : "+rs.getString("회사이름"));
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
				else if(dto.getCommand().equals(Info.BUSOUT)) {
					JOptionPane.showMessageDialog(null, "차량 매도 성공","Message",JOptionPane.PLAIN_MESSAGE);
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