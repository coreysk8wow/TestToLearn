package test.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class ChatServer {
	private static final int SERVER_PORT = 8888;
	private ServerSocketChannel ssc;
	private Selector selector;

	/*
	 * Initialize ServerSocketChannel.
	 */
	private void init() throws IOException {
	    ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress(SERVER_PORT));
		ssc.configureBlocking(false);
		
		selector = Selector.open();
		ssc.register(selector, SelectionKey.OP_ACCEPT);
	}

	private void start() {
		
	}

}