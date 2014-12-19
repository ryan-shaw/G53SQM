package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Message packet 
 * 
 * @author Ryan Shaw
 *
 */
public class Packet3Message extends Packet {

	public String message;
	public String from;
	
	public Packet3Message(){
		
	}
	
	public Packet3Message(String message, String from){
		this.message = message;
		this.from = from;
	}
	
	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(message);
		dos.writeUTF(from);
	}

	@Override
	public Packet read(DataInputStream dis) throws IOException {
		message = dis.readUTF();
		from = dis.readUTF();
		return this;
	}

}
