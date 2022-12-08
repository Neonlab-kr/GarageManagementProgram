package Server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import javax.swing.*;

import oracle.jdbc.rowset.OracleCachedRowSet;

public class Sell_Frame extends JFrame implements ActionListener,Runnable{
	private String ID;
	private JPanel bus_panel=new JPanel();
	private JLabel bus_lbl=new JLabel("���� ��ȣ");
	private JComboBox bus_box=new JComboBox();
	private JButton search_btn=new JButton("��ȸ");
	private JLabel kind_lbl=new JLabel("���� ���� : ");
	private JLabel year_lbl=new JLabel("���� ���� : ");
	private JLabel company_lbl=new JLabel("ȸ�� �̸� : ");
	private JPanel btn_panel=new JPanel();
	private JButton sell_btn=new JButton("�ŵ�");
	private JButton back_btn=new JButton("�ڷΰ���");
	private JPanel search_panel=new JPanel();
	
	private Socket socket;
	private ObjectInputStream reader=null;
	private ObjectOutputStream writer=null;
	
	public Sell_Frame(String str, Socket socket,ObjectInputStream reader,ObjectOutputStream writer){
		this.ID=str;
		this.socket=socket;
		this.reader=reader;
		this.writer=writer;
		
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
					dto.setCommand(Info.BUSINFO);
					writer.writeObject(dto);
					writer.flush();
				}catch(IOException io){
					io.printStackTrace();
				}
			}
		});
		sell_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new Bus_Frame(ID,socket,reader,writer).service();
				setVisible(false);
			}
		});
		back_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new Bus_Frame(ID,socket,reader,writer).service();
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
		System.out.println("���� �Ϸ�!");
		
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		InfoDTO dto= null;
		while(true){
			try{
				dto = (InfoDTO) reader.readObject();
				if(dto.getCommand()==Info.BUSINFO){
					OracleCachedRowSet rs=dto.getRs();
					try {
						while(rs.next()) {
							bus_box.addItem(rs.getString("������ȣ"));
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
					JOptionPane.showMessageDialog(null, "���� �ŵ� ����","Message",JOptionPane.PLAIN_MESSAGE);
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