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

public class Join_Frame extends JFrame implements ActionListener, Runnable {
	private JPanel ID_panel = new JPanel();
	private JPanel job_panel = new JPanel();
	private JPanel company_panel = new JPanel();
	private JPanel password_panel = new JPanel();
	private JPanel name_panel = new JPanel();
	private JPanel address_panel = new JPanel();
	private JPanel age_panel = new JPanel();
	private JPanel tel_panel = new JPanel();
	private JPanel btn_panel = new JPanel();
	private JLabel ID_lbl = new JLabel("ID");
	private JLabel job_lbl = new JLabel("직책");
	private JLabel company_lbl = new JLabel("회사명");
	private JLabel password_lbl = new JLabel("비밀번호");
	private JLabel name_lbl = new JLabel("이름");
	private JLabel address_lbl = new JLabel("주소");
	private JLabel age_lbl = new JLabel("나이");
	private JLabel tel_lbl = new JLabel("연락처");
	private JTextField ID_tf = new JTextField(18);
	private JPasswordField password_tf = new JPasswordField(20);
	private JTextField name_tf = new JTextField(20);
	private JTextField address_tf = new JTextField(20);
	private JTextField age_tf = new JTextField(20);
	private JTextField tel_tf = new JTextField(20);
	private JRadioButton jb1 = new JRadioButton("사원", true);
	private JRadioButton jb2 = new JRadioButton("주임");
	private JRadioButton jb3 = new JRadioButton("대리");
	private JRadioButton jb4 = new JRadioButton("과장");
	private JRadioButton jb5 = new JRadioButton("부장");
	private JRadioButton jb6 = new JRadioButton("사장");
	private ButtonGroup jbg = new ButtonGroup();
	private JRadioButton cb1 = new JRadioButton("삼성", true);
	private JRadioButton cb2 = new JRadioButton("유한");
	private JRadioButton cb3 = new JRadioButton("태영");
	private ButtonGroup cbg = new ButtonGroup();
	private JButton confirm_btn = new JButton("확인");
	private JButton join_btn = new JButton("승인 요청");
	private JButton cancel_btn = new JButton("취소");
	Thread t;
	private Socket socket;
	private ObjectInputStream reader = null;
	private ObjectOutputStream writer = null;

	private boolean isChecked = false;

	public Join_Frame() throws UnknownHostException, IOException {
		socket = new Socket("localhost",9500);
		//에러 발생
		reader= new ObjectInputStream(socket.getInputStream());
		writer = new ObjectOutputStream(socket.getOutputStream());

		setTitle("Member Join");
		setSize(450, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(9, 1));

		ID_panel.add(ID_lbl);
		ID_panel.add(ID_tf);
		ID_panel.add(confirm_btn);
		add(ID_panel);

		job_panel.add(job_lbl);
		jbg.add(jb1);
		jbg.add(jb2);
		jbg.add(jb3);
		jbg.add(jb4);
		jbg.add(jb5);
		jbg.add(jb6);
		job_panel.add(jb1);
		job_panel.add(jb2);
		job_panel.add(jb3);
		job_panel.add(jb4);
		job_panel.add(jb5);
		job_panel.add(jb6);
		add(job_panel);

		company_panel.add(company_lbl);
		cbg.add(cb1);
		cbg.add(cb2);
		cbg.add(cb3);
		company_panel.add(cb1);
		company_panel.add(cb2);
		company_panel.add(cb3);
		add(company_panel);

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

		// 확인 버튼 ActionListener
		confirm_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.IDCHECK);
					String[] argument = { ID_lbl.getText() };
					dto.setArgument(argument);
					writer.writeObject(dto);
					writer.flush();

				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});
		// 승인 요청 버튼 ActionListener
		join_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (isChecked == false) {
					JOptionPane.showMessageDialog(null, "아이디 중복 체크 확인!", "Warning", JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						InfoDTO dto = new InfoDTO();
						dto.setCommand(Info.JOIN);
						String job = "";
						if (jb1.isSelected())
							job = jb1.getText();
						else if (jb2.isSelected())
							job = jb2.getText();
						String com = "";
						if (cb1.isSelected())
							com = cb1.getText();
						else if (cb2.isSelected())
							com = cb2.getText();
						else if (cb3.isSelected())
							com = cb3.getText();

						String[] argument = new String[] { ID_tf.getText(), job, com, password_tf.getText(),
								name_tf.getText(), address_tf.getText(), age_tf.getText(), tel_tf.getText() };
						dto.setArgument(argument);
						writer.writeObject(dto);
						writer.flush();
						JOptionPane.showMessageDialog(null, "가입 승인 요청 완료", "Message", JOptionPane.PLAIN_MESSAGE);
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
					isChecked = false;
				}
			}
		});
		// 취소 버튼 ActionListener
		cancel_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new Main_Frame().service();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				setVisible(false);
				t.interrupt();
			}
		});
		btn_panel.add(join_btn);
		btn_panel.add(cancel_btn);
		add(btn_panel);

		setVisible(true);
	}

	public void service() {
		System.out.println("연결 완료!");
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		InfoDTO dto = null;
		while (true) {
			try {
				dto = (InfoDTO) reader.readObject();
				if (dto.getCommand().equals(Info.IDCHECK)) {
					OracleCachedRowSet rs = dto.getRs();
					try {
						if (rs.next()) {
							isChecked = false;
						} else {
							isChecked = true;
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
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}