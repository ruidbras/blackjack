package blackjack;

public class Game {
	private Player player;
	private Dealer dealer;
	private Strategy strategy;
	private boolean ingame;
	private boolean wasASplit;
	
	
	Game(Player p, Dealer d, Strategy s){
		player = p;
		dealer = d;
		strategy = s;
		wasASplit=false;
	}
	
	public boolean makeBet(int b){
		return player.bet(b);
		
	}
	
	public void deal(){
		if(player.getBet()==0)
			if(!(player.bet(player.oldbet))){
				return;
			}
		strategy.addPlays();
		player.getHand().drawCard();
		player.getHand().drawCard();
		dealer.drawHand();
		dealer.printDealersFirstTwo();
		System.out.println(player);
		ingame = true;
		if(player.getHand().blackjack()){
			System.out.println("BlackJack!");
			finalizeDealer();
		}
	}
	
	public void split(Deck d, Junk j){
		wasASplit=true;
		System.out.println("Player makes split");
		player.newHand(d, j, player.getCurrentHand());
		System.out.println(player);
	}
	
	
	public void insurance(){
		player.bet(player.bet);//ver as bets
		System.out.println("Dealers Hand: "+dealer.hand);
		if(dealer.hand.blackjack()){
			player.setBalance(2*player.bet);
			System.out.println("Player wins insurance");
			cleanTable();
		}
	}
	
	public void hit(){
		System.out.println("Player hits");
		player.hit();
		dealer.printDealersFirstTwo();
		System.out.println(player);
		if(player.getHand().getTotal()>21 && player.getCurrentHand()>0){
			System.out.println(player.getCurrentHand() + "th hand busts");
			player.getHand().cleanHand();
			player.hands.remove(player.getCurrentHand());
			player.setNumbHands();
			player.setCurrentHand(player.getCurrentHand()-1);
		}else{
			if(player.getHand().getTotal()>21 && player.getNumHands()==1){
				System.out.println("Player busts");
				System.out.println("Dealers Hand: "+dealer.hand);
				cleanTable();
			}if(player.getHand().getTotal()>21 && player.getNumHands()>1){
				System.out.println(player.getCurrentHand() + "th hand busts");
				player.getHand().cleanHand();
				player.hands.remove(player.getCurrentHand());
				player.setNumbHands();
				finalizeDealer();
			}
		}
	}
	
	public void stand(){
		if(player.getCurrentHand()>0){
			System.out.println("Player stands in "+player.getCurrentHand()+ " hand");
			player.setCurrentHand(player.getCurrentHand()-1);
			dealer.printDealersFirstTwo();
			System.out.println(player);
		}
		else{
			System.out.println("Player stands");
			finalizeDealer();
		}
		
	}
	
	public boolean dealerFinalizeCards(){
		while(dealer.hand.getTotal()<17){
			dealer.hand.drawCard();
			System.out.println("Dealer hits");
			System.out.println("Dealers Hand: "+dealer.hand);
			if(dealer.hand.getTotal()>21){
				/* When the dealer busts */
				System.out.println("Dealer busts");
				player.setBalance((player.bet)*2);
				System.out.println("Player wins and his current balance is "+player.getBalance());
				cleanTable();
				return false;
			}
		}
		return true;
	}
	
	
	public void finalizeDealer(){
		
		System.out.println("Dealers Hand: "+dealer.hand);
		
		/* Check if the player has blackjack */
		if(player.getHand().blackjack() && !wasASplit){
			if(dealer.hand.blackjack()){
				System.out.println("BlackJack!");
				player.setBalance(player.bet);
				System.out.println("Player pushes and his current balance is "+player.getBalance());
				strategy.addDealerbj();
				strategy.addPlayerbj();
				strategy.addPushes();
				cleanTable();
				return;
			}else{
				player.setBalance((player.bet)*2);//Should pay 2.5
				System.out.println("Player wins and his current balance is "+player.getBalance());
				strategy.addDealerbj();
				strategy.addWins();
				cleanTable();
				return;
			}
		}
		/* Checks if the dealer has blackjack */
		if(dealer.hand.blackjack()){
			System.out.println("BlackJack!");
			System.out.println("Player loses and his current balance is "+player.getBalance());
			strategy.addLoses();
			strategy.addDealerbj();
			cleanTable();
			return;
		}
		
		int n = player.getNumHands()-1;
		player.setCurrentHand(n);
		boolean alreadyFinalized=false;
		
		while((player.getCurrentHand())>=0){
			
			if(player.getHand().getTotal()>=dealer.hand.getTotal()){
				if (dealerFinalizeCards()&&!alreadyFinalized){
					alreadyFinalized=true;
				}
				else{
					ingame=false;
					cleanTable();
					return;
				}
			}
			else{
				System.out.println("Hand "+player.getCurrentHand()+" loses");
				n=n-1;
				player.setCurrentHand(n);
				continue;
			}
			if (alreadyFinalized){
				/* If no one busts the game checks who is the winner */
				if(player.getHand().getTotal()>dealer.hand.getTotal()){
					player.setBalance((player.bet)*2);
					strategy.addWins();
					System.out.println("Player wins and his current balance is "+player.getBalance());
				}else if(player.getHand().getTotal()==dealer.hand.getTotal()){
					player.setBalance(player.bet);
					strategy.addPushes();
					System.out.println("Player pushes and his current balance is "+player.getBalance());
				}
				else{
					strategy.addLoses();
					System.out.println("Player loses and his current balance is "+player.getBalance());
				}
			}
			n=n-1;
			player.setCurrentHand(n);
		}
		wasASplit=false;
		alreadyFinalized=false;
		ingame=false;
		cleanTable();
	}
	
		
	public boolean ingame(){
		return ingame;
	}
	
	public void dowbleDown(){
		System.out.println("Player dowbles down");
		player.doubleDown(player.getCurrentHand());//preciso ver as bets, porque nao percebi bem aquilo da oldbet
		System.out.println(player);
		
		/*if(inHandTwo){
			inHandTwo=false;
			System.out.println("Player doubles in first hand");
			dealer.printDealersFirstTwo();
			System.out.println(player);
		}*/
		
		if(player.getHand().getTotal()>21){
			System.out.println("Player busts");
			finalizeDealer();
		}
	}
	public void cleanTable(){
		dealer.cleanDealerHand();
		player.cleanPlayerHand();
		wasASplit=false;
		ingame = false;
	}
	

}
