
package io.grpc.communication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


/**
 * A simple client that requests a greeting from the {@link ServerTerminal}.
 */
public class ClientTerminal {
  private static final Logger logger = Logger.getLogger(ClientTerminal.class.getName());

  private final ManagedChannel channel;
  private final CommunicatorGrpc.CommunicatorBlockingStub blockingStub;

  /** Construct client connecting to  server at {@code host:port}. */
  public ClientTerminal(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true)
        .build());
  }

  /** Construct client for accessing RouteGuide server using the existing channel. */
  ClientTerminal(ManagedChannel channel) {
    this.channel = channel;
    blockingStub = CommunicatorGrpc.newBlockingStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  /** Say hello to server. 
 * @param threadId */
  public void greet(String name, int threadId) {
    //logger.info("Will try to greet " + name + " ...");
    DigitalRequest request = DigitalRequest.newBuilder().setName(name).build();
    DigitalReply response;
//    try {
//      response = blockingStub.sendMessage(request);
//    } catch (StatusRuntimeException e) {
//      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
//      return;
//    }
//    logger.info("Greeting: " + response.getMessage());
    try {
     response = blockingStub.sendMessageAgain(request);
   } catch (StatusRuntimeException e) {
    logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
    return;
   }
   logger.info("Thread number -> "+threadId+" Greeting: -> "+ response.getMessage());

  }

  /**
   * Greet server. If provided, the first element of {@code args} is the name to use in the
   * greeting.
   */
  public static void main(String[] args) throws Exception {
	  ExecutorService service = Executors.newCachedThreadPool();
	  long start_time = System.currentTimeMillis();
	    for (int i = 0; i < 500; i++) {
	        service.submit(new CallingThread(i,args));
	    }
	    service.shutdown();
	    try {
	        service.awaitTermination(1, TimeUnit.HOURS);
	        long difference = (System.currentTimeMillis() - start_time);
	        logger.info("Total time taken in milliseconds : " + difference);
	    } catch (InterruptedException e) {

	    }
  }


}
