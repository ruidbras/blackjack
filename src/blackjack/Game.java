package blackjack;

public class Game {
	private Player player;
	private Dealer dealer;
	private Strategy strategy;
	private boolean ingame;
	public boolean inHandTwo;
	
	
	
	Game(Player p, Dealer d, Strategy s){
		player = p;
		dealer = d;
		strategy = s;
		inHandTwo=false;
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
		player.hand[0].drawCard();
		player.hand[0].drawCard();
		dealer.drawHand();
		dealer.printDealersFirstTwo();
		System.out.println(player);
		ingame = true;
		if(player.hand[0].blackjack()){
			System.out.println("BlackJack!");
			finalizeDealer();
		}
	}
	
	public void split(Deck d, Junk j){
		System.out.println("Player makes split");
		player.newHand(d, j);
		System.out.println(player);
		inHandTwo=true;
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
		if(inHandTwo){
			player.hand[1].drawCard();
			dealer.printDealersFirstTwo();
			System.out.println(player);
			if(player.hand[1].getTotal()>21){
				System.out.println("first hand busts");
				player.hand[1].cleanHand();
				player.hand[1].equals(null);
				inHandTwo=false;
			}
		}else{
			player.hand[0].drawCard();
			System.out.println(player);
			if(player.hand[0].getTotal()>21){
				System.out.println("Player busts");
				finalizeDealer();
			}
		}
	}
	
	public void stand(){
		if(inHandTwo){
			inHandTwo=false;
			System.out.println("Player stands in first hand");
			dealer.printDealersFirstTwo();
			System.out.println(player);
		}
		else{
			System.out.println("Player stands");
			finalizeDealer();
		}
		
	}
	
	public void finalizeDealer(){
		System.out.println("Dealers Hand: "+dealer.hand);
		
		int n = 0;
		boolean splitMode=false;
		//choose best hand
		try{
		if(!player.hand[1].equals(null)){
			splitMode=true;
			if(player.hand[1].getTotal()>=player.hand[0].getTotal()){
				n=1;
				if(player.hand[1].getTotal()>21){
					n=0;
				}
			}
		}
		}catch(Exception name){}
		
		
		/* Check if the player has blackjack */
		//atencao, depois de um split nao ha blackjacks!!!
		if(player.hand[0].blackjack() && !splitMode){
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
				strategy.addPlayerbj();
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
		/* Checks if the player have busted */
		if((player.hand[n].getTotal())<=21){
			while(dealer.hand.getTotal()<17){
				dealer.hand.drawCard();
				System.out.println("Dealer hits");
				System.out.println("Dealers Hand: "+dealer.hand);
				if(dealer.hand.getTotal()>21){
					/* When the dealer busts */
					System.out.println("Dealer busts");
					player.setBalance((player.bet)*2);
					System.out.println("Player wins and his current balance is "+player.getBalance());
					strategy.addWins();
					cleanTable();
					return;
				}
			}
		}else{
			/* When the player busts */
			System.out.println("Player loses and his current balance is "+player.getBalance());
			strategy.addLoses();
			cleanTable();
			return;
		}
		
		/* If no one busts the game checks who is the winner */
		if(player.hand[n].getTotal()>dealer.hand.getTotal()){
			player.setBalance((player.bet)*2);
			System.out.println("Player wins and his current balance is "+player.getBalance());
			strategy.addWins();
			cleanTable();
		}else if(player.hand[n].getTotal()==dealer.hand.getTotal()){
			player.setBalance(player.bet);
			System.out.println("Player pushes and his current balance is "+player.getBalance());
			strategy.addPushes();
			cleanTable();
		}
		else{
			System.out.println("Player loses and his current balance is "+player.getBalance());
			strategy.addLoses();
			cleanTable();
		}
		cleanTable();
	}
	
	public boolean ingame(){
		return ingame;
	}
	
	public void dowbleDown(){
		System.out.println("Player dowbles down");
		player.doubleDown();//preciso ver as bets, porque nao percebi bem aquilo da oldbet
		System.out.println(player);
		
		if(inHandTwo){
			inHandTwo=false;
			System.out.println("Player doubles in first hand");
			dealer.printDealersFirstTwo();
			System.out.println(player);
		}
		
		if(player.hand[0].getTotal()>21){
			System.out.println("Player busts");
			finalizeDealer();
		}
	}
	public void cleanTable(){
		dealer.cleanDealerHand();
		player.cleanPlayerHand();
		ingame = false;
	}

}
