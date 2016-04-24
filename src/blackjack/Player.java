package blackjack;

public class Player {
	
	/* Field */
	Hand[] hand= new Hand[2];
	private double balance;
	public int bet;
	public int oldbet;
	
	/* Constructor */
	Player(Deck d, Junk j, int i){
		hand[0] = new Hand(d,j);
		hand[1]= null;
		balance = i; 
	}
	
	
	/* Get's */
	public Hand getHand() {
		return hand[0];
	}

	public double getBalance() {
		return balance;
	}
	
	public int getBet() {
		return bet;
	}
	
	/* Methods */
	public void hit(){
		hand[0].drawCard();
	}
	
	public void newHand(Deck d, Junk j){
		try{
			hand[1]=new Hand(d,j);
			bet(bet);//preciso ver melhor
			hand[1].hand.add(hand[0].hand.get(1));
			hand[0].hand.remove(1);
			hand[0].drawCard();
			hand[1].drawCard();
		}catch(Exception name){}
	}

	public void doubleDown(){
		if(balance>=bet){
			balance-=bet;
			bet = 2*bet;
			hand[0].drawCard();
		}else{
			System.out.println("[!]Not enough credits to make that bet");
		}
	}
	
	public boolean bet(int b){
		int temp;
		if(bet!=0){
			temp = bet;
			balance += bet;
			bet = 0;
			if(balance>=b){
				balance-=b;
				bet = b;
				oldbet = b;
				return true;
			}else{
				System.out.println("[!]Not enough credits to make that bet");
				bet = temp;
				return false;
			}
		}
		if(balance>=b){
			balance-=b;
			bet = b;
			oldbet = b;
			return true;
		}else{
			System.out.println("[!]Not enough credits to make that bet");
		}
		return false;
	}
	
	public void addCredit(int i){
		balance += i;
	}
	

	public void setBalance(double d) {
		this.balance += d;
	}
	
	public void cleanPlayerHand(){
		hand[0].cleanHand();
		
		try{
			hand[1].cleanHand();
			hand[1]=null;
		}catch(Exception name){}
		
		bet = 0;
	}


	@Override
	public String toString() {
		if (hand[1]!=null){
			return "Player Hands: "+ hand[0]+"    "+hand[1];
		}
		return "Player Hand: " + hand[0];
	}

}
