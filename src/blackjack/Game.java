package blackjack;

/**
 * This class extends the abstract class GameType and implements the methods inherited from the super class, in order to provide information
 * in the terminal about the actions and everything related to the game.
 * @author Pedro Esteves, Ricardo Cristino, Rui Bras
 * @version 1.0
 */
public class Game extends GameType{
	public Junk handDealer= new Junk();
	public Junk handPlayer = new Junk();
	public Junk handPlayer2 = new Junk();
	public Junk handPlayer3 = new Junk();
	public Junk handPlayer4 = new Junk();
	
	/**The constructor associates a Shoe, a Junk, a Player, a Statistics and a Strategy to the game.
	 * 	//wasASplit is initialized as false, ingame as false, insuranceMode as false, firstplay as true,
	 *  alreadyBet as false and count_splits as 0.
	 * @param shoe
	 * @param junk
	 * @param p
	 * @param d
	 * @param s
	 * @param str
	 */
	public Game(Shoe shoe, Junk junk, Player p, Dealer d, Statistics s, Strategy str){
		super(shoe, junk, p, d, s, str);
	}
	
	/**This method receives a value b that is the quantity to bet and a min_bet that is the minimum bet.
	 * If there was already a bet, it's called the reBet() method from Player class. If reBet() returns false than it's because
	 * player doesn't have enough credits to bet b, min_bet or oldbet.
	 * If this is the first time bet command is typed than alreadyBet is set to true and bet() method from Player class is called.
	 * If this method returns false because player has not enough credits the action fails, if the method returns true, alreadyBet is
	 * set to true
	 */
	
