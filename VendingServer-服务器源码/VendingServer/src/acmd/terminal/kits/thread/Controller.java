package acmd.terminal.kits.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller extends Thread {
	private static ServerSocket ss;
	private static final HashMap<String, Socket> socketMapping = new HashMap<>();
	private static final ExecutorService pool = Executors.newCachedThreadPool();
	static {
		try {
			ss = new ServerSocket(9000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private Socket socket = null;
	private boolean switchkey = true;
	private DataInputStream in;
	private DataOutputStream out;
	public synchronized Socket connect() throws IOException{
		socket = ss.accept();
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		return socket;
	}
	public void shutdown() throws IOException  {
		switchkey = false;
		if (switchkey) {
			socket.close();
		}
		this.interrupt();
	}

	@Override
	public void run() {
		while (switchkey) {
			try {
				String text = in.readUTF();
				System.out.println(text);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		switchkey = true;
	}
	
	public void execute(){
		pool.execute(this);
	}

}
