package btc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import btc.model.v2.ITicker;
import btc.model.v2.Ticker;
import btc.trading.first.Z_1_Currency;

public class Z_1_currencyTest {

	ITicker ticker = new ITicker() {
		
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
	};
	@Test
	 public  void processTest() throws Exception {
		Z_1_Currency z = new Z_1_Currency(ticker);
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
