package blackjack;

import java.util.LinkedList;

public class Player {
	
	/* Field */
	LinkedList<Hand> hands= new LinkedList<Hand>();
	private double balance;
	public int bet;//meter vector de bets
	public int oldbet;
	public int numbHands; //number of hands in the list
	public int currentHand;//current hand in play
	
	/* Constructor */
	Player(Deck d, Junk j, int i){
		hands.add(new Hand(d,j));
		balance = i; 
		numbHands=hands.size();
		currentHand=0;
	}
	
	/* Get's */
	public int getNumHands(){
		return numbHands;
	}
	
	public int getCurrentHand(){
		return currentHand;
	}
	
	public Hand getHand() {
		return hands.get(getCurrentHand());
	}

	public double getBalance() {
		return balance;
	}
	
	public int getBet() {
		return bet;
	}
	
	public void setBalance(double d) {
		this.balance += d;
	}
	
	public void setNumbHands(){
		numbHands=hands.size();
	}
	
	public void setCurrentHand(int n){
		currentHand=n;
	}
	
	/* Methods */
	public void hit(){
		getHand().drawCard();
	}
	
	public void newHand(Deck d, Junk j, int index){
		try{
			hands.add(index+1, new Hand(d,j));
			setNumbHands();
			bet(bet);//preciso ver melhor
			hands.get(index+1).hand.add(hands.get(index).hand.get(1));
			hands.get(index).hand.remove(1);
			hands.get(index+1).drawCard();
			hands.get(index).drawCard();
			setCurrentHand(index+1);
		}catch(Exception name){}
	}

	public void doubleDown(int index){
		if(balance>=bet){
			balance-=bet;
			bet = 2*bet;
			hands.get(index).drawCard();
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
		
	public void cleanPlayerHand(){
		
		for(int i=1; i<numbHands; i++){
			hands.get(i).cleanHand();
			hands.remove(i);
			bet = 0;
		}
		hands.get(0).cleanHand();
		bet=0;
		setNumbHands();
		setCurrentHand(0);
	}


	@Override
	public String toString() {
		if (numbHands!=1){
			return "Player Hands: "+ hands;
		}
		return "Player Hand: " + hands;
	}

}
