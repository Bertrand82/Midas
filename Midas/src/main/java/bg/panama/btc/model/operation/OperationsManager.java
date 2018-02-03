package bg.panama.btc.model.operation;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.service.spi.ServiceRegistryAwareService;

import bg.panama.btc.BitfinexClient;
import bg.panama.btc.BitfinexClientFactory;
import bg.panama.btc.OrderFactory;
import bg.panama.btc.OrderManager;
import bg.panama.btc.PanicThreadOrderSaveAllInUsd;
import bg.panama.btc.model.ActiveOrder;
import bg.panama.btc.model.ActiveOrders;
import bg.panama.btc.model.Balance;
import bg.panama.btc.model.Balances;
import bg.panama.btc.swing.SymbolConfig;
import bg.panama.btc.swing.SymbolsConfig;
import bg.panama.btc.trading.first.Config;
import bg.panama.btc.trading.first.ServiceCurrencies;
import bg.panama.btc.trading.first.SessionCurrencies;
import bg.panama.btc.trading.first.SessionCurrency;

public class OperationsManager {

	private static final Logger loggerOrder = LogManager.getLogger("orders");

	public static OperationsManager instance = new OperationsManager();

	private static Config config = Config.getInstance();

	private List<Operation> listOperations = new ArrayList<>();
	
	private long timePanic = 0;
	
	private OperationsManager() {
		this.listOperations = OperationEntitiesFactory.instance.getListOperationInit();
		this.updateListOperation();
	}

	

	private void updateListOperation() {
		System.err.println("UpdateListOperation");
		ActiveOrders activeOrders = OrderFactory.getInstance().getAllActivesOrders();
		for(ActiveOrder activeOrder : activeOrders.getlOrders()){
			System.out.println(" -------------- "+activeOrder.getShortName()+"   "+activeOrder);
			
		}
	}



	public void emergencySaveInDollar(String from) {
		// Vomme l'ordre panic annule tous les ordres en cours, Un ordre panic annule aussi les ordres de ventes panic precedent ... 
		// Aussi une panique toute les 3 minutes, ca suffit ! 
		// TODO a mettre dans le fichier de config
		long deltaPanicMax_ms = config.getDeltaPanicMax_ms();
		if ((System.currentTimeMillis() - timePanic) > 3 * 60000) {
			BitfinexClient bitfinexClient = BitfinexClientFactory.getBitfinexClientAuthenticated();
			loggerOrder.info("emergencySaveInDollar from :" + from);
			PanicThreadOrderSaveAllInUsd panicThread = new PanicThreadOrderSaveAllInUsd(bitfinexClient);
			panicThread.saveAllInUSD("from OrderFactory from " + from);
		}
	}

	public static List<Order> saveAllInDollar(SessionCurrencies session, Balances balances) {

		List<Order> orders = new ArrayList<>();
		for (Balance balance : balances.getlBalancesExchange()) {
			String currency = balance.getCurrency();
			if ("usd".equalsIgnoreCase(currency)) {
				// PAs d'ordres
			} else if (balance.getAvailableInDollar() < 30) {
				// minimum order size between 10-25 USD
			} else {
				Order order = new Order(currency, balance.getAvailable(), Order.Side.sell, Order.TypeChoicePrice.panic);
				orders.add(order);
			}
		}
		System.err.println(" saveAllInDollar orders " + orders.size());
		return orders;
	}

	public static List<Operation> processOrdersVent___DEPRECTED(Balances balances) {
		List<Operation> orders = new ArrayList<>();
		SessionCurrencies sessionCurrencies = ServiceCurrencies.getInstance().getSessionCurrencies();
		if (sessionCurrencies == null) {
			return orders;
		}
		if (sessionCurrencies.isModePanic()) {
			// C'ets traité par ailleurs
			return orders;
		}
		for (Balance balance : balances.getlBalancesExchange()) {
			String currency = balance.getCurrency();
			if ("usd".equalsIgnoreCase(currency)) {
				// PAs d'ordres =))
			} else if (balance.getAvailableInDollar() < 30) {
				// minimum order size between 10-25 USD
			} else {
				SessionCurrency sc = sessionCurrencies.getSessionCurrency_byShortName(currency);

				if (sc == null) {
					System.err.println("No sessionCurrency for :" + currency);
				} else {
					SessionCurrency.EtatSTOCHASTIQUE etat_10mn = SessionCurrency
							.getStochastique(sc.getStochastique_10mn());
					System.err.println(
							"processOrdersVente " + currency + "  " + etat_10mn + "    vendre: " + etat_10mn.vendre);
					if (etat_10mn.vendre) {
						// Order orderVente = new
						// Order(sc.getShortName(),balance.getAvailable(),Order.Side.sell,
						// Order.TypeChoicePrice.fromTickers);
						// balance.addOrderVente(orderVente.getAmmount());
						// orders.add(orderVente);
					}
				}
			}
		}
		System.err.println("processOrdersVente orders " + orders.size());
		return orders;
	}

