
POC : grpc-java 
==============================================

In gRPC a client application can directly call methods on a server application on a different machine as if it was a local object, making it easier for you to create distributed applications and services.

To build the poc project, run below command in grpc-java directory:

```
$ ./gradlew installDist
```

This creates the scripts `server-terminal` and `client-terminal` in
`build/install/grpc-java-poc/bin/` directory that run the example. This project requires the server to be running before starting the client.

For example, to try the grpc-java-poc communication example first run:

```
$ ./build/install/grpc-java-poc/bin/server-terminal
```

And in a different terminal window run:

```
$ ./build/install/grpc-java-poc/bin/client-terminal
```


That's it!


