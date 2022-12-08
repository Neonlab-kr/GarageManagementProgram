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

public class Bus_Frame extends JFrame implements ActionListener, Runnable {
	private String ID;
	private JPanel sit_panel = new JPanel();
	private JPanel parking_panel_1 = new JPanel();
	private JPanel parking_panel_2 = new JPanel();
	private char sec_num = 'A';
	private JButton bus_btn_1 = new JButton();
	private JButton bus_btn_2 = new JButton();
	private JButton bus_btn_3 = new JButton();
	private JButton bus_btn_4 = new JButton();
	private JButton bus_btn_5 = new JButton();
	private JButton bus_btn_6 = new JButton();
	private JButton bus_btn_7 = new JButton();
	private JButton bus_btn_8 = new JButton();
	private JLabel cur_sec = new JLabel("현재 섹터 : " + sec_num + " 섹터");
	private JPanel btn_panel = new JPanel(new GridLayout(9, 1));
	private JLabel ID_lbl = new JLabel("사용자 아이디 : " + ID);
	private JButton profile_btn = new JButton("사용자 프로필 확인");
	private JButton cur_btn = new JButton("버스 현황 조회");
	private JPanel access_panel = new JPanel(new GridLayout(1, 2));
	private JButton out_btn = new JButton("출차");
	private JButton in_btn = new JButton("입차");
	private JPanel deal_panel = new JPanel(new GridLayout(1, 2));
	private JButton buy_btn = new JButton("차량 매입");
	private JButton sell_btn = new JButton("차량 매도");
	private JButton sector_A_btn = new JButton("A 섹터");
	private JButton sector_B_btn = new JButton("B 섹터");
	private JButton sector_C_btn = new JButton("C 섹터");
	private JButton logout_btn = new JButton("로그아웃");
	private String[] garage = new String[24];
	private int count = 0;
	private Socket socket;
	private ObjectInputStream reader = null;
	private ObjectOutputStream writer = null;

