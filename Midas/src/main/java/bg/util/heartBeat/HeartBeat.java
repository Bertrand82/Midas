package bg.util.heartBeat;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HeartBeat implements Runnable {
	private static final Logger loggerJVM = LogManager.getLogger("jvm");

	private static final long TIME = 2 * 60 * 1000;
	private static HeartBeat instance;
	private boolean isOn = true;

	public static HeartBeat getInstance() {
		synchronized (HeartBeat.class) {
			if (instance == null) {
				instance = new HeartBeat();
				Thread t = new Thread(instance);
				t.setPriority(Thread.MIN_PRIORITY);
				t.setName("HeartBeatBg");
				t.start();
			}
		}
		return instance;
	}

	Set<ICheckAlive> list = new HashSet<>();

	public void add(ICheckAlive check) {
		list.add(check);
	}

	@Override
	public void run() {
		loggerJVM.info("Start --------------------");
		int i = 0;
		while (isOn) {
			for (ICheckAlive c : list) {
				i++;
				try {
					c.checkIsAlive(TIME);
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					loggerJVM.warn(" Exception12 " + e.getMessage());
				}
			}
			if (i == 10) {
				i=0;
				long total = (long) (Runtime.getRuntime().totalMemory() / 1000000);
				long max = (long) (Runtime.getRuntime().maxMemory() / 1000000);
				loggerJVM.info("Total Memory :" + total + " M \t| max Memory : " + max + " M");
			}
			try {
				Thread.sleep(TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
