package blackjack;

public class Player {
	
	/* Field */
	Hand hand;
	private double balance;
	public int bet;
	public int oldbet;
	
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
				System.out.println("[!]Não tem créditos suficientes para efectuar a aposta");
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
	
	public void cleanPlayerHand(){
		hand.cleanHand();
		bet = 0;
	}


	@Override
	public String toString() {
		return "Player: " + hand;
	}

}