	public Bus_Frame(String str, Socket socket, ObjectInputStream reader, ObjectOutputStream writer) {
		this.ID = str;
		this.socket = socket;
		this.reader = reader;
		this.writer = writer;

		setTitle("Garage Management Program");
		setSize(650, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// 윈도우 이벤트
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.EXIT);
					writer.writeObject(dto);
					writer.flush();
				} catch (IOException io) {
					io.printStackTrace();
				}
			}
		});

		bus_btn_1.setText(setString(1, garage[0]));
		bus_btn_2.setText(setString(2, garage[1]));
		bus_btn_3.setText(setString(3, garage[2]));
		bus_btn_4.setText(setString(4, garage[3]));
		bus_btn_5.setText(setString(5, garage[4]));
		bus_btn_6.setText(setString(6, garage[5]));
		bus_btn_7.setText(setString(7, garage[6]));
		bus_btn_8.setText(setString(8, garage[7]));

		// 프로필 버튼 ActionListener
		profile_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new profile_Frame(ID, socket, reader, writer).service();
				setVisible(false);
			}
		});
		// 현황 조회 버튼 ActionListener
		cur_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Cur_Frame(ID, socket, reader, writer).service();
				setVisible(false);
			}
		});
		// 차량 입차 버튼 ActionListener
		in_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new In_Frame(ID, socket, reader, writer).service();
				setVisible(false);
			}
		});
		// 차량 출차 버튼 ActionListener
		out_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Out_Frame(ID, socket, reader, writer).service();
				setVisible(false);
			}
		});
		// 차량 매입 버튼 ActionListener
		buy_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Buy_Frame(ID, socket, reader, writer).service();
				setVisible(false);
			}
		});
		// 차량 매도 버튼 ActionListener
		sell_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Sell_Frame(ID, socket, reader, writer).service();
				setVisible(false);
			}
		});
		sector_A_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sec_num = 'A';
				cur_sec.setText("현재 섹터 : " + sec_num + " 섹터");
				bus_btn_1.setText(setString(1, garage[0]));
				bus_btn_2.setText(setString(2, garage[1]));
				bus_btn_3.setText(setString(3, garage[2]));
				bus_btn_4.setText(setString(4, garage[3]));
				bus_btn_5.setText(setString(5, garage[4]));
				bus_btn_6.setText(setString(6, garage[5]));
				bus_btn_7.setText(setString(7, garage[6]));
				bus_btn_8.setText(setString(8, garage[7]));
			}
		});
		sector_B_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sec_num = 'B';
				cur_sec.setText("현재 섹터 : " + sec_num + " 섹터");
				bus_btn_1.setText(setString(1, garage[8]));
				bus_btn_2.setText(setString(2, garage[9]));
				bus_btn_3.setText(setString(3, garage[10]));
				bus_btn_4.setText(setString(4, garage[11]));
				bus_btn_5.setText(setString(5, garage[12]));
				bus_btn_6.setText(setString(6, garage[13]));
				bus_btn_7.setText(setString(7, garage[14]));
				bus_btn_8.setText(setString(8, garage[15]));
			}
		});
		sector_C_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sec_num = 'C';
				cur_sec.setText("현재 섹터 : " + sec_num + " 섹터");
				bus_btn_1.setText(setString(1, garage[16]));
				bus_btn_2.setText(setString(2, garage[17]));
				bus_btn_3.setText(setString(3, garage[18]));
				bus_btn_4.setText(setString(4, garage[19]));
				bus_btn_5.setText(setString(5, garage[20]));
				bus_btn_6.setText(setString(6, garage[21]));
				bus_btn_7.setText(setString(7, garage[22]));
				bus_btn_8.setText(setString(8, garage[23]));
			}
		});
		logout_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Main_Frame(socket, reader, writer).service();
				setVisible(false);
			}
		});
		ID_lbl.setText("사용자 아이디 : " + ID);
		sit_panel.setLayout(new BorderLayout());

		parking_panel_1.setLayout(new GridLayout());
		parking_panel_1.setPreferredSize(new Dimension(450, 200));
		parking_panel_1.add(bus_btn_1);
		parking_panel_1.add(bus_btn_2);
		parking_panel_1.add(bus_btn_3);
		parking_panel_1.add(bus_btn_4);

		parking_panel_2.setLayout(new GridLayout());
		parking_panel_2.setPreferredSize(new Dimension(450, 200));

		parking_panel_2.add(bus_btn_5);
		parking_panel_2.add(bus_btn_6);
		parking_panel_2.add(bus_btn_7);
		parking_panel_2.add(bus_btn_8);

		sit_panel.add(parking_panel_1, BorderLayout.NORTH);
		sit_panel.add(cur_sec, BorderLayout.CENTER);
		sit_panel.add(parking_panel_2, BorderLayout.SOUTH);
		add(sit_panel, BorderLayout.WEST);

		deal_panel.add(buy_btn);
		deal_panel.add(sell_btn);
		btn_panel.add(ID_lbl);
		btn_panel.add(profile_btn);
		btn_panel.add(cur_btn);
		access_panel.add(in_btn);
		access_panel.add(out_btn);
		btn_panel.add(access_panel);
		btn_panel.add(deal_panel);
		btn_panel.add(sector_A_btn);
		btn_panel.add(sector_B_btn);
		btn_panel.add(sector_C_btn);
		btn_panel.add(logout_btn);
		add(btn_panel);

		setVisible(true);
	}

	public String setString(int num, String str) {
		String s = str;
		if (str == null) {
			s = "공석";
		}
		return "<html><body><center>" + sec_num + " 섹터" + "<br>차고 번호 : " + num + "<br>" + s + "</center></body></html>";
	}

	public void service() {
		System.out.println("연결 완료!");
		try {
			InfoDTO dto = new InfoDTO();
			dto.setCommand(Info.BUSINFO);
			writer.writeObject(dto);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		InfoDTO dto = null;
		while (true) {
			try {
				dto = (InfoDTO) reader.readObject();
				if (dto.getCommand() == Info.BUSINFO) {
					OracleCachedRowSet rs = dto.getRs();
					try {
						while (rs.next()) {
							garage[count] = rs.getString("버스번호");
							count++;
							if (count >= 24) {
								break;
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if (rs != null)
							try {
								rs.close();
							} catch (Exception e) {
							}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}