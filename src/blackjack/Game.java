package blackjack;

//This function has a Player, a Dealer, a Strategy, a Shoe, a Junk, a Statistics
/*ingame is a boolean variable that indicates if a player is in game or not, this variable is set to true when it's typed d, and set to false 
 * when the game ends and the dealer removes all the cards from the table.
 * */
/*wasASplit is set to true when it's made the first split of the game.
 *insuranceMode is set to true after typing i.
 * firstplay means that the player is currently playing one hand that stills have only two cards or not.
 * alreadyBet is set to true after making b command successfully.
 * count_splits is the number of splits performing during one play (from the first time that it's typed d until the cards are removed).
 * This value can be 0 to 3, and no more than 3.
 * 
 */
public class Game {
	private Player player;
	private Dealer dealer;
	private Strategy str;
	private Shoe shoe;
	private Junk junk;
	private Statistics statistics;
	private boolean ingame;
	private boolean wasASplit;
	private boolean insuranceMode;
	private boolean firstplay;
	private boolean alreadyBet;
	public int count_splits;
	public Junk handDealer= new Junk();
	public Junk handPlayer = new Junk();
	public Junk handPlayer2 = new Junk();
	public Junk handPlayer3 = new Junk();
	public Junk handPlayer4 = new Junk();
	
	
	public Game(Shoe shoe, Junk junk, Player p, Dealer d, Statistics s, Strategy str){
		player = p;
		dealer = d;
		statistics = s;
		this.shoe = shoe;
		this.junk=junk;
		wasASplit=false;
		ingame=false;
		insuranceMode=false;
		firstplay = true;
		alreadyBet=false;
		count_splits=0;
		this.str = str;
	}
	//Returns ingame.
	public boolean ingame(){
		return ingame;
	}
	//Returns wasASplit.
	public boolean wasASplit(){
		return wasASplit;
	}
	//Returns firstplay.
	public boolean firstplay(){
		return firstplay;
	}
	//Returns insuranceMode.
	public boolean insuranceMode(){
		return insuranceMode;
	}
	//This method receives a value b that is the quantity to bet and a min_bet that is the minimum bet.
	//If there was already a bet, it's called the reBet() method from Player class. If reBet() returns false than it's because
	//player doesn't have enough credits to bet b, min_bet or oldbet.
	//If this is the first time bet command is typed than alreadyBet is set to true and bet() method from Player class is called.
	//If this method returns false because player has not enough credits the action fails, if the method returns true, alreadyBet is
	//set to true
	public void makeBet(double b, double min_bet){
		if(alreadyBet) {
			if(!player.reBet(b,min_bet)){
				System.out.println("b: Illegal command (Not enough credits)");
				return;
			}
			System.out.println("player is betting "+player.getBet());
			return;
		}
		if(!player.bet(b,min_bet)){
			System.out.println("b: Illegal command (Not enough credits)");
			return;
		}
		alreadyBet=true;
		System.out.println("player is betting "+player.getBet());
	}
	
	//In method makeBet, the methods reBet() and bet() from Player class are called and if they don't return false, a value is different than 0
	//is added to the bet list, in the index corresponding to the current hand of the player. This bet value is checked now when Player.deal()
	//is called to determine if a bet was already made to perform the next actions. If the previous check succeeds, dealer draws the first four
	//cards to the game (two for him and two for player).
	public void deal(double min_bet){
		handDealer.emptyCards();
		handDealer = new Junk();
		handPlayer.emptyCards();
		handPlayer = new Junk();
		handPlayer2.emptyCards();
		handPlayer2 = new Junk();
		handPlayer3.emptyCards();
		handPlayer3 = new Junk();
		handPlayer4.emptyCards();
		handPlayer4 = new Junk();
		if(!player.deal()) {
			System.out.println("d: Illegal command");
			return;
		}
		dealer.drawFirstFour(player);
		statistics.addPlays();
		dealer.printDealersFirstTwo();
		System.out.println(player);
		str.countCard(player.getHand().getCards().getFirst());
		str.countCard(player.getHand().getCards().getLast());
		str.countCard(dealer.getDealerHand().getCards().getFirst());
		ingame = true;
		handDealer.addCards(dealer.getDealerHand().getCards());
		handPlayer.addCards(player.hands.get(0).getCards());
	}
	
