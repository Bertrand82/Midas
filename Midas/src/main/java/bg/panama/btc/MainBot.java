package bg.panama.btc;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bg.panama.btc.swing.MidasGUI;
import bg.util.exit.Exiter;

public class MainBot {

	static final Logger logger = LogManager.getLogger(MainBot.class.getName());
	static final Logger loggerTrade = LogManager.getLogger("trade");
	public static void main(String[] a){
		System.err.println("Start Midas see log in logs directory ");
		Exiter.getInstance();
		File dirLog = new File("logs");
		dirLog.mkdirs();
		logger.info("start");
		loggerTrade.info("start");
		new MidasGUI();
	}
}
