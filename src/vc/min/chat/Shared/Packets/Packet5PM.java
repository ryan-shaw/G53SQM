package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet5PM extends Packet{
	
	public String username;
	public String message;
	
	public Packet5PM(){
		
	}
	
	public Packet5PM(String username, String message){
		this.username = username;
		this.message = message;
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(username);
		dos.writeUTF(message);
	}

	@Override
	public Packet read(DataInputStream dis) throws IOException {
		username = dis.readUTF();
		message = dis.readUTF();
		return this;
	}

}
