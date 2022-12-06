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
