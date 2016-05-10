package blackjack;

public class Game {
	private Player player;
	private Dealer dealer;
	private BS bs;
	Deck deck;
	Junk junk;
	private Strategy strategy;
	private boolean ingame;
	private boolean wasASplit;
	private boolean insuranceMode;
	private boolean firstplay;
	private boolean alreadyBet;
	private boolean alreadyDeal;
	
	Game(Deck deck, Junk junk, Player p, Dealer d, Strategy s, BS bss){
		player = p;
		dealer = d;
		strategy = s;
		this.deck = deck;
		this.junk=junk;
		wasASplit=false;
		ingame=false;
		insuranceMode=false;
		firstplay = true;
		alreadyBet=false;
		alreadyDeal=false;
		bs = bss;
	}
	
	public boolean ingame(){
		return ingame;
	}
	public boolean wasASplit(){
		return wasASplit;
	}
	
	public boolean firstplay(){
		return firstplay;
	}
	
	public boolean insuranceMode(){
		return insuranceMode;
	}
	
	public void makeBet(double b){
		if(alreadyBet) {
			player.reBet(b);
			return;
		}
		alreadyBet=true;
		player.bet(b);
	}
	
	public void deal(double min_bet){
		if(alreadyDeal) return;
		alreadyDeal=true;;
		if(!player.deal(min_bet)) return;
		dealer.drawFirstFour(player);
		strategy.addPlays();
		dealer.printDealersFirstTwo();
		bs.countCard(player.getHand().getCards().getFirst());
		bs.countCard(player.getHand().getCards().getLast());
		bs.countCard(dealer.getDealerHand().getCards().getFirst());
		System.out.println(player);
		if(player.getHand().blackjack()){
			finalizeDealer();
			return;
		}
		ingame = true;
	}
	
	public void split(){
		if(player.getBalance()<player.getBet()){
			System.out.println("Not enough money");
			return;
		}
		player.split(dealer.dealCard(),dealer.dealCard());
		bs.countCard(player.hands.get(player.getCurrentHand()).cards.get(1));
		bs.countCard(player.hands.get(player.getCurrentHand()-1).cards.get(1));
		strategy.addPlays();
		wasASplit=true;
		firstplay = true;
		player.runHands();
		System.out.println(player);
	}
	
	public void insurance(){
		//if it was already a split, insurance can't be made
		if(wasASplit){
			System.out.println("Can't make insurance after spliting");
			return;
		}
		System.out.println("Player makes insurance");
		if(player.setInsuranceBet()){
			System.out.println("Current balance: "+player.getBalance());
			insuranceMode=true;
		}
	}
	
	public void hit(){
		//If it results from a split of a pair of Aces
		if(player.runHands()==false){
			stand();
			return;
		}
		System.out.println("Player hits");
		firstplay = false;
		player.hit(dealer.dealCard());
		bs.countCard(player.getHand().cards.getLast());
		dealer.printDealersFirstTwo();
		System.out.println(player);
		if(player.getHand().getTotal()>21 && player.getCurrentHand()>0){
			System.out.println(player.getCurrentHand() + "th hand busts");
			strategy.addLoses();
			dealer.cleanPlayerBustedHand(player);
			firstplay = true;
		}else{
			if(player.getHand().getTotal()>21 && player.getNumHands()==1){
				System.out.println("Player busts");
				strategy.addLoses();
				System.out.println("Dealers Hand: "+dealer.hand);
				bs.countCard(dealer.getDealerHand().cards.get(1));
				if(insuranceMode){
					if(dealer.hand.blackjack()){
						System.out.println("Player wins insurance");
						player.setBalance(2*player.getInsuranceBet());
					}else{
						System.out.println("Player loses insurance");
					}
					insuranceMode = false;
				}
				player.setBetZero();
				if(dealerFinalizeCards()){
					cleanTable();
				}
				System.out.println("Current balance: "+player.getBalance());
				return;
			}if(player.getHand().getTotal()>21 && player.getNumHands()>1){
				System.out.println(player.getCurrentHand() + "th hand busts");
				strategy.addLoses();
				dealer.cleanPlayerBustedHand(player);
				finalizeDealer();
			}
		}
	}
	
