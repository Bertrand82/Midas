package btc.swing.heartBeat;

import java.util.HashSet;
import java.util.Set;

public class HeartBeat implements Runnable{
	
	private static final long TIME = 2 *60*1000;
	private static HeartBeat instance;
	private boolean isOn = true;
	
	public static  HeartBeat getInstance(){
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
	public void add(ICheckAlive check){
		list.add(check);
	}

	@Override
	public void run() {
		while(isOn){
			for(ICheckAlive c :list){
				c.checkIsAlive(TIME);
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
