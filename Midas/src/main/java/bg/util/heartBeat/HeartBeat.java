package bg.util.heartBeat;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HeartBeat implements Runnable{
	private static final Logger loggerJVM= LogManager.getLogger("jvm");

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
			loggerJVM.info("Total Memory :"+Runtime.getRuntime().totalMemory()+" | max Memory : "+Runtime.getRuntime().maxMemory());
			try {
				Thread.sleep(TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
