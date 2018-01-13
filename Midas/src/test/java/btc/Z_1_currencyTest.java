package btc;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import bg.panama.btc.model.v2.ITicker;
import bg.panama.btc.model.v2.Ticker;
import bg.panama.btc.trading.first.SessionCurrencies;
import bg.panama.btc.trading.first.SessionCurrency;

public class Z_1_currencyTest {

	Ticker ticker = new Ticker();
		
		
	@Test
	 public  void processTest() throws Exception {
		SessionCurrency z = new SessionCurrency(ticker,new SessionCurrencies());
		for(int i=0; i<1000;i++){
			z.update(ticker);
			
		}
		for(int i=0; i<10;i++){
			Thread.sleep(10);
			z.update(ticker);
		}
	}
}
