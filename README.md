# Java chat server and client
---

## Prerequisites

## Chat Protocol

#### PING (keepalive)
Client must send a keepalive every 10 seconds to ensure the connection is still alive, if a keepalive fails to reach the server the server drops the connection.


```
Client: PING

Server: PONG
```