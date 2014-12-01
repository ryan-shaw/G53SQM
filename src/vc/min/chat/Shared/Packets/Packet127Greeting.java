package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
