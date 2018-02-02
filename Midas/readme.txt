


Robot de Trading:


Utilise uniquement la plateforme bitfinex
Fonctions Principales 
	Évalue la situation générale, 
	Qualifie le comportement des monaies, 
	Identifie la meilleure monaie,
	Gere l'achat et la vente de monaie.

Evaluer la situation générale:

Si 80% (seuil_panic) des monnaies sont en baisse: Mode Panic : Tout vendre
Si 60% (seuil_arret_achat) des monnaies sont en baisse : Mode Attente : Ne plus acheter
Si 80%  (seuil_achat) des monaies sont en hausse : Mode Trading : Achats autorisés

Qualifier le comportement des monaies :

Price (Last price)
Price (Moyenne des derniers prix)
Stochastique 10mn	   ----> Autorise ou interdit l'achat
Stochastique 1 heure   ----> Autorise /ou interdit l'achat; déclenche la vente
Liste des min/max des 2 heures precedentes

Identifier la meileure monaie :
Celle qui a le plus de potentiel : Max (last_Max - price)/price
Celle qui a le plus croissance moyenne 

Gérer operation (achat et  vente)

Passer un ordre d'achat au prix 
	
	
	Lors de la création d'une opération : 
		Montant : Montant max par devise, Montant total exposé, 
		Prix Achat : 
			- au last_price du ticker 
			- A partir des orders Asks et Bids (Bid+ask)/2
			- max ask 
		Prix de vente (Stop) : Price dernier minimum
		Prix de vente (Limit) : Dernier max de l'heure
		
		
		Déclenchement de l'ordre de vente:
			Changement du stochastique
			Limmite(>Haute ou basse) fixée au départ atteinte
			panic (Traiter à part)
	

TODO LONG: 
Simulation sur plusieurs jours, avec plusieurs "algo" en parallèle
Traitement d'autres plateformes

TODO SHORT:
Horloge centrale (Permettant de re-jouer en accéléré)
Validation cycle de vie d'une opération: Visualisation des operations, des ordres, de leur bilan (Si terminé)
Visualisation du fichier de config
Implementer connecteur acquisition/balance/order Simu ou Bitfinex
	
	
	
FAQ

Q  : Comment builer :
mvn clean compile assembly:single

Q : "message":"Invalid order: not enough tradable balance for  .... 
Le montant de l'ordre est trop important . 
Lorsque l'on passe des ordres et qu'ils ne sont pas executé, le montant de ces ordres est deduit de la balance. Le montant disponible (available) n'est plus le montant total.
 Il faut soit attendre que les ordres passe effectivement, soit annulé les ordres (Quis sont sans doute mal positionné en terme de prix)