	@Override
	public void makeBet(double b, double min_bet){
		if(alreadyBet()) {
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
		setAlreadyBet(true);
		System.out.println("player is betting "+player.getBet());
	}
	
	public void emptyInterfacePlayer(){
		handPlayer.emptyCards();
		handPlayer = new Junk();
		handPlayer2.emptyCards();
		handPlayer2 = new Junk();
		handPlayer3.emptyCards();
		handPlayer3 = new Junk();
		handPlayer4.emptyCards();
		handPlayer4 = new Junk();
	}
	
	public void addInterfacePlayer(){
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
	
	
	//In method makeBet, the methods reBet() and bet() from Player class are called and if they don't return false, a value is different than 0
	//is added to the bet list, in the index corresponding to the current hand of the player. This bet value is checked now when Player.deal()
	//is called to determine if a bet was already made to perform the next actions. If the previous check succeeds, dealer draws the first four
	//cards to the game (two for him and two for player).
	@Override
	public void deal(double min_bet){
		if(ingame()){
			System.out.println("d: Illegal command");
			return;
		}
		handDealer.emptyCards();
		handDealer = new Junk();
		emptyInterfacePlayer();
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
		setInGame(true);
		handDealer.addCards(dealer.getDealerHand().getCards());
		handPlayer.addCards(player.hands.get(0).getCards());
	}
	
	//Split is only allowed when ingame=true, the first hand has two equal cards, the player has a balance larger or equal to the bet of
	//the splitting hand, and there were no more than 3 splits in the game. After this, the cards are splitted in two new hands and dealer delivers
	//one new card for each hand. wasASplit is set to true and firstplay also set to true, because the new current hand has only two cards.
	@Override
	public void split(){
		emptyInterfacePlayer();
		if(!ingame() || !player.getHand().cardsSameValue()){
			System.out.println("s: Illegal command (can't split)");
			return;
		}
		if(count_splits==3){
			System.out.println("s: Illegal command (can't split more)");
			//stand();
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
		setWasASplit(true);
		setFirstplay(true);
		emptyInterfacePlayer();
		addInterfacePlayer();
	}
	
	//Insurance is only allowed to do right after deal and before any other plays, and only if the shown card of the dealer is an ace.
	//If all the conditions are verified, than a insuranceBet with the same value as the current bet is placed (only if player has enough
	//credits to perform this action). The variable insuranceMode is set to true after a successfull insurance.
	@Override
	public void insurance(){
		//if it was already a split, insurance can't be made
		if(wasASplit() || insuranceMode()|| !firstplay()|| !ingame() || !dealer.canHaveBlackjack()){
			System.out.println("i: Illegal command");
			return;
		}
		System.out.println("player makes insurance");
		if(player.setInsuranceBet()){
			setInsuranceMode(true);
		}else{
			System.out.println("i: Illegal command (Not enough credits)");
		}
	}
	
	//Hit is only possible if ingame = true. Player can hit a hand the times he want's but it can't pass a total value of 21.
	//A new card, delivered by the dealer, is added to the current hand of the player, than it's checked if the final hand
	//value is greater than 21. If it is, the bet is removed and set to zero and current hand is set to the next one in the list. If the 
	//current hand was already the last one of the list, the game finishes.
	@Override
	public void hit(){
		if(!ingame()){
			System.out.println("h: Illegal command");
			return;
		}
		handDealer.emptyCards();
		handDealer = new Junk();
		emptyInterfacePlayer();
		setFirstplay(false);
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
			setFirstplay(true);
		}
		handDealer.addCards(dealer.getDealerHand().getCards());
		addInterfacePlayer();
	}
	
	//Standing is only allowed when ingame=true. When player stands current hand is set to the next hand on players list of hands.
	//When current hand is in the end of the list, after standing the game is finalized.
	@Override
	public void stand(){
		if(!ingame()){
			System.out.println("s: Illegal command");
			return;
		}
		setFirstplay(true);
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
		addInterfacePlayer();
	}

	
	//This method hits dealer's hand until it reaches 17 or more. Hits are only made if player has some hand that didn't bust, otherwise
	//dealer stands. If dealer busts, player wins all hands that haven't busted and loses all the others, than the game is finished.
	//Function returns false if dealers busts and true if dealer stands.
	@Override
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
	//When the player and the dealer don't have blackjack dealerFinalizeCards() is called. If the method returns true (dealer finalizes hand without
	//busting) the decisions keep going in a while cycle that runs all the hands of the player. 
	//When a hand has a bet value equal to 0;
	//it's because that hand is busted, and immediately loses;
	//if the value of player's hand is higher than the dealer's hand player wins;
	//If the value of player's hand is equal to the dealer's hand, player can win or push. He wins if his hand has two cards and has a 
	//value of 21 but dealer has a hand with more than two cards. In every other situations player pushes;
	//In every other situations player loses his hand.
	@Override
	public void finalizeDealer(){
		setFirstplay(true);
		System.out.println(dealer);
		str.countCard(dealer.getDealerHand().getCards().get(1));
		/* Check if the player has blackjack */
		if(player.getHand().blackjack() && !wasASplit()){
			statistics.addPlayerbj();
			System.out.println("Player got a BlackJack!");
			if(dealer.getDealerHand().blackjack()){
				System.out.println("Dealer got a BlackJack!");
				if(insuranceMode()){
					player.setBalance(2*player.getInsuranceBet());
					System.out.println("Player wins insurance");
					setInsuranceMode(false);
				}
				player.setBalance(player.getBet());
				System.out.println("player pushes and his current balance is "+player.getBalance());
				statistics.addDealerbj();
				statistics.addPushes();
				cleanTable();
				return;
			}else{
				if(insuranceMode()){
					System.out.println("Player loses insurance");
					setInsuranceMode(false);
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
			if(insuranceMode()){
				System.out.println("Dealers BlackJack!");
				player.setBalance(2*player.getInsuranceBet());
				System.out.println("Player wins insurance and his current balance is "+player.getBalance());
				statistics.addLoses();
				cleanTable();
				setInsuranceMode(false);
				return;
			}else{
				System.out.println("Dealers BlackJack!");
				System.out.println("Player loses and his current balance is "+player.getBalance());
				statistics.addLoses();
				setInsuranceMode(false);
				cleanTable();
				return;
			}
		}else{
			if(insuranceMode()){
				System.out.println("Player loses insurance");
				setInsuranceMode(false);
			}
		}
		
		if(dealerFinalizeCards()==false){
			return;
		}
		int n = 0;
		player.setCurrentHand(n);
		while((player.getCurrentHand())<player.getNumHands()){
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
	
	//doubleDowns are only allowed when the player is in game, there are still two cards in current hand (even in splitting), if the
	//hand value is 9, 10 or 11 and if the player has a balance greater or equal to that hand current bet.
	//If all these conditions are verified than it's set a new bet, doubling the previous one and it's discounted the value in player's
	//balance. A new card is delivered to the hand and hand automatically stands. 
	public void doubleDown(){
		if(!ingame() || !firstplay()||wasASplit()){
			System.out.println("2: Illegal command");
			return;
		}
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
		//firstplay = false;
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
			setFirstplay(true);
		}	
		stand();
	}
	
	//Sets firstplay to true and wasASplit, ingame, and alreadyBet to false. Than dealer cleans his hand and player's hands.
	@Override
	public void cleanTable(){
		handDealer.emptyCards();
		handDealer = new Junk();
		emptyInterfacePlayer();
		handDealer.addCards(dealer.getDealerHand().getCards());
		addInterfacePlayer();
		setFirstplay(true);
		dealer.cleanDealerHand();
		dealer.cleanPlayerHands(player);
		setWasASplit(false);
		setInGame(false);
		setAlreadyBet(false);
		count_splits=0;
	}
	
	//Surrender is only allowed if player didn't make any play already. Surrender is only allowed if it is the first move of the game,
	//before hitting, doubling or splitting. Player receives half of his bet and loses the game.
	@Override
	public void surrender(){
		if(!ingame()||insuranceMode()){
			System.out.println("u: Illegal command");
			return;
		}
		if(wasASplit() || !firstplay()){
			System.out.println("u: Illegal command (can't surrend)");
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
}