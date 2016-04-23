package blackjack;

public class Game {
	private Player player;
	private Dealer dealer;
	private boolean ingame;
	
	Game(Player p, Dealer d){
		player = p;
		dealer = d;
	}
	
	public boolean makeBet(int b){
		return player.bet(b);
		
	}
	
	public void deal(){
		if(player.getBet()==0)
			if(!(player.bet(player.oldbet))){
				return;
			}
		
		player.hand.drawCard();
		player.hand.drawCard();
		dealer.drawHand();
		dealer.printDealersFirstTwo();
		System.out.println("Your hand: "+player.hand);
		ingame = true;
		if(player.hand.blackjack()){
			System.out.println("BlackJack!");
			finalizeDealer();
		}
	}
	
	public void hit(){
		System.out.println("Player hits");
		player.hand.drawCard();
		System.out.println("Your hand: "+player.hand);
		if(player.hand.getTotal()>21){
			System.out.println("Player busts");
			finalizeDealer();
		}
	}
	
	public void stand(){
		System.out.println("Player stands");
		finalizeDealer();
	}
	
	public void finalizeDealer(){
		System.out.println("Dealers Hand: "+dealer.hand);
		/* Check if the player has blackjack */
		if(player.hand.blackjack()){
			if(dealer.hand.blackjack()){
				System.out.println("BlackJack!");
				player.setBalance(player.bet);
				System.out.println("Player pushes and his current balance is "+player.getBalance());
				cleanTable();
				return;
			}else{
				player.setBalance((player.bet)*2);//Should pay 2.5
				System.out.println("Player wins and his current balance is "+player.getBalance());
				cleanTable();
				return;
			}
		}
		/* Checks if the dealer has blackjack */
		if(dealer.hand.blackjack()){
			System.out.println("BlackJack!");
			System.out.println("Player loses and his current balance is "+player.getBalance());
			cleanTable();
			return;
		}
		/* Checks if the player have busted */
		if((player.hand.getTotal())<=21){
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
					return;
				}
			}
		}else{
			/* When the player busts */
			System.out.println("Player loses and his current balance is "+player.getBalance());
			cleanTable();
			return;
		}
		
		/* If no one busts the game checks who is the winner */
		if(player.hand.getTotal()>dealer.hand.getTotal()){
			player.setBalance((player.bet)*2);
			System.out.println("Player wins and his current balance is "+player.getBalance());
			cleanTable();
		}else if(player.hand.getTotal()==dealer.hand.getTotal()){
			player.setBalance(player.bet);
			System.out.println("Player pushes and his current balance is "+player.getBalance());
			cleanTable();
		}
		else{
			System.out.println("Player loses and his current balance is "+player.getBalance());
			cleanTable();
		}
		cleanTable();
	}
	
	public boolean ingame(){
		return ingame;
	}
	
	public void cleanTable(){
		dealer.hand.cleanHand();
		player.cleanPlayerHand();
		ingame = false;
	}

}
