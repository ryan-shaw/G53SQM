package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Packet4ListClients extends Packet {

	public boolean fullList;
	public ArrayList<String> clients;
	public int count;
	public boolean clientSending;
	
	public Packet4ListClients(){
		
	}
	
	public Packet4ListClients(boolean fullList){
		this.fullList = fullList;
		this.clientSending = true;
	}
	
	public Packet4ListClients(boolean fullList, ArrayList<String> clients){
		this.fullList = fullList;
		this.clients = clients;
		this.clientSending = false;
	}
	
	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeBoolean(clientSending);
		dos.writeBoolean(fullList);
		
		if(clientSending) 
			return;
		if(fullList){
			dos.writeUTF(clients.toString().substring(1, clients.toString().length() - 1));
		}else{
			dos.writeInt(clients.size());
		}
	}

	@Override
	public Packet read(DataInputStream dis) throws IOException {
		this.clientSending = dis.readBoolean();
		this.fullList = dis.readBoolean();

		if(clientSending)
			return this;
		if(fullList){
			clients = new ArrayList<String>();
			String[] clients = dis.readUTF().split(",");
			for(String s : clients){
				this.clients.add(s);
			}
			this.count = this.clients.size();
		}else{
			this.count = dis.readInt();
		}
		return this;
	}

}
