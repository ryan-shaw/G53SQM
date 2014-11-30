package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet255Disconnect extends Packet {

	@Override
	public void write(DataOutputStream dos) throws IOException {
		
	}

	@Override
	public Packet read(DataInputStream dis) throws IOException {
		return null;
	}

}
