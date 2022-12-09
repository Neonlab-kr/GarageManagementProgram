package Server;

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

public class Buy_Frame extends JFrame implements ActionListener,Runnable{
	private String ID;
	private JPanel pan=new JPanel();
	private JLabel bus_lbl=new JLabel("버스 번호");
	private JTextField bus_tf=new JTextField(20);
	private JLabel kind_lbl=new JLabel("버스 종류");
	private JTextField kind_tf=new JTextField(20);
	private JLabel year_lbl=new JLabel("버스 연식");
	private JTextField year_tf=new JTextField(20);
	private JLabel company_lbl=new JLabel("회사 이름");
	private JButton buy_btn=new JButton("매입");
	private JButton back_btn=new JButton("뒤로가기");
	
	private Socket socket;
	private ObjectInputStream reader=null;
	private ObjectOutputStream writer=null;
	
	public Buy_Frame(String str) throws UnknownHostException, IOException{
		this.ID=str;
		socket = new Socket("localhost",9500);
		//에러 발생
		reader= new ObjectInputStream(socket.getInputStream());
		writer = new ObjectOutputStream(socket.getOutputStream());
		
		setTitle("Bus Buy");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(330,200);
		
		buy_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.BUSBUY);
					String[]argument=new String[] {bus_tf.getText(),kind_tf.getText(),year_tf.getText(),ID};
					dto.setArgument(argument);		
					writer.writeObject(dto);
					writer.flush();
					JOptionPane.showMessageDialog(null, "차량 매입 성공","Message",JOptionPane.PLAIN_MESSAGE);
				}catch(IOException ioe){
					ioe.printStackTrace();
				}catch(NullPointerException npe) {
					
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
		pan.add(bus_lbl);
		pan.add(bus_tf);
		pan.add(kind_lbl);
		pan.add(kind_tf);
		pan.add(year_lbl);
		pan.add(year_tf);
		pan.add(buy_btn);
		pan.add(back_btn);
		add(pan);
		setVisible(true);
	}
	public void service(){
		System.out.println("연결 완료!");
		
		Thread t = new Thread(this);
		t.start();
	}
	@Override
	public void run() {
		InfoDTO dto= null;
		while(true){
			try{
				dto = (InfoDTO) reader.readObject();
				if(dto.getCommand()==Info.BUSBUY){
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