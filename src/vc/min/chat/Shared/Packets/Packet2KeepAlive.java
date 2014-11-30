package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet2KeepAlive extends Packet {
	
	public Packet2KeepAlive(){
		
	}
	
	@Override
	public void write(DataOutputStream dos) throws IOException {

	}

	@Override
	public Packet read(DataInputStream dis) throws IOException {
		return this;
	}

}
