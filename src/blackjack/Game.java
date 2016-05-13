package blackjack;

public class Game {
	private Player player;
	private Dealer dealer;
	private BS bs;
	Shoe shoe;
	Junk junk;
	private Strategy strategy;
	private boolean ingame;
	private boolean wasASplit;
	private boolean insuranceMode;
	private boolean firstplay;
	private boolean alreadyBet;
	int count_splits;
	
	Game(Shoe shoe, Junk junk, Player p, Dealer d, Strategy s, BS bss){
		player = p;
		dealer = d;
		strategy = s;
		this.shoe = shoe;
		this.junk=junk;
		wasASplit=false;
		ingame=false;
		insuranceMode=false;
		firstplay = true;
		alreadyBet=false;
		count_splits=0;
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
	
	public void makeBet(double b, double min_bet){
		if(alreadyBet) {
			if(!player.reBet(b,min_bet)){
				System.out.println("[!]Not enough credits");////////////////////sair
			}
			System.out.println("player is betting "+player.getBet());
			return;
		}
		alreadyBet=true;
		if(!player.bet(b,min_bet)){
			System.out.println("[!]Not enough credits");////////////////////sair
		}
		alreadyBet=true;
		System.out.println("player is betting "+player.getBet());
	}
	
	public void deal(double min_bet){
		if(!player.deal()) return;
		dealer.drawFirstFour(player);
		strategy.addPlays();
		dealer.printDealersFirstTwo();
		System.out.println(player);
		bs.countCard(player.getHand().getCards().getFirst());
		bs.countCard(player.getHand().getCards().getLast());
		bs.countCard(dealer.getDealerHand().getCards().getFirst());
		ingame = true;
	}
	
	public void split(){
		if(count_splits==3){
			System.out.println("can't split more");
			stand();
			return;
		}
		if(player.getBalance()<player.getBet()){
			System.out.println("Not enough money");
			stand();
			return;
		}
		player.split(dealer.dealCard(),dealer.dealCard());
		bs.countCard(player.hands.get(player.getCurrentHand()).cards.get(1));
		bs.countCard(player.hands.get(player.getCurrentHand()+1).cards.get(1));
		player.printStart("p");
		strategy.addPlays();
		++count_splits;
		wasASplit=true;
		firstplay = true;
	}
	
	public void insurance(){
		//if it was already a split, insurance can't be made
		if(wasASplit){
			System.out.println("Can't make insurance after spliting");
			return;
		}
		System.out.println("player makes insurance");
		if(player.setInsuranceBet()){
			insuranceMode=true;
		}else{
			System.out.println("Not enough money to make this play");
		}
	}
	
	public void hit(){
		firstplay = false;
		player.hit(dealer.dealCard());
		bs.countCard(player.getHand().cards.getLast());
		player.printStart("h");
		if(player.getCurrentHand()<player.getNumHands()){
			if(player.getHand().getTotal()>21){
				player.printBustedHand();
				player.setBetZero();
				player.setCurrentHand(player.getCurrentHand()+1);
				if(player.getCurrentHand()==player.getNumHands()){
					player.setCurrentHand(player.getCurrentHand()-1);
					finalizeDealer();
					return;
				}
				player.printPlayingHand();
				System.out.println(player);
				firstplay = true;
			}
		}
	}
	
	public void stand(){
		firstplay = true;
		if(player.getCurrentHand()+1<player.getNumHands()){
			player.printStart("s");
			player.setCurrentHand(player.getCurrentHand()+1);
			player.printPlayingHand();
			System.out.println(player);
			return;
		}
		else{
			player.printStart("s");
			finalizeDealer();
		}
	}

	public boolean dealerFinalizeCards(){
		if(player.allHandsBusted()){
			System.out.println("dealer stands");
			return true;
		}
		while(dealer.hand.getTotal()<17){
			dealer.drawCardToDealer();
			bs.countCard(dealer.getDealerHand().cards.getLast());
			System.out.println("dealer hits");
			System.out.println(dealer);
			if(dealer.hand.getTotal()>21){
				/* When the dealer busts */
				System.out.println("dealer busts");
				int i = 0;
				player.setCurrentHand(i);
				while(player.getCurrentHand()<player.getNumHands()){
					if(player.getBet()==0){
						player.printWLP("loses");
						strategy.addLoses();
					}else{
						player.setBalance(player.getBet()*2);
						player.printWLP("wins");
						strategy.addWins();
					}
					player.setCurrentHand(++i);
				}
				cleanTable();
				return false;
			}
		}
		System.out.println("dealer stands");
		return true;
	}
	
	public void finalizeDealer(){
		firstplay = true;
		System.out.println(dealer);
		bs.countCard(dealer.getDealerHand().cards.get(1));
		/* Check if the player has blackjack */
		if(player.getHand().blackjack() && !wasASplit){
			strategy.addPlayerbj();
			System.out.println("Player got a BlackJack!");
			if(dealer.hand.blackjack()){
				System.out.println("Dealer got a BlackJack!");
				if(insuranceMode){
					player.setBalance(2*player.getInsuranceBet());
					System.out.println("Player wins insurance");
					insuranceMode=false;
				}
				player.setBalance(player.getBet());
				System.out.println("player pushes and his current balance is "+player.getBalance());
				strategy.addDealerbj();
				strategy.addPushes();
				cleanTable();
				return;
			}else{
				if(insuranceMode){
					System.out.println("Player loses insurance");
					insuranceMode=false;
				}
				player.setBalance((player.getBet())*2.5);//Should pay 2.5
				System.out.println("Player wins and his current balance is "+player.getBalance());
				strategy.addWins();
				cleanTable();
				return;
			}
		}
		/* Checks if the dealer has blackjack */
		if(dealer.hand.blackjack()){
			strategy.addDealerbj();
			if(insuranceMode){
				System.out.println("Dealers BlackJack!");
				player.setBalance(2*player.getInsuranceBet());
				System.out.println("Player wins insurance and his current balance is "+player.getBalance());
				strategy.addLoses();
				cleanTable();
				insuranceMode=false;
				return;
			}else{
				System.out.println("Dealers BlackJack!");
				System.out.println("Player loses and his current balance is "+player.getBalance());
				strategy.addLoses();
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
			return;
		}
		
		int n = 0;
		player.setCurrentHand(n);
		
		while((player.getCurrentHand())<player.getNumHands()){
			
			/* If no one busts the game checks who is the winner */
			if(player.getBet()==0){
				player.printWLP("loses");
				strategy.addLoses();
				player.setCurrentHand(++n);
				continue;
			}
			if(player.getHand().getTotal()>dealer.hand.getTotal()){
				player.setBalance((player.getBet())*2);
				strategy.addWins();
				player.printWLP("wins");
			}else if(player.getHand().getTotal()==dealer.hand.getTotal()){
				//If he has a 21 hand value of two cards, it wins an ordinary 21 made of more than 2 cards
				if(player.getHand().countCards()==2 && player.getHand().genTotal()==21){
					player.setBalance(2*player.getBet());
					strategy.addWins();
					player.printWLP("wins");
				}else{
					player.setBalance(player.getBet());
					strategy.addPushes();
					player.printWLP("pushes");
				}
			}
			else{
				strategy.addLoses();
				player.printWLP("loses");
			}
			
			player.setCurrentHand(++n);
		}
		cleanTable();
	}
	
	public void doubleDown(){
		if(player.getBalance()<player.getBet()){
			System.out.println("can't make this play");
			stand();
			return;
		}
		if(!player.getHand().worthForDouble()){
			System.out.println("can't make this play");
			return;
		}
		player.doubleDown(dealer.dealCard());
		System.out.println("player doubles down");
		firstplay = false;
		bs.countCard(player.getHand().cards.getLast());
		System.out.println(player);
		if(player.getCurrentHand()<player.getNumHands()){
			if(player.getHand().getTotal()>21){
				System.out.println("player busts "+"["+(player.getCurrentHand()+1)+"]" + "th hand busts");
				player.setBetZero();
				player.setCurrentHand(player.getCurrentHand()+1);
				if(player.getCurrentHand()==player.getNumHands()){
					player.setCurrentHand(player.getCurrentHand()-1);
					System.out.println(player);
					finalizeDealer();
					return;
				}
				System.out.println(player);
				firstplay = true;
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
		alreadyBet=false;
		count_splits=0;
	}
	
	public double getPercentageDeck(){
		return junk.countCards()/(junk.countCards()+shoe.countCards())*100; //modificar para deck.total
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
		if(wasASplit){
			System.out.println("can't surrend");
			stand();
			return;
		}
		System.out.println("player is surrendering");
		System.out.println(dealer);
		player.setBalance(0.5*player.getBet());
		System.out.println("player current balance is "+player.getBalance());
		strategy.addLoses();
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