package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Server should send this to newly connected clients to notify them that the server is ready for a login packet
 * 
 * @author Ryan Shaw
 *
 */
public class Packet127Greeting extends Packet{

	public Packet127Greeting(){
		
	}
	
	@Override
	public void write(DataOutputStream dos) throws IOException {
		
	}

	@Override
	public Packet read(DataInputStream dis) throws IOException {
		return this;
	}

}
