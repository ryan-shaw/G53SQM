package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Sent on disconnect (2 way)
 * 
 * @author Ryan Shaw
 *
 */
public class Packet1Disconnect extends Packet {

	public String message;
	
	public Packet1Disconnect(){
		
	}
	
	public Packet1Disconnect(String message){
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
