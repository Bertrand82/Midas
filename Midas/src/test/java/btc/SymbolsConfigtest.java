package btc;

import org.junit.Test;

import bg.panama.btc.swing.SymbolsConfig;



public class SymbolsConfigtest {

	@Test
	 public  void processTest() throws Exception {
		System.out.println("Symbols Test");
		SymbolsConfig symbols = SymbolsConfig.getInstance();
		symbols.store();
		System.out.println("Symbols Done "+symbols.gethSymbols().size());
	}
	
}
