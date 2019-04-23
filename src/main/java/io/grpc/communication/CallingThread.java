package io.grpc.communication;

import java.util.HashMap;
import java.util.logging.Logger;

public class CallingThread implements Runnable {
	private int id;
	private HashMap<Long, Long> histogram;
	private String[] args;
	private static final Logger logger = Logger.getLogger(CallingThread.class.getName());

	@Override
	public void run() {
		long start_time = System.currentTimeMillis();
			try {
				grpcCall(id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		long difference = (System.currentTimeMillis() - start_time);

		Long count = histogram.get(difference);
		if (count != null) {
			count++;
			histogram.put(Long.valueOf(difference), count);
		} else {
			histogram.put(Long.valueOf(difference), Long.valueOf(1L));
		}

		//logger.info(histogram.toString());
	}

	public CallingThread(int id, String[] args) {
		this.id = id;
		this.args = args;
	}

	private void grpcCall(int threadId) throws InterruptedException {
		ClientTerminal client = new ClientTerminal("localhost", 50052);
		try {
			/* Access a service running on the local machine on port 50051 */
			String user = "DI World";
			if (args.length > 0) {
				user = args[0]; /* Use the arg as the name to greet if provided */
			}
			client.greet(user, threadId);
		} finally {
			client.shutdown();
		}
	}

}
