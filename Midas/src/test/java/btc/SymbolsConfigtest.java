package btc;

import static org.junit.Assert.*;

import java.io.File;

import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

import btc.model.Symbols;
import btc.swing.ProtectedConfigFile;
import btc.swing.SymbolsConfig;


public class SymbolsConfigtest {

	@Test
	 public  void processTest() throws Exception {
		System.out.println("Symbols Test");
		SymbolsConfig symbols = SymbolsConfig.getInstance();
		symbols.store();
		System.out.println("Symbols Done "+symbols.gethSymbols().size());
	}
	
}
