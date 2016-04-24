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
		if(balance>=2*bet){
			balance-=2*bet;
			bet = 2*bet;
			hand[0].drawCard();
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
<<<<<<< HEAD
		}
=======
		}		
>>>>>>> 6250346dd7628e9b2458027e8c9c787d819e06d7
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
<<<<<<< HEAD
		hand[0].cleanHand();
		
		try{
			hand[1].cleanHand();
			hand[1]=null;
		}catch(Exception name){}
		
=======
		hand.cleanHand();
>>>>>>> 6250346dd7628e9b2458027e8c9c787d819e06d7
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