	//
	public void split(){
		handPlayer.emptyCards();
		handPlayer = new Junk();
		handPlayer2.emptyCards();
		handPlayer2 = new Junk();
		handPlayer3.emptyCards();
		handPlayer3 = new Junk();
		handPlayer4.emptyCards();
		handPlayer4 = new Junk();
		if(count_splits==3){
			System.out.println("s: Illegal command (can't split more)");
			stand();
			return;
		}
		if(player.getBalance()<player.getBet()){
			System.out.println("s: Illegal command (Not enough credits)");
			//stand();
			return;
		}
		player.split(dealer.dealCard(),dealer.dealCard());
		str.countCard(player.hands.get(player.getCurrentHand()).getCards().get(1));
		str.countCard(player.hands.get(player.getCurrentHand()+1).getCards().get(1));
		player.printStart("p");
		statistics.addPlays();
		++count_splits;
		wasASplit=true;
		firstplay = true;
		if(player.getNumHands()==1)
			handPlayer.addCards(player.hands.get(0).getCards());
		if(player.getNumHands()==2){
			handPlayer.addCards(player.hands.get(0).getCards());
			handPlayer2.addCards(player.hands.get(1).getCards());
		}
		if(player.getNumHands()==3){
			handPlayer.addCards(player.hands.get(0).getCards());
			handPlayer2.addCards(player.hands.get(1).getCards());
			handPlayer3.addCards(player.hands.get(2).getCards());
		}
		if(player.getNumHands()==4){
			handPlayer.addCards(player.hands.get(0).getCards());
			handPlayer2.addCards(player.hands.get(1).getCards());
			handPlayer3.addCards(player.hands.get(2).getCards());
			handPlayer4.addCards(player.hands.get(3).getCards());
		}
	}
	
	public void insurance(){
		//if it was already a split, insurance can't be made
		if(wasASplit){
			System.out.println("i: Illegal command (Can't make insurance after spliting)");
			return;
		}
		System.out.println("player makes insurance");
		if(player.setInsuranceBet()){
			insuranceMode=true;
		}else{
			System.out.println("i: Illegal command (Not enough credits)");
		}
	}
	
