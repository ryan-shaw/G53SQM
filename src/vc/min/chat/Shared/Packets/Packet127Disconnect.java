package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet127Disconnect extends Packet {

	public String message;
	
	public Packet127Disconnect(){
		
	}
	
	public Packet127Disconnect(String message){
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
