package bg.panama.btc.simu;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import bg.panama.btc.trading.first.SessionCurrencies;
import bg.panama.btc.trading.first.SessionCurrenciesFactory;
import bg.panama.btc.trading.first.SessionCurrency;
import bg.panama.btc.trading.first.SessionCurrency.EtatSTOCHASTIQUE;

public class SimuProcess implements Runnable{
	
	
	private Thread t = new Thread(this);
	private Date dateStart ;
	private Date dateEnd  = new Date();
	long duree_mn =100;
	DialogSimuGUI dialogSimuGUI;
	int size =100;
	
	Comparator<SessionCurrency> comparatorBestEligible = new Comparator<SessionCurrency>() {

		@Override
		public int compare(SessionCurrency o1, SessionCurrency o2) {
			// TODO Auto-generated method stub
			return 0;
		}
	};
	public SimuProcess(DialogSimuGUI dialogSimuGUI) {
		super();
		this.dialogSimuGUI = dialogSimuGUI;
		startSimuProcess();
	}

	public void startSimuProcess() {
		dateStart = new Date(dateEnd.getTime() - duree_mn*60000l);
		t.setDaemon(true);
		t.setName("SimuProcess");
		t.start();
	}

	@Override
	public void run() {
		List<SessionCurrencies> listSessionCurrencies_  = SessionCurrenciesFactory.instance.getSessionsCurrencies(size);
		
		if (listSessionCurrencies_ == null){
			log("listSessionCurrencies_ is null !!!");
		}else {
			System.out.println("Simu run size :"+listSessionCurrencies_.size());
			for(int i = listSessionCurrencies_.size() -1; i>=0;i--){
				SessionCurrencies sc = listSessionCurrencies_.remove(i);
				process(sc);
			}
		}
	}

	

	private void process(SessionCurrencies sc) {
		//System.out.println("simu sc date :"+sc.getDate()+"  "+sc.getTickerBest());
		List<SessionCurrency> list = sc.getlSessionCurrency();
		Collections.sort(list, comparatorBestEligible);
		SessionCurrency sessionCurrency = getFirstEligible(list);
	}

	private SessionCurrency getFirstEligible(List<SessionCurrency> list) {
		for(SessionCurrency sc : list){
			if(isEligible(sc)){
				return sc;
			}
		}
		return null;
	}

	private boolean isEligible(SessionCurrency sc) {
		boolean b1 = false;
		boolean b2 = false;
		if (sc.getHourlyChangePerCentByDayInstant() > 0){
			b1 = true;
		}
		EtatSTOCHASTIQUE etat_10mn = sc.getEtatStochastique_10mn();
		if (etat_10mn.acheter){
			b2=true;
		}
		return b1 & b2;
	}

	private void log(String string) {
		dialogSimuGUI.log(string);
	}

}
