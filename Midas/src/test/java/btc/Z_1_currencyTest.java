package btc;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import btc.model.v2.ITicker;
import btc.model.v2.Ticker;
import btc.trading.first.SessionCurrency;

public class Z_1_currencyTest {

	ITicker ticker = new ITicker() {
		
		@Override
		public double getHourlyPrice() {
			// TODO Auto-generated method stub
			return 100;
		}

		@Override
		public double getVolumeDaily() {
			// TODO Auto-generated method stub
			return 100;
		}
		
		@Override
		public String getShortName() {
			// TODO Auto-generated method stub
			return "tTestShort";
		}
		
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "TTest";
		}
		
		@Override
		public double getLowDaily() {
			// TODO Auto-generated method stub
			return 100;
		}
		
		@Override
		public double getLastPrice() {
			// TODO Auto-generated method stub
			return 100;
		}
		
		@Override
		public double getHighDaily() {
			// TODO Auto-generated method stub
			return 100;
		}
		
		@Override
		public double getDaylyChangePerCent() {
			// TODO Auto-generated method stub
			return 0.001;
		}
		
		@Override
		public double getDaylyChange() {
			// TODO Auto-generated method stub
			return 100;
		}

		@Override
		public double getHourlyChangePerCent() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setHourlyChangePerCent(double d) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public long getDeltaTemps_ms() {
			// TODO Auto-generated method stub
			return 1000000000;
		}

		@Override
		public void setDeltaTemps_ms(long delta) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Date getDate() {
			// TODO Auto-generated method stub
			return new Date();
		}

		@Override
		public int getNumero() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setNumero(int number) {
			// TODO Auto-generated method stub
			
		}
		
		
	};
	@Test
	 public  void processTest() throws Exception {
		SessionCurrency z = new SessionCurrency(ticker);
		for(int i=0; i<1000;i++){
			z.update(ticker);
			assertEquals(ticker.getLastPrice(),z.getLastPrice(),0.000001);
			assertEquals(ticker.getDaylyChangePerCent(),z.getDaylyChangePerCent(),0.000001);

		}
		for(int i=0; i<10;i++){
			Thread.sleep(10);
			z.update(ticker);
			assertEquals(ticker.getLastPrice(),z.getLastPrice(),0.000001);
		}
	}
}
