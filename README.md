# Java chat server and client
---
[![Build Status](https://travis-ci.org/ryanshawty/G53SQM.svg?branch=master)](https://travis-ci.org/ryanshawty/G53SQM)

## Prerequisites

## Tools used
### GitHub
- Subversion
### Travis
- Build and test project
### Eclipse
- Junit
	- For unit testing
- EclEmma
	- Test coverage

## Chat Protocol

#### LOGIN <username>
Packet ID: 0

Login packet
```
Client: username

Server: username
```

#### PING (keepalive)
Packet ID: 2

Client must send a keepalive packet every 10 seconds to ensure the connection is still alive, if a keepalive fails to reach the server the server drops the connection.


#### Greeting
Packet ID: 127

Sent to the client once connected.