# Java chat server and client
---
[![Build Status](https://travis-ci.org/ryanshawty/G53SQM.svg?branch=master)](https://travis-ci.org/ryanshawty/G53SQM)
<a href="https://scan.coverity.com/projects/3643">
  <img alt="Coverity Scan Build Status"
       src="https://scan.coverity.com/projects/3643/badge.svg"/>
</a>
## Prerequisites
### OracleJDK8
Built and tested on oracle JDK 8, do not expect this to work on any other JDK
### Ant
Ant is used for building and testing


## Instructions for building and testing (in terminal):
 1. Clone the repo
    * ``git clone https://github.com/ryanshawty/G53SQM.git``
 2. Install ant
    * Windows: https://code.google.com/p/winant/
    * Linux: http://dita-ot.sourceforge.net/doc/ot-userguide13/xhtml/installing/linux_installingant.html
    * OSX: Execute ``ant`` in terminal to verify if install is required, if so, best bet is install via HomeBrew
 3. ``cd G53SQM``
 4. Test server
    * ``ant ServerTest`` - This will build and test the server

## Tools used
- GitHub
	- Source control
- TravisCI 
	- Build and test project
- Eclipse 
	- JUnit
		For unit testing
	- EclEmma
		- Test coverage
- Coverity
	- Useful for detecting and assigning defects in the build

## Chat Protocol
This is the chat protocol that is used in the project, it consists of the basic expectations of a chat server.

#### PING (keepalive)
Packet ID: 2

Client must send a packet every 1 second (does not necessarily have to be a ping packet type) to ensure the connection is still alive, if a keepalive fails to reach the server the server drops the connection.

#### Greeting
Packet ID: 127

Server -> Client

Sent to the client once connected.

#### LOGIN <username>
Packet ID: 0

Client <-> Server

If the username is accepted the server responds with the exact same packet received.

### Disconnect
Packet ID: 1

Client <-> Server

Disconnect the client from the server.

### Message
Packet ID: 3

Client -> Server -> All Clients

Send a broadcast message to all clients.

### List clients
Packet ID: 4

Client -> Server -> Client

Request a list of connected clients, either a count of the clients or names.

### Personal message
Packet ID: 5

Send a personal message to a client.

## Using the protocol
There is a shared packets package called Shared.Packets, in this package there is a class called PacketHandler.java, this class will handle everything to do with the packets and it should be used in both server and client, meaning both depend directly on this package and any changes will effect both ends. This method will reduce the amount of bugs because of not updating something on both the client and server packages. See Java Docs in docs/ to view additional information about packets including their property names.

There are 4 public methods + 1 constructor:

```java
public PacketHandler(DataInputStream, DataOutputStream)
public void writePacket(Packet) throws IOException
public Packet readPacket(int /* Packet ID */)

/* Helper methods */
public int getPacketID(Class<? extends Packet> /* The packet class */)
public Class<? extend Packet> getPacketClass(int /* Packet ID */)
```

You can read more into these with the documents attached in IPacketHandler (the interface class)