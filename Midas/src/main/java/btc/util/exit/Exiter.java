package btc.util.exit;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Permet de tuer le precedent process occupant le même port et .... attend d'être tué par le suivant.
 * Evite d'avoir 2 process actifs en même temps.
 * 
 * @author bg
 *
 */
public class Exiter implements Runnable {

	private static final Exiter instance = new Exiter();
	private ServerSocket serverSocket;
	private int port = 8181;
	private boolean bStarting = true;

	private Exiter() {
		(new Thread(this)).start();
	}

	private List<IExiter> listExiterListener = new ArrayList<>();

	public void addExiterListener(IExiter exiterListener) {
		listExiterListener.add(exiterListener);
	}

	@Override
	public void run() {

		while (bStarting) {
			try {
				serverSocket = new ServerSocket(port);
				bStarting = false;
				awake();
			} catch (IOException e) {
				System.err.println("Exiter : " + e.getMessage() + "  port " + port);
				openSocketKillRequest();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		try {
			Socket s = serverSocket.accept();// Ca bloque
			System.err.println("Kimll Request from :" + s.getRemoteSocketAddress());

			for (IExiter listener : listExiterListener) {
				listener.exitRequest();
			}

			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void openSocketKillRequest() {
		try {
			System.err.println("Send request kill previous");
			Socket socket = new Socket("localhost", port);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Exiter getInstance() {
		return instance;
	}

	private synchronized void awake() {
		notifyAll();
	}

	public synchronized boolean isAlone() {
		if (bStarting) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}
