# Java chat server and client
---
[![Build Status](https://travis-ci.org/ryanshawty/G53SQM.svg?branch=master)](https://travis-ci.org/ryanshawty/G53SQM)

## Prerequisites

## Chat Protocol

#### PING (keepalive)
Client must send a keepalive every 10 seconds to ensure the connection is still alive, if a keepalive fails to reach the server the server drops the connection.


```
Client: PING

Server: PONG
```