	public void stand(){
		firstplay = true;
		if(player.getCurrentHand()>0){
			System.out.println("Player stands in "+player.getCurrentHand()+ " hand");
			player.setCurrentHand(player.getCurrentHand()-1);
			dealer.printDealersFirstTwo();
			System.out.println(player);
			player.runHands();
			return;
		}
		else{
			System.out.println("Player stands");
			finalizeDealer();
		}
	}
	
	public boolean dealerFinalizeCards(){
		while(dealer.hand.getTotal()<17){
			dealer.drawCardToDealer();
			bs.countCard(dealer.getDealerHand().cards.getLast());
			System.out.println("Dealer hits");
			System.out.println("Dealers Hand: "+dealer.hand);
			if(dealer.hand.getTotal()>21){
				/* When the dealer busts */
				System.out.println("Dealer busts");
				int i = 0;
				player.setCurrentHand(i);
				while(player.getCurrentHand()<player.getNumHands()){
					player.setBalance(player.getBet()*2);
					strategy.addWins();
					i=i+1;
					player.setCurrentHand(i);
				}
				cleanTable();
				return false;
			}
		}
		return true;
	}
	
	public void finalizeDealer(){
		firstplay = true;
		System.out.println("Dealers Hand: "+dealer.hand);
		bs.countCard(dealer.getDealerHand().cards.get(1));
		/* Check if the player has blackjack */
		if(player.getHand().blackjack() && !wasASplit){
			System.out.println("Player got a BlackJack!");
			if(dealer.hand.blackjack()){
				System.out.println("Dealer got a BlackJack!");
				if(insuranceMode){
					player.setBalance(2*player.getInsuranceBet());
					System.out.println("Player wins insurance");
					insuranceMode=false;
				}
				player.setBalance(player.getBet());
				System.out.println("Player pushes");
				System.out.println("Current balance: "+player.getBalance());
				strategy.addDealerbj();
				strategy.addPlayerbj();
				strategy.addPushes();
				cleanTable();
				return;
			}else{
				if(insuranceMode){
					System.out.println("Player loses insurance");
					insuranceMode=false;
				}
				player.setBalance((player.getBet())*2.5);//Should pay 2.5
				System.out.println("Player wins");
				System.out.println("Current balance: "+player.getBalance());
				strategy.addDealerbj();
				strategy.addWins();
				cleanTable();
				return;
			}
		}
		/* Checks if the dealer has blackjack */
		if(dealer.hand.blackjack()){
			if(insuranceMode){
				System.out.println("Dealers BlackJack!");
				player.setBalance(2*player.getInsuranceBet());
				System.out.println("Player wins insurance");
				System.out.println("Current balance: "+player.getBalance());
				cleanTable();
				insuranceMode=false;
				return;
			}else{
				System.out.println("Dealers BlackJack!");
				System.out.println("Player loses");
				System.out.println("Current balance: "+player.getBalance());
				strategy.addLoses();
				strategy.addDealerbj();
				insuranceMode = false;
				cleanTable();
				return;
			}
		}else{
			if(insuranceMode){
				System.out.println("Player loses insurance");
				insuranceMode=false;
			}
		}
		
		if(dealerFinalizeCards()==false){
			System.out.println("Player wins");
			System.out.println("Current balance: "+player.getBalance());
			strategy.addWins();
			return;
		}
		
		int n = player.getNumHands()-1;
		player.setCurrentHand(n);
		
		while((player.getCurrentHand())>=0){
			
			/* If no one busts the game checks who is the winner */
			if(player.getHand().getTotal()>dealer.hand.getTotal()){
				player.setBalance((player.getBet())*2);
				strategy.addWins();
				System.out.println("Hand "+player.getCurrentHand()+" wins");
			}else if(player.getHand().getTotal()==dealer.hand.getTotal()){
				//If he has a 21 hand value of two cards, it wins an ordinary 21 made of more than 2 cards
				if(player.getHand().countCards()==2 && player.getHand().genTotal()==21){
					player.setBalance(2*player.getBet());
					strategy.addWins();
					System.out.println("Hand "+player.getCurrentHand()+" wins");
				}else{
					player.setBalance(player.getBet());
					strategy.addPushes();
					System.out.println("Hand "+player.getCurrentHand()+" pushes");
				}
			}
			else{
				strategy.addLoses();
				System.out.println("Hand "+player.getCurrentHand()+ " loses");
			}
			n=n-1;
			player.setCurrentHand(n);
		}
		System.out.println("Current balance: "+player.getBalance());
		cleanTable();
	}
	
