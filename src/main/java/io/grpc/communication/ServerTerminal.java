
package io.grpc.communication;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Server that manages startup/shutdown of a {@code Communicator} server.
 */
public class ServerTerminal {
	private static final Logger logger = Logger.getLogger(ServerTerminal.class.getName());

	private Server server;

	private void start() throws IOException {
		/* The port on which the server should run */
		int port = 50052;
		server = ServerBuilder.forPort(port).addService(new CommunicatorImpl()).build().start();
		logger.info("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Use stderr here since the logger may have been reset by its JVM shutdown
				// hook.
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				ServerTerminal.this.stop();
				System.err.println("*** server shut down");
			}
		});
	}

	private void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon
	 * threads.
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	/**
	 * Main launches the server from the command line.
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		final ServerTerminal server = new ServerTerminal();
		server.start();
		server.blockUntilShutdown();
	}

	static class CommunicatorImpl extends CommunicatorGrpc.CommunicatorImplBase {

		@Override
		public void sendMessage(DigitalRequest req, StreamObserver<DigitalReply> responseObserver) {
			DigitalReply reply = DigitalReply.newBuilder().setMessage("Hello " + req.getName()).build();
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
		}

		@Override
		public void sendMessageAgain(DigitalRequest req, StreamObserver<DigitalReply> responseObserver) {
			DigitalReply reply = DigitalReply.newBuilder()
					.setMessage("This is Hello from Sahil and Supreet " + req.getName()).build();
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
		}
	}
}
