package blackjack;

public class Player {
	
	/* Field */
	Hand hand;
	private int balance;
	private int bet;
	
	/* Constructor */
	Player(Deck d, Junk j, int i){
		hand = new Hand(d,j);
		balance = i; 
	}
	
	
	/* Get's */
	public Hand getHand() {
		return hand;
	}

	public int getBalance() {
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
		
	}
	
	public void bet(int b){
		if(balance>=b){
			balance-=b;
			bet = b;
			hand.drawCard();
			hand.drawCard();
		}else{
			System.out.println("[!]Não tem créditos suficientes para efectuar a aposta");
		}
	}
	
	public void addCredit(int i){
		balance += i;
	}


	@Override
	public String toString() {
		return "Player: " + hand;
	}



}