	public void doubleDown(){
		//If it results from a split of a pair of Aces
		if(player.runHands()==false){
			System.out.println("You can only split or stand");
			stand();
			return;
		}
		System.out.println("Player doubles down");
		if(!player.doubleDown(dealer.dealCard())){
			return;
		}
		firstplay = false;
		dealer.printDealersFirstTwo();
		System.out.println(player);
		if(player.getHand().getTotal()>21 && player.getCurrentHand()>0){
			System.out.println(player.getCurrentHand() + "th hand busts");
			strategy.addLoses();
			dealer.cleanPlayerBustedHand(player);
			firstplay = true;
			return;
		}else{
			if(player.getHand().getTotal()>21 && player.getNumHands()==1){
				System.out.println("Player busts");
				strategy.addLoses();
				System.out.println("Dealers Hand: "+dealer.hand);
				if(insuranceMode){
					if(dealer.hand.blackjack()){
						System.out.println("Player wins insurance");
						player.setBalance(2*player.getInsuranceBet());
					}else{
						System.out.println("Player loses insurance");
					}
					insuranceMode = false;
				}
				player.setBetZero();
				if(dealerFinalizeCards()){
					cleanTable();
				}
				System.out.println("Current balance: "+player.getBalance());
				return;
			}if(player.getHand().getTotal()>21 && player.getNumHands()>1){
				System.out.println(player.getCurrentHand() + "th hand busts");
				strategy.addLoses();
				dealer.cleanPlayerBustedHand(player);
				finalizeDealer();
			}
		}
		stand();
	}
	
	public void cleanTable(){
		firstplay=true;
		dealer.cleanDealerHand();
		dealer.cleanPlayerHands(player);
		wasASplit=false;
		ingame = false;
		alreadyDeal=false;
		alreadyBet=false;
	}
	
	public double getPercentageDeck(){
		return junk.countCards()/(junk.countCards()+deck.countCards())*100; //modificar para deck.total
	}
	
	public void checkInputs(double min_bet, double max_bet, double balance, int shoe, double shuffle, boolean readshoe){
		if(min_bet < 1){
			System.out.println("Minimum Bet must be greater or equal to 1");
			System.exit(0);
		}
		if(max_bet<10*min_bet || max_bet>20*min_bet){
			System.out.println("Max Bet must be greater than 10 times the Min Bet and lesser than 20 times the Min Bet");
			System.exit(0);
		}
		if(balance<50*min_bet){
			System.out.println("Balance must be greater than 50 times the Min Bet");
			System.exit(0);
		}
		if((shoe<4 || shoe>8) && !readshoe){
			System.out.println("Shoe must be greater than 4 and lesser than 8");
			System.exit(0);
		}
		if((shuffle<10 || shuffle>100) && !readshoe){
			System.out.println("Shuffle must be greater than 10 and lesser than 100");
			System.exit(0);
		}
		System.out.println("Inputs are ok");
	}
	
	public void surrender(){
		player.setBalance(0.5*player.getBet());
		System.out.println("Player surrenders");
		System.out.println("Current balance: "+player.getBalance());
		cleanTable();
	}
	
	public boolean betLimit(double b, double min_bet, double max_bet){
		if(b>=min_bet && b<=max_bet){
			return true;
		}
		else{
			return false;
		}	
	}
}