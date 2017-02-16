package testNIO;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Simple echo-back server which listens for incoming stream connections and
 * echoes back whatever it reads. A single Selector object is used to listen to
 * the server socket (to accept new connections) and all the active socket
 * channels.
 * 
 */
public class SelectSockets {

	public static int PORT_NUMBER = 9090;

	// Use the same byte buffer for all channels. A single thread is
	// servicing all the channels, so no danger of concurrent acccess.
	private ByteBuffer buffer = ByteBuffer.allocate(1024);

	public static void main(String[] args) throws Exception {
		new SelectSockets( ).go(args);
	}

	public void go(String[] argv) throws Exception {
		int port = PORT_NUMBER;
		if (argv.length > 0) { // Override default listen port
			port = Integer.parseInt(argv[0]);
		}
		// Allocate an unbound server socket channel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		// Get the associated ServerSocket to bind it with
		ServerSocket serverSocket = serverSocketChannel.socket();
		// Create a new Selector for use below
		Selector selector = Selector.open();
		// Set the port the server channel will listen to
		serverSocket.bind(new InetSocketAddress((InetAddress) null, port));
//		serverSocket.bind(new InetSocketAddress(InetAddress.getByAddress(new byte[]{-64, -88, 66, 2}), port));
		// 192.168.66.2  --> WLAN
		// serverSocket.bind(new InetSocketAddress(port));
		// Set nonblocking mode for the listening socket
		serverSocketChannel.configureBlocking(false);
		// Register the ServerSocketChannel with the Selector
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		System.out.println("Listening on ip : port = " + serverSocket.getInetAddress() + " : " + serverSocket.getLocalPort());
		while (true) {
			// This may block for a long time. Upon returning, the
			// selected set contains keys of the ready channels.
			int n = selector.select(); System.out.println("after select()");
			if (n == 0) {
				continue; // nothing to do
			}
			// Get an iterator over the set of selected keys
			Iterator it = selector.selectedKeys().iterator();
			// Look at each key in the selected set
			while (it.hasNext()) {
				SelectionKey key = (SelectionKey) it.next();
				// Is a new connection coming in?
				if (key.isAcceptable()) {
					ServerSocketChannel server = (ServerSocketChannel) key
							.channel();
					SocketChannel channel = server.accept();
					registerChannel(selector, channel, SelectionKey.OP_READ);
				}

				// Is there data to read on this channel?
				if (key.isReadable()) {
					readDataFromSocket(key);
				}
				// Remove key from selected set; it's been handled
				it.remove();
			}
		}
	}

	/**
	 * Register the given channel with the given selector for the given
	 * operations of interest
	 */
	protected void registerChannel(Selector selector,
			SelectableChannel channel, int ops) throws Exception {
		if (channel == null) {
			return; // could happen
		}
		// Set the new channel nonblocking
		channel.configureBlocking(false);
		// Register it with the selector
		channel.register(selector, ops);
	}

	/**
	 * Sample data handler method for a channel with data ready to read.
	 * 
	 * @param key
	 *            A SelectionKey object associated with a channel determined by
	 *            the selector to be ready for reading. If the channel returns
	 *            an EOF condition, it is closed here, which automatically
	 *            invalidates the associated key. The selector will then
	 *            de-register the channel on the next select call.
	 */
	protected void readDataFromSocket(SelectionKey key) throws Exception {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		int count;
		buffer.clear(); // Empty buffer
		// Loop while data is available; channel is nonblocking
		while ((count = socketChannel.read(buffer)) > 0) {
			System.out.println("capacity : " + buffer.capacity());
			System.out.println("limit : " + buffer.limit());
			System.out.println("position : " + buffer.position());
			System.out.println("-------------------");
			
			buffer.flip(); // Make buffer readable
			
			System.out.println("capacity : " + buffer.capacity());
			System.out.println("limit : " + buffer.limit());
			System.out.println("position : " + buffer.position());
			System.out.println("-------------------");
			// Send the data; don't assume it goes all at once
			
//			System.out.print(buffer.array().toString());
			
			for (int i = 0; i < count; i++) {
				System.out.print((char) buffer.array()[i]);
			}
			System.out.println();
			
			while (buffer.hasRemaining()) {
				socketChannel.write(buffer);
			}
			// WARNING: the above loop is evil. Because
			// it's writing back to the same nonblocking
			// channel it read the data from, this code can
			// potentially spin in a busy loop. In real life
			// you'd do something more useful than this.
			System.out.println("capacity : " + buffer.capacity());
			System.out.println("limit : " + buffer.limit());
			System.out.println("position : " + buffer.position());
			System.out.println("-------------------");
			
			buffer.clear(); // Empty buffer
			
			System.out.println("capacity : " + buffer.capacity());
			System.out.println("limit : " + buffer.limit());
			System.out.println("position : " + buffer.position());
			System.out.println("-------------------");
		}
		if (count < 0) {
			// Close channel on EOF, invalidates the key
			socketChannel.close();
		}
	}

	/**
	 * Spew a greeting to the incoming client connection.
	 * 
	 * @param channel
	 *            The newly connected SocketChannel to say hello to.
	 */
	private void sayHello(SocketChannel channel) throws Exception {
		buffer.clear();
		buffer.put("Hi there!\r\n".getBytes());
		buffer.flip();
		channel.write(buffer);

		/*while(buffer.hasRemaining()) {
			System.out.println((char)(buffer.getChar()));
		}*/
	}

}
