package blackjack;

public class Game {
	private Player player;
	private Dealer dealer;
	private Strategy strategy;
	private boolean ingame;
	private boolean wasASplit;
	private boolean insuranceMode;
	
	Game(Player p, Dealer d, Strategy s){
		player = p;
		dealer = d;
		strategy = s;
		wasASplit=false;
		ingame=false;
		insuranceMode=false;
	}
	
	
	public boolean makeBet(double b){
		return player.bet(b);
		
	}
	
	public void deal(){
		if(player.getBet()==0)
			if(!(player.bet(player.oldbet))){
				return;
			}
		strategy.addPlays();
		player.hit();
		player.hit();
		dealer.drawHand();
		dealer.printDealersFirstTwo();
		System.out.println(player);
		ingame = true;
	}
	
	public void split(Deck d, Junk j){
		wasASplit=true;
		System.out.println("Player makes split");
		player.newHand(d, j, player.getCurrentHand());
		System.out.println(player);
	}
	
	
	public void insurance(){
		System.out.println("Player makes insurance");
		player.setInsuranceBet();
		System.out.println("Dealers Hand: "+dealer.hand);
		insuranceMode=true;
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
			player.setBetZero();
			player.setNumbHands();
			player.setCurrentHand(player.getCurrentHand()-1);
		}else{
			if(player.getHand().getTotal()>21 && player.getNumHands()==1){
				System.out.println("Player busts");
				System.out.println("Dealers Hand: "+dealer.hand);
				cleanTable();
				return;
			}if(player.getHand().getTotal()>21 && player.getNumHands()>1){
				System.out.println(player.getCurrentHand() + "th hand busts");
				player.getHand().cleanHand();
				player.hands.remove(player.getCurrentHand());
				player.setBetZero();
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
				player.setBalance(player.getBet()*2);
				System.out.println("Player wins");
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
			System.out.println("Player got a BlackJack!");
			if(dealer.hand.blackjack()){
				System.out.println("Dealer got a BlackJack!");
				if(insuranceMode){
					player.setBalance(2*player.getInsuranceBet());
					System.out.println("Player wins insurance");
					cleanTable();
					insuranceMode=false;
					return;
				}
				player.setBalance(player.getBet());
				System.out.println("Player pushes");
				strategy.addDealerbj();
				strategy.addPlayerbj();
				strategy.addPushes();
				cleanTable();
				return;
			}else{
				player.setBalance((player.getBet())*2.5);//Should pay 2.5
				System.out.println("Player wins");
				strategy.addDealerbj();
				strategy.addWins();
				cleanTable();
				return;
			}
		}
		/* Checks if the dealer has blackjack */
		if(dealer.hand.blackjack()){
			System.out.println("BlackJack!");
			if(insuranceMode){
				player.setBalance(2*player.getInsuranceBet());
				System.out.println("Player wins insurance");
				cleanTable();
				insuranceMode=false;
				return;
			}else{
				System.out.println("Player loses");
				strategy.addLoses();
				strategy.addDealerbj();
				cleanTable();
				return;
			}
		}
		
		int n = player.getNumHands()-1;
		player.setCurrentHand(n);
		boolean alreadyFinalized=false;
		
		while((player.getCurrentHand())>=0){
			
			if(player.getHand().getTotal()>=dealer.hand.getTotal()){
				if (!alreadyFinalized){
					if(dealerFinalizeCards()==false){
						wasASplit=false;
						ingame=false;
						return;
					}
					alreadyFinalized=true;
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
					player.setBalance((player.getBet())*2);
					strategy.addWins();
					System.out.println("Hand "+player.getCurrentHand()+" wins");
				}else if(player.getHand().getTotal()==dealer.hand.getTotal()){
					player.setBalance(player.getBet());
					strategy.addPushes();
					System.out.println("Hand "+player.getCurrentHand()+" pushes");
				}
				else{
					strategy.addLoses();
					System.out.println("Hand "+player.getCurrentHand()+ " loses");
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
		player.doubleDown();
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
				return;
			}if(player.getHand().getTotal()>21 && player.getNumHands()>1){
				System.out.println(player.getCurrentHand() + "th hand busts");
				player.getHand().cleanHand();
				player.hands.remove(player.getCurrentHand());
				player.setNumbHands();
				finalizeDealer();
			}
		}
		
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
