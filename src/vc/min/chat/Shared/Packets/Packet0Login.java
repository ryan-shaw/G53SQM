package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet0Login extends Packet {

	public String username;
	
	public Packet0Login(){
		
	}
	
	public Packet0Login(String username){
		this.username = username;
	}
	
	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(username);
	}

	@Override
	public Packet read(DataInputStream dis) throws IOException {
		username = dis.readUTF();
		return this;
	}
	
}
