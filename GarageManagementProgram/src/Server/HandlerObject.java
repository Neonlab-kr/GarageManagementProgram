package Server;
import java.net.Socket;
import java.sql.SQLException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class HandlerObject extends Thread {
	ObjectInputStream reader;
	ObjectOutputStream writer;
	private Socket socket;

	public HandlerObject(Socket socket) throws IOException {
		this.socket = socket;
		writer = new ObjectOutputStream(socket.getOutputStream());
		reader = new ObjectInputStream(socket.getInputStream());
	}

	public void run() {
		InfoDTO dto = null;
		DBcontrol DBcontrol = new DBcontrol();
		try {
			while (true) {
				dto = (InfoDTO) reader.readObject();
				if (dto.getCommand() == Info.EXIT) {
					InfoDTO sendDto = new InfoDTO();
					sendDto.setCommand(Info.EXIT);
					writer.writeObject(sendDto);

					writer.flush();
					reader.close();
					writer.close();
					socket.close();
					Server.list.remove(this);
					break;
				} else if(dto.getCommand() == Info.TEST) {
					InfoDTO sendDto = new InfoDTO();
					sendDto.setCommand(Info.TEST);
					sendDto.setRs(DBcontrol.test());
					writer.writeObject(sendDto);
				} else if(dto.getCommand() == Info.LOGIN) {
					InfoDTO sendDto = new InfoDTO();
					sendDto.setCommand(Info.LOGIN);
					sendDto.setRs(DBcontrol.login(dto.getArgument()[0],dto.getArgument()[1]));
					writer.writeObject(sendDto);
				} else if(dto.getCommand() == Info.CONFIRM) {
					DBcontrol.confirm(dto.getArgument()[0],dto.getArgument()[1]);
				} else if(dto.getCommand() == Info.JOIN) {
					DBcontrol.join(dto.getArgument()[0],dto.getArgument()[1],dto.getArgument()[2],dto.getArgument()[3],dto.getArgument()[4],dto.getArgument()[5],dto.getArgument()[6],dto.getArgument()[7]);
				} else if(dto.getCommand() == Info.BUSINFO) {
					InfoDTO sendDto = new InfoDTO();
					sendDto.setCommand(Info.BUSINFO);
					sendDto.setRs(DBcontrol.businfo());
					writer.writeObject(sendDto);
				} else if(dto.getCommand() == Info.BUSSEARCH) {
					InfoDTO sendDto = new InfoDTO();
					sendDto.setCommand(Info.BUSSEARCH);
					sendDto.setRs(DBcontrol.bussearch(dto.getArgument()[0],dto.getArgument()[1]));
					writer.writeObject(sendDto);
				} else if(dto.getCommand() == Info.PROFILE) {
					InfoDTO sendDto = new InfoDTO();
					sendDto.setCommand(Info.PROFILE);
					sendDto.setRs(DBcontrol.profile(dto.getArgument()[0]));
					writer.writeObject(sendDto);
				} else if(dto.getCommand() == Info.RECORD ) {
					InfoDTO sendDto = new InfoDTO();
					sendDto.setCommand(Info.RECORD );
					sendDto.setRs(DBcontrol.record());
					writer.writeObject(sendDto);
				} else if(dto.getCommand() == Info.SEARCH ) {
					InfoDTO sendDto = new InfoDTO();
					sendDto.setCommand(Info.SEARCH );
					sendDto.setRs(DBcontrol.search(dto.getArgument()[0]));
					writer.writeObject(sendDto);
				} else if(dto.getCommand() == Info.BUSIN) {
					DBcontrol.busin(dto.getArgument()[0],dto.getArgument()[1],dto.getArgument()[2]);
				} else if(dto.getCommand() == Info.BUSOUT ) {
					DBcontrol.busout(dto.getArgument()[0],dto.getArgument()[1],dto.getArgument()[2]);
				} else if(dto.getCommand() == Info.BUSBUY ) {
					DBcontrol.busbuy(dto.getArgument()[0],dto.getArgument()[1],dto.getArgument()[2],dto.getArgument()[3]);
				} else if(dto.getCommand() == Info.BUSSELL  ) {
					DBcontrol.bussell(dto.getArgument()[0]);
				} else if(dto.getCommand() == Info.USER  ) {
					InfoDTO sendDto = new InfoDTO();
					sendDto.setCommand(Info.USER  );
					sendDto.setRs(DBcontrol.user(dto.getArgument()[0]));
					writer.writeObject(sendDto);
				} else if(dto.getCommand() == Info.IDCHECK  ) {
					InfoDTO sendDto = new InfoDTO();
					sendDto.setCommand(Info.IDCHECK  );
					sendDto.setRs(DBcontrol.idcheck(dto.getArgument()[0]));
					writer.writeObject(sendDto);
				} else if(dto.getCommand()==Info.SELLINFO) {
					InfoDTO sendDto=new InfoDTO();
					sendDto.setCommand(Info.SELLINFO);
					sendDto.setRs(DBcontrol.sellinfo(dto.getArgument()[0]));
					writer.writeObject(sendDto);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
