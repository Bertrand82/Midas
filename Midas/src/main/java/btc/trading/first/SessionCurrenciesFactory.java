package btc.trading.first;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SessionCurrenciesFactory {

	public static File file = new File("p_session.bin");

	public static void saveOnFile(SessionCurrencies sessionCurrencies) {
		try {
			ObjectOutputStream output = null;
			try {
				OutputStream os = new FileOutputStream(file);
				output = new ObjectOutputStream(os);
				output.writeObject(sessionCurrencies);
			} finally {
				output.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (file.exists()){
				file.delete();
			}
		}
	}

	public static void synchronizeWithArchive(SessionCurrencies session) {
		try {
			if (file.exists()) {
				ObjectInputStream in = null;
				try {
					InputStream is = new FileInputStream(file);
					in = new ObjectInputStream(is);
					Object o = in.readObject();
					SessionCurrencies sessionArchive = (SessionCurrencies) o;
					session.updateWithArchive(sessionArchive);
				} finally {
					in.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
