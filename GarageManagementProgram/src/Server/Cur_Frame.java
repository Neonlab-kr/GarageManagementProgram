package Server;

import javax.sql.RowSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import oracle.jdbc.rowset.OracleCachedRowSet;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cur_Frame extends JFrame implements ActionListener,Runnable
{
	private String ID;
	private String colName[]={"버스 번호","종류","연식","회사 이름"};
	private DefaultTableModel model=new DefaultTableModel(colName,0);
	private JTable table=new JTable(model);
	private JPanel pan=new JPanel();
	private JComboBox combo=new JComboBox(colName);
	private JTextField search_tf=new JTextField(10);
	private JButton search_btn=new JButton("검색");
	private JButton back_btn=new JButton("뒤로가기");
	
	private Socket socket;
	private ObjectInputStream reader=null;
	private ObjectOutputStream writer=null;
	
	public Cur_Frame(String str, Socket socket,ObjectInputStream reader,ObjectOutputStream writer){
		this.ID=str;
		this.socket=socket;
		this.reader=reader;
		this.writer=writer;
		
		setTitle("Bus Current Situation");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(550,600);
		
		JScrollPane scroll=new JScrollPane(table);
		search_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DefaultTableModel m=(DefaultTableModel) table.getModel();
				m.setNumRows(0);
				try {
					InfoDTO dto = new InfoDTO();
					dto.setCommand(Info.BUSSEARCH);
					writer.writeObject(dto);
					writer.flush();
					String[] argument={search_tf.getText(),combo.getSelectedItem().toString()};
					dto.setArgument(argument);
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		});
		//뒤로가기 버튼 ActionListener
		back_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new Bus_Frame(ID,socket,reader,writer).service();
				setVisible(false);
			}
		});
		pan.add(combo);
		pan.add(search_tf);
		pan.add(search_btn);
		pan.add(scroll);
		pan.add(back_btn);
		
		
		add(pan);
		setVisible(true);
	}
	
	public void service(){
		System.out.println("연결 완료!");
		
		try{
			InfoDTO dto = new InfoDTO();
			dto.setCommand(Info.BUSSEARCH);
			writer.writeObject(dto);
			writer.flush();
			String[] argument=new String[]{"",combo.getSelectedItem().toString()};
			dto.setArgument(argument);
		}catch(IOException e){
			e.printStackTrace();
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
				if(dto.getCommand().equals(Info.BUSSEARCH)){
					OracleCachedRowSet rs=dto.getRs();
					try{
						while(rs.next()){
							String[] row=new String[]{rs.getString("버스번호"),rs.getString("종류"),
													  rs.getString("연식"),rs.getString("회사")};
							model.addRow(row);
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
			}catch(IOException e){
				e.printStackTrace();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}	
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}