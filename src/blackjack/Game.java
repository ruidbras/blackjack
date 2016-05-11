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
	private boolean simulation;
	
	Game(Shoe shoe, Junk junk, Player p, Dealer d, Strategy s, BS bss, boolean sim){
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
		simulation=!sim;
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
				if(simulation) System.out.println("[!]Not enough credits");////////////////////sair
			}
			return;
		}
		alreadyBet=true;
		player.bet(b,min_bet);
		alreadyBet=true;
		if(simulation) System.out.println("player is betting "+player.getBet());
	}
	
	public void deal(double min_bet){
		if(!player.deal()) return;
		dealer.drawFirstFour(player);
		strategy.addPlays();
		if(simulation) dealer.printDealersFirstTwo();
		if(simulation) System.out.println(player);
		bs.countCard(player.getHand().getCards().getFirst());
		bs.countCard(player.getHand().getCards().getLast());
		bs.countCard(dealer.getDealerHand().getCards().getFirst());
		ingame = true;
	}
	
	public void split(){
		if(count_splits==3){
			if(simulation) {
				System.out.println("can't split more");
			}else stand();
			return;
		}
		if(player.getBalance()<player.getBet()){
			if(simulation){
				System.out.println("Not enough money");
			}else stand();
			return;
		}
		player.split(dealer.dealCard(),dealer.dealCard());
		bs.countCard(player.hands.get(player.getCurrentHand()).cards.get(1));
		bs.countCard(player.hands.get(player.getCurrentHand()+1).cards.get(1));
		strategy.addPlays();
		++count_splits;
		wasASplit=true;
		firstplay = true;
		player.runHands();
		if(simulation) System.out.println("player is splitting");
		if(simulation) System.out.println("playing "+(player.getCurrentHand()+1)+"nd hand...");
		if(simulation) System.out.println(player);
	}
	
	public void insurance(){
		//if it was already a split, insurance can't be made
		if(wasASplit){
			if(simulation) System.out.println("Can't make insurance after spliting");
			return;
		}
		if(simulation) System.out.println("player makes insurance");
		if(player.setInsuranceBet()){
			if(simulation) System.out.println("Current balance: "+player.getBalance());
			insuranceMode=true;
		}else{
			if(simulation) System.out.println("Not enough money to make this play");
		}
	}
	
	public void hit(){
		//If it results from a split of a pair of Aces
		if(player.runHands()==false){
			if(simulation){
				System.out.println("You can only split or stand");
			}else stand();
			return;
		}
		if(simulation) System.out.println("player hits");
		firstplay = false;
		player.hit(dealer.dealCard());
		bs.countCard(player.getHand().cards.getLast());
		if(simulation) System.out.println(player);
		if(player.getCurrentHand()<player.getNumHands()){
			if(player.getHand().getTotal()>21){
				if(simulation) System.out.println("player's "+"["+(player.getCurrentHand()+1)+"]" + "th hand busts");
				strategy.addLoses();
				player.setBetZero();
				player.setCurrentHand(player.getCurrentHand()+1);
				if(player.getCurrentHand()==player.getNumHands()){
					player.setCurrentHand(player.getCurrentHand()-1);
					finalizeDealer();
					return;
				}
				if(simulation) System.out.println(player);
				firstplay = true;
			}
		}
	}
	
	public void stand(){
		firstplay = true;
		if(player.getCurrentHand()+1<player.getNumHands()){
			if(simulation) System.out.println("player stands "+"["+(player.getCurrentHand()+1)+"]");
			player.setCurrentHand(player.getCurrentHand()+1);
			if(simulation) System.out.println(player);
			player.runHands();
			return;
		}
		else{
			if(simulation) System.out.println("player stands");
			finalizeDealer();
		}
	}
	
	
	
	public boolean dealerFinalizeCards(){
		if(player.allHandsBusted()){
			if(simulation) System.out.println("dealer stands");
			cleanTable();
			return false;
		}
		while(dealer.hand.getTotal()<17){
			dealer.drawCardToDealer();
			bs.countCard(dealer.getDealerHand().cards.getLast());
			if(simulation) System.out.println("dealer hits");
			if(simulation) System.out.println(dealer);
			if(dealer.hand.getTotal()>21){
				/* When the dealer busts */
				if(simulation) System.out.println("dealer busts");
				int i = player.getNumHands()-1;
				player.setCurrentHand(i);
				while(player.getCurrentHand()>=0){
					player.setBalance(player.getBet()*2);
					strategy.addWins();
					player.setCurrentHand(--i);
				}
				cleanTable();
				return false;
			}
		}
		if(simulation) System.out.println("dealer stands");
		return true;
	}
	
	public void finalizeDealer(){
		firstplay = true;
		if(simulation) System.out.println(dealer);
		bs.countCard(dealer.getDealerHand().cards.get(1));
		/* Check if the player has blackjack */
		if(player.getHand().blackjack() && !wasASplit){
			if(simulation) System.out.println("Player got a BlackJack!");
			if(dealer.hand.blackjack()){
				if(simulation) System.out.println("Dealer got a BlackJack!");
				if(insuranceMode){
					player.setBalance(2*player.getInsuranceBet());
					if(simulation) System.out.println("Player wins insurance");
					insuranceMode=false;
				}
				player.setBalance(player.getBet());
				if(simulation) System.out.println("player pushes and his current balance is "+player.getBalance());
				strategy.addDealerbj();
				strategy.addPlayerbj();
				strategy.addPushes();
				cleanTable();
				return;
			}else{
				if(insuranceMode){
					if(simulation) System.out.println("Player loses insurance");
					insuranceMode=false;
				}
				player.setBalance((player.getBet())*2.5);//Should pay 2.5
				if(simulation) System.out.println("Player wins and his current balance is "+player.getBalance());
				strategy.addDealerbj();
				strategy.addWins();
				cleanTable();
				return;
			}
		}
		/* Checks if the dealer has blackjack */
		if(dealer.hand.blackjack()){
			if(insuranceMode){
				if(simulation) System.out.println("Dealers BlackJack!");
				player.setBalance(2*player.getInsuranceBet());
				if(simulation) System.out.println("Player wins insurance and his current balance is "+player.getBalance());
				cleanTable();
				insuranceMode=false;
				return;
			}else{
				if(simulation) System.out.println("Dealers BlackJack!");
				if(simulation) System.out.println("Player loses and his current balance is "+player.getBalance());
				strategy.addLoses();
				strategy.addDealerbj();
				insuranceMode = false;
				cleanTable();
				return;
			}
		}else{
			if(insuranceMode){
				if(simulation) System.out.println("Player loses insurance");
				insuranceMode=false;
			}
		}
		
		if(dealerFinalizeCards()==false){
			if(simulation) System.out.println("Player wins and his current balance is "+player.getBalance());
			strategy.addWins();
			return;
		}
		
		int n = 0;
		player.setCurrentHand(n);
		
		while((player.getCurrentHand())<player.getNumHands()){
			
			/* If no one busts the game checks who is the winner */
			if(player.getBet()==0){
				if(simulation) System.out.println("player loses ["+(player.getCurrentHand()+1)+"] and his current balance is "+player.getBalance());
				player.setCurrentHand(++n);
				continue;
			}
			if(player.getHand().getTotal()>dealer.hand.getTotal()){
				player.setBalance((player.getBet())*2);
				strategy.addWins();
				if(simulation) System.out.println("player wins ["+(player.getCurrentHand()+1)+"] and his current balance is "+player.getBalance());
			}else if(player.getHand().getTotal()==dealer.hand.getTotal()){
				//If he has a 21 hand value of two cards, it wins an ordinary 21 made of more than 2 cards
				if(player.getHand().countCards()==2 && player.getHand().genTotal()==21){
					player.setBalance(2*player.getBet());
					strategy.addWins();
					if(simulation) System.out.println("player wins ["+(player.getCurrentHand()+1)+"] and his current balance is "+player.getBalance());
				}else{
					player.setBalance(player.getBet());
					strategy.addPushes();
					if(simulation) System.out.println("player pushes ["+(player.getCurrentHand()+1)+"] and his current balance is "+player.getBalance());
				}
			}
			else{
				strategy.addLoses();
				if(simulation) System.out.println("player loses ["+(player.getCurrentHand()+1)+"] and his current balance is "+player.getBalance());
			}
			
			player.setCurrentHand(++n);
		}
		cleanTable();
	}
	
	public void doubleDown(){
		//If it results from a split of a pair of Aces
		if(player.runHands()==false){
			if(simulation){
				System.out.println("You can only split or stand");
			}else stand();
			return;
		}
		if(!player.doubleDown(dealer.dealCard())){
			if(simulation){
				System.out.println("can't make this play");
			}else stand();
			return;
		}
		if(simulation) System.out.println("player doubles down");
		firstplay = false;
		bs.countCard(player.getHand().cards.getLast());
		if(simulation) System.out.println(player);
		
		if(player.getCurrentHand()<player.getNumHands()){
			if(player.getHand().getTotal()>21){
				if(simulation) System.out.println("player busts "+"["+(player.getCurrentHand()+1)+"]" + "th hand busts");
				strategy.addLoses();
				player.setBetZero();
				player.setCurrentHand(player.getCurrentHand()+1);
				if(player.getCurrentHand()==player.getNumHands()){
					player.setCurrentHand(player.getCurrentHand()-1);
					if(simulation) System.out.println(player);
					finalizeDealer();
					return;
				}
				if(simulation) System.out.println(player);
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
		if(simulation)System.out.println("Inputs are ok");
	}
	
	public void surrender(){
		if(wasASplit){
			if(simulation){
				System.out.println("can't surrend");
			}else stand();
		}
		if(simulation) System.out.println("player is surrendering");
		if(simulation) System.out.println(dealer);
		player.setBalance(0.5*player.getBet());
		if(simulation) System.out.println("player current balance is "+player.getBalance());
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