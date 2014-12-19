package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet5PM extends Packet{
	
	public String toUsername;
	public String fromUsername;
	public String message;
	
	public Packet5PM(){
		
	}
	
	public Packet5PM(String to, String from, String message){
		this.toUsername = to;
		this.fromUsername = from;
		this.message = message;
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(toUsername);
		dos.writeUTF(fromUsername);
		dos.writeUTF(message);
	}

	@Override
	public Packet read(DataInputStream dis) throws IOException {
		toUsername = dis.readUTF();
		fromUsername = dis.readUTF();
		message = dis.readUTF();
		return this;
	}

}
