package blackjack;

public class Player {
	
	/* Field */
	Hand hand;
	private double balance;
	public int bet;
	
	/* Constructor */
	Player(Deck d, Junk j, int i){
		hand = new Hand(d,j);
		balance = i; 
	}
	
	
	/* Get's */
	public Hand getHand() {
		return hand;
	}

	public double getBalance() {
		return balance;
	}
	
	public int getBet() {
		return bet;
	}
	
	/* Methods */
	public void hit(){
		hand.drawCard();
	}
	

	public void doubleDown(){
		if(balance>=2*bet){
			balance-=2*bet;
			bet = 2*bet;
			hand.drawCard();
		}else{
			System.out.println("[!]Não tem créditos suficientes para efectuar a aposta");
		}
	}
	
	public boolean bet(int b){
		if(balance>=b){
			balance-=b;
			bet = b;
			hand.drawCard();
			hand.drawCard();
			return true;
		}else{
			System.out.println("[!]Não tem créditos suficientes para efectuar a aposta");
		}
		return false;
	}
	
	public void addCredit(int i){
		balance += i;
	}
	

	public void setBalance(double d) {
		this.balance += d;
	}


	@Override
	public String toString() {
		return "Player: " + hand;
	}

}