	/**
	 * GEnere un ordre vers le best
	 * 
	 * @param sessionBest
	 * @return
	 */
	public static Operation processSimpleOrderUsdToBest(Balances balances, SessionCurrency sessionBest,
			double maxAmountInDolllar) {
		if (sessionBest == null) {
			return null;
		}

		SessionCurrencies sessionCurrencies = sessionBest.getSessionCurrencies();
		String currencyUSD = "usd";
		Balance balanceUsd = balances.getBalance(currencyUSD);

		SymbolConfig symbolTickerBest = SymbolsConfig.getInstance().getSymbolConfig(sessionBest.getShortName());

		String currencyBest = sessionBest.getShortName();
		Balance balanceBest = balances.getBalance(currencyBest);

		double amountMAxDollar = Balance.getAchatMaxInDollar(currencyBest);

		Operation order = null;
		if (currencyBest.equalsIgnoreCase(currencyUSD)) {
			// PAs d'ordres! evidemment. Ne devrait jamais arriver
		} else if (sessionCurrencies.isModePanic()) {
			// Pas d'ordre. Le mode panic est géré par ailleurs mais il n'est
			// pas necessaire d'acheter
			logDebug("Is Mode Panic");
		} else if (sessionCurrencies.getAmbianceMarket().getNbPanic() > 0) {
			// Pas d'ordre. Le mode panic a fait disjoncter le system.
			logDebug("Mode Panic No removed");
		} else if (balances.getTotalCryptoAmountInDollar() > Config.getInstance().getPlafondCryptoInDollar() * 0.9) {
			// Pas d'ordre. Le mode panic a fait disjoncter le system.
			logDebug("Plafond crypto atteint");
		} else if (balanceUsd.getAvailableInDollar() < 50) {
			// minimum order size between 10-25 USD . Il n'y a plus de dollar
		} else if (balanceUsd.getAvailable() != balanceUsd.getAmount()) {
			// Il y a des ordres "en cours", eventuellement des ordre d'achat de
			// cette devise ....
			logDebug("Ordre achat usd en cours (available != amount)");
		} else if (sessionCurrencies.getNumero() < 10) {
			// Pas d'ordre: les filtres ne sont pas initialisés
			// Pas d'ordre: il y a assez d'ordre sur Balance Best
		} else if ((balanceBest != null) && (balanceBest.getAmountInDollar() > 100)) {
			// Le compte a déja été alimenté. A Modifier
		} else {
			double amountOrderInDollar = (balanceUsd.getAvailable()) * 0.9;
			if (symbolTickerBest.getMaxTrade() == 0) {

			} else {

				amountOrderInDollar = Math.min(amountMAxDollar, amountOrderInDollar);
				amountOrderInDollar = Math.min(Config.getInstance().getPlafondCryptoInDollar(), amountOrderInDollar);
			}
			amountOrderInDollar = Math.min(Config.getInstance().getPlafondCurrencyInDollard(), amountOrderInDollar); // ECRETAGE
																														// POUR
																														// QUALIF
			double price = sessionBest.getTicker_Z_1().getLastPrice();
			double amount = amountOrderInDollar / price;
			order = new Operation(sessionBest.getShortName(), amount, Order.TypeChoicePrice.fromTickers);
			order.getOrderAchat().setComment("orderToBest limit : " + maxAmountInDolllar + "");
		}
		return order;
	}

	private static void logDebug(String s) {
		System.err.println(s);
	}

	public static void processOperationsAchat(List<Operation> operationsAchat) {
		System.err.println("No Implemented yet");
		if (config.isOrderAbleAchat()) {
			for (Operation operation : operationsAchat){
				operation.achat();
			}
			
		}

	}

	public static List<Order> processOrdersVent() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void addOperation(Operation operation) {
		listOperations.add(operation);
	}



	public static OperationsManager getInstance() {
		return instance;
	}

}
