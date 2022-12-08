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

public class Main_Frame extends JFrame implements ActionListener,Runnable{
	//Component 설정
	private JPanel title_panel=new JPanel();
	private JPanel login_panel=new JPanel();
	private JPanel p2=new JPanel();
	private JLabel Title=new JLabel("Garage Management Program");
	private JLabel ID_lbl=new JLabel("ID");
	private JLabel PW_lbl=new JLabel("Password");
	private JButton btn_login=new JButton("로그인");
	private JButton join_btn=new JButton("회원가입");
	private JButton btn_quit=new JButton("종료");
	private JTextField ID_tf=new JTextField(15);
	private JPasswordField PW_tf=new JPasswordField(15);
	
	private Thread t;
	//reader, writer 설정
	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	//Main_Frame() 생성자
	public Main_Frame(Socket socket,ObjectInputStream reader,ObjectOutputStream writer) {
		this.socket=socket;
		this.reader=reader;
		this.writer=writer;
		//윈도우 창 설정
		setTitle("Garage Management Program");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(3,1));
		setResizable(false);
		setSize(450,300);
		
		//윈도우 이벤트
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

		//로그인 버튼 ActionListener
		btn_login.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try{
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.LOGIN);
					writer.writeObject(dto);
					writer.flush();
					String[]argument= {ID_tf.getText(),PW_tf.getText()};
					dto.setArgument(argument);
					new Bus_Frame("Sample",socket,reader,writer).service();
					setVisible(false);
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		});
		//회원가입 버튼 ActionListener
		join_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
					new Join_Frame(socket,reader,writer).service();
					setVisible(false);
					t.interrupt();
			}
		});
		//종료 버튼 ActionListener
		btn_quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try {
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.EXIT);//Info.EXIT 종료 명령
					writer.writeObject(dto);
					writer.flush();
					t.interrupt();
					t.sleep(100);
					reader.close();
					writer.close();
					socket.close();
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
				}
			}
		});

		//Component 배치
		Title.setFont(new Font("Serif",Font.BOLD,30));
		title_panel.add(Title);
		add(title_panel);
		
		login_panel.add(ID_lbl);
		login_panel.add(ID_tf);
		login_panel.add(PW_lbl);
		login_panel.add(PW_tf);
		add(login_panel);
		
		p2.add(btn_login);
		p2.add(join_btn);
		p2.add(btn_quit);
		add(p2);
		
		setVisible(true);
	}
	
	public void service(){
		System.out.println("연결 완료!"); 	
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run(){
		InfoDTO dto= null;
		while(true){
			try{
				dto = (InfoDTO) reader.readObject();
				if(dto.getCommand().equals(Info.EXIT)){
					t.interrupt();
					reader.close();
					writer.close();
					socket.close();
					System.exit(0);
				}
				else if(dto.getCommand().equals(Info.LOGIN)){
					OracleCachedRowSet rs=dto.getRs();
					try{
						if(rs.next()){
						new Bus_Frame(rs.getString("아이디"),socket,reader,writer).service();
						this.setVisible(false);
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
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}