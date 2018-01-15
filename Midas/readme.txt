mvn clean compile assembly:single

Architecture :
	Un process de dialogue avec la plateforme: Recherche des tickers et mise en BDD (Classe ThreadFetchTickers)
	Un process de traitement des tickers , de calcul des indicateurs (stochastique, filtre) et de mise en bdd. (Classe ThreadProcessTickers)
	Un processus de recherche de la Balance (infos du compte) et de mise en BDD. (Classe ThreadBalance)
	Un processus 'de decision " qui selectionne la bonne devise .. TODO 
	Un processus de gestion des ordres. TODO
	
	Visu :Une visu/console tableau de bord mise a jour constamment.
	
	Module de rejeu TODO
	
	
FAQ

"message":"Invalid order: not enough tradable balance for  .... Le montant de l'ordre est trop important . 
Lorsque l'on passe des ordres et qu'ils ne sont pas executé, le montant de ces ordres est deduit de la balance. Il faut soit attendre, soit annulé les ordres (Quis sont mal positionné en terme de prix)