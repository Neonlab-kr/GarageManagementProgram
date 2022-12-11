package Server;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import oracle.jdbc.rowset.OracleCachedRowSet;
import java.util.Date;

public class Out_Frame extends JFrame implements ActionListener, Runnable {
	private String ID;
	private String[] hourValue = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
			"16", "17", "18", "19", "20", "21", "22", "23" };
	private String[] minuteValue = { "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55" };
	private JPanel pan = new JPanel(new GridLayout(3, 1));
	private JPanel subpan1 = new JPanel();
	private JLabel bus_lbl = new JLabel("버스 번호");
	private JComboBox bus_box = new JComboBox();
	private JPanel subpan2 = new JPanel();
	private JLabel pre_in_lbl = new JLabel("예상 입차 시간");
	private JComboBox pre_in_hour_box = new JComboBox(hourValue);
	private JComboBox pre_in_minute_box = new JComboBox(minuteValue);
	private JPanel subpan3 = new JPanel();
	private JButton out_btn = new JButton("출차");
	private JButton back_btn = new JButton("뒤로가기");

	private Socket socket;
	private ObjectInputStream reader = null;
	private ObjectOutputStream writer = null;

	public Out_Frame(String str) throws UnknownHostException, IOException {
		this.ID = str;
		socket = new Socket("localhost", 9500);
		// 에러 발생
		reader = new ObjectInputStream(socket.getInputStream());
		writer = new ObjectOutputStream(socket.getOutputStream());

		setTitle("Bus Out");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		bus_box.setPreferredSize(new Dimension(150, 20));
		setSize(350, 300);

		// 출차 버튼 ActionListener
		out_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.BUSOUT);
					String pre_in_time = pre_in_hour_box.getSelectedItem().toString() + ":"
							+ pre_in_minute_box.getSelectedItem().toString();
					String c_time = new Date().getHours() + ":" + new Date().getMinutes();
					String[] argument = new String[] { bus_box.getSelectedItem().toString(), pre_in_time, c_time };
					dto.setArgument(argument);
					writer.writeObject(dto);
					writer.flush();

				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});

		// 뒤로가기 버튼 ActionListener
		back_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		subpan1.add(bus_lbl);
		subpan1.add(bus_box);
		subpan2.add(pre_in_lbl);
		subpan2.add(pre_in_hour_box);
		subpan2.add(new JLabel("시"));
		subpan2.add(pre_in_minute_box);
		subpan2.add(new JLabel("분"));
		subpan3.add(out_btn);
		subpan3.add(back_btn);
		pan.add(subpan1);
		pan.add(subpan2);
		pan.add(subpan3);
		add(pan);
		setVisible(true);
	}

	public void service() {
		System.out.println("연결 완료!");

		try {
			InfoDTO dto = new InfoDTO();
			dto.setCommand(Info.BUSINFO);
			writer.writeObject(dto);
			writer.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
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
				if (dto.getCommand().equals(Info.BUSINFO)) {
					OracleCachedRowSet rs = dto.getRs();
					try {
						while (rs.next()) {
							if(rs.getString("버스번호")!=null) {
								bus_box.addItem(rs.getString("버스번호"));
								bus_box.repaint();								
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
				} else if (dto.getCommand().equals(Info.BUSINFO)) {
					JOptionPane.showMessageDialog(null, "출차 성공", "Message", JOptionPane.PLAIN_MESSAGE);
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