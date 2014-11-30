package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet {
	public abstract void write(DataOutputStream dos) throws IOException;
	public abstract Packet read(DataInputStream dis) throws IOException;
}
