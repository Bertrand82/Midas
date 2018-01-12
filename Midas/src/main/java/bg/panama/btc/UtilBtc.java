package bg.panama.btc;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;



public class UtilBtc {

	public final static DecimalFormat df;
	static {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		
		df = new DecimalFormat ("#########.###############",dfs) ;
	}
	
}
