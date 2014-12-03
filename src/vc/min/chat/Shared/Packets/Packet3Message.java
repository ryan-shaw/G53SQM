package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet3Message extends Packet {

	public String message;
	
	public Packet3Message(){
		
	}
	
	public Packet3Message(String message){
		this.message = message;
	}
	
	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(message);
	}

	@Override
	public Packet read(DataInputStream dis) throws IOException {
		message = dis.readUTF();
		return this;
	}

}