	public void hit(){
		handDealer.emptyCards();
		handDealer = new Junk();
		handPlayer.emptyCards();
		handPlayer = new Junk();
		handPlayer2.emptyCards();
		handPlayer2 = new Junk();
		handPlayer3.emptyCards();
		handPlayer3 = new Junk();
		handPlayer4.emptyCards();
		handPlayer4 = new Junk();
		firstplay = false;
		player.hit(dealer.dealCard());
		str.countCard(player.getHand().getCards().getLast());
		player.printStart("h");
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
		handDealer.addCards(dealer.getDealerHand().getCards());
		if(player.getNumHands()==1)
			handPlayer.addCards(player.hands.get(0).getCards());
		if(player.getNumHands()==2){
			handPlayer.addCards(player.hands.get(0).getCards());
			handPlayer2.addCards(player.hands.get(1).getCards());
		}
		if(player.getNumHands()==3){
			handPlayer.addCards(player.hands.get(0).getCards());
			handPlayer2.addCards(player.hands.get(1).getCards());
			handPlayer3.addCards(player.hands.get(2).getCards());
		}
		if(player.getNumHands()==4){
			handPlayer.addCards(player.hands.get(0).getCards());
			handPlayer2.addCards(player.hands.get(1).getCards());
			handPlayer3.addCards(player.hands.get(2).getCards());
			handPlayer4.addCards(player.hands.get(3).getCards());
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
		handDealer.addCards(dealer.getDealerHand().getCards());
		if(player.getNumHands()==1)
			handPlayer.addCards(player.hands.get(0).getCards());
		if(player.getNumHands()==2){
			handPlayer.addCards(player.hands.get(0).getCards());
			handPlayer2.addCards(player.hands.get(1).getCards());
		}
		if(player.getNumHands()==3){
			handPlayer.addCards(player.hands.get(0).getCards());
			handPlayer2.addCards(player.hands.get(1).getCards());
			handPlayer3.addCards(player.hands.get(2).getCards());
		}
		if(player.getNumHands()==4){
			handPlayer.addCards(player.hands.get(0).getCards());
			handPlayer2.addCards(player.hands.get(1).getCards());
			handPlayer3.addCards(player.hands.get(2).getCards());
			handPlayer4.addCards(player.hands.get(3).getCards());
		}
	}

	
	//This method hits dealer's hand until it reaches 17 or more. Hits are only made if player has some hand that didn't bust, otherwise
	//dealer stands.
	public boolean dealerFinalizeCards(){
		if(player.allHandsBusted()){
			System.out.println("dealer stands");
			return true;
		}
		while(dealer.getDealerHand().getTotal()<17){
			dealer.drawCardToDealer();
			str.countCard(dealer.getDealerHand().getCards().getLast());
			System.out.println("dealer hits");
			System.out.println(dealer);
			if(dealer.getDealerHand().getTotal()>21){
				/* When the dealer busts */
				System.out.println("dealer busts");
				int i = 0;
				player.setCurrentHand(i);
				while(player.getCurrentHand()<player.getNumHands()){
					if(player.getBet()==0){
						player.printWLP("loses");
						statistics.addLoses();
					}else{
						player.setBalance(player.getBet()*2);
						player.printWLP("wins");
						statistics.addWins();
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
	//This finalizes the game. First it checks if the player has a blackjack (this verification is only made if there was no splits),
	//than it's verified if the dealer has a blackjack. If the dealer has a blackjack it's a push, and if it was made insurance, the player
	//also wins insurance, if the dealer has not a blackjack it's a win and any insurance made is lost.
	//If the player doesn't have a blackjack but the dealer has, player loses and he can only win insurance if it was made, if dealer
	//also doesn't have a blackjack than any insurance made is lost.
	//When the player and the dealer don't have blackjack dealerFinalizeCards is called
	public void finalizeDealer(){
		firstplay = true;
		System.out.println(dealer);
		str.countCard(dealer.getDealerHand().getCards().get(1));
		/* Check if the player has blackjack */
		if(player.getHand().blackjack() && !wasASplit){
			statistics.addPlayerbj();
			System.out.println("Player got a BlackJack!");
			if(dealer.getDealerHand().blackjack()){
				System.out.println("Dealer got a BlackJack!");
				if(insuranceMode){
					player.setBalance(2*player.getInsuranceBet());
					System.out.println("Player wins insurance");
					insuranceMode=false;
				}
				player.setBalance(player.getBet());
				System.out.println("player pushes and his current balance is "+player.getBalance());
				statistics.addDealerbj();
				statistics.addPushes();
				cleanTable();
				return;
			}else{
				if(insuranceMode){
					System.out.println("Player loses insurance");
					insuranceMode=false;
				}
				player.setBalance((player.getBet())*2.5);
				System.out.println("Player wins and his current balance is "+player.getBalance());
				statistics.addWins();
				cleanTable();
				return;
			}
		}
		/* Checks if the dealer has blackjack */
		if(dealer.getDealerHand().blackjack()){
			statistics.addDealerbj();
			if(insuranceMode){
				System.out.println("Dealers BlackJack!");
				player.setBalance(2*player.getInsuranceBet());
				System.out.println("Player wins insurance and his current balance is "+player.getBalance());
				statistics.addLoses();
				cleanTable();
				insuranceMode=false;
				return;
			}else{
				System.out.println("Dealers BlackJack!");
				System.out.println("Player loses and his current balance is "+player.getBalance());
				statistics.addLoses();
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
				statistics.addLoses();
				player.setCurrentHand(++n);
				continue;
			}
			if(player.getHand().getTotal()>dealer.getDealerHand().getTotal()){
				player.setBalance((player.getBet())*2);
				statistics.addWins();
				player.printWLP("wins");
			}else if(player.getHand().getTotal()==dealer.getDealerHand().getTotal()){
				//If he has a 21 hand value of two cards, it wins an ordinary 21 made of more than 2 cards
				if(player.getHand().countCards()==2 && player.getHand().getTotal()==21){
					player.setBalance(2*player.getBet());
					statistics.addWins();
					player.printWLP("wins");
				}else{
					player.setBalance(player.getBet());
					statistics.addPushes();
					player.printWLP("pushes");
				}
			}
			else{
				statistics.addLoses();
				player.printWLP("loses");
			}
			
			player.setCurrentHand(++n);
		}
		cleanTable();
	}
	
	public void doubleDown(){
		if(player.getBalance()<player.getBet()){
			System.out.println("2: Illegal command (Not enough credits)");
			//stand();
			return;
		}
		if(!player.getHand().canDouble()){
			System.out.println("2: Illegal command (Can't double this hand)");
			return;
		}
		player.doubleDown(dealer.dealCard());
		firstplay = false;
		str.countCard(player.getHand().getCards().getLast());
		player.printStart("2");
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
			firstplay = true;
		}	
		stand();
	}
	
	public void cleanTable(){
		handDealer.emptyCards();
		handDealer = new Junk();
		handPlayer.emptyCards();
		handPlayer = new Junk();
		handPlayer2.emptyCards();
		handPlayer2 = new Junk();
		handPlayer3.emptyCards();
		handPlayer3 = new Junk();
		handPlayer4.emptyCards();
		handPlayer4 = new Junk();
		handDealer.addCards(dealer.getDealerHand().getCards());
		if(player.getNumHands()==1)
			handPlayer.addCards(player.hands.get(0).getCards());
		if(player.getNumHands()==2){
			handPlayer.addCards(player.hands.get(0).getCards());
			handPlayer2.addCards(player.hands.get(1).getCards());
		}
		if(player.getNumHands()==3){
			handPlayer.addCards(player.hands.get(0).getCards());
			handPlayer2.addCards(player.hands.get(1).getCards());
			handPlayer3.addCards(player.hands.get(2).getCards());
		}
		if(player.getNumHands()==4){
			handPlayer.addCards(player.hands.get(0).getCards());
			handPlayer2.addCards(player.hands.get(1).getCards());
			handPlayer3.addCards(player.hands.get(2).getCards());
			handPlayer4.addCards(player.hands.get(3).getCards());
		}
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
	}
	
	public void surrender(){
		if(wasASplit||!firstplay()){
			System.out.println("u: Illegal command (can't surrend");
			stand();
			return;
		}
		System.out.println("player is surrendering");
		System.out.println(dealer);
		player.setBalance(0.5*player.getBet());
		System.out.println("player current balance is "+player.getBalance());
		statistics.addLoses();
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