package blackjack;

import java.util.LinkedList;

public class Player {
	
	/* Field */
	LinkedList<Hand> hands= new LinkedList<Hand>();
	private double balance;
	public LinkedList<Double> bet = new LinkedList<Double>();//meter vector de bets
	public int numbHands; //number of hands in the list
	public int currentHand;//current hand in play
	public double insuranceBet;
	
	/* Constructor */
	Player(Deck d, Junk j, double i){
		hands.add(new Hand(d,j));
		balance = i; 
		numbHands=hands.size();
		currentHand=0;
		insuranceBet=0;
		bet.add((double)0);
	}
	
	/* Get's */
	
	public double getInsuranceBet(){
		return insuranceBet;
	}
	
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
	
	public double getBet() {
		return bet.get(getCurrentHand());
	}
	
	public void setBalance(double d) {
		this.balance += d;
	}
	
	public void setNumbHands(){
		numbHands=hands.size();
	}
	
	public boolean setInsuranceBet(){
		insuranceBet=getBet();
		if(getBalance()<insuranceBet){
			System.out.println("Not enough money to make this play");
			return false;
		}
		setBalance(-insuranceBet);
		return true;
	}
	
	public void setBetZero(){
		bet.remove(getCurrentHand());
	}
	
	public void setCurrentHand(int n){
		currentHand=n;
	}
	
	/* Methods */
	public void hit(){
		getHand().drawCard();
	}
	
	public void newHand(Deck d, Junk j){
		int index=getCurrentHand();
		hands.add(index+1, new Hand(d,j));
		setNumbHands();
		if (hands.get(index).twoAces()){
			hands.get(index).setHandCanBeHit(false);
			hands.get(index+1).setHandCanBeHit(false);
			System.out.println("entrou");
		}
		hands.get(index+1).hand.add(hands.get(index).hand.get(1));
		bet.add(index+1, getBet());
		hands.get(index).hand.remove(1);
		hands.get(index+1).drawCard();
		hands.get(index).drawCard();
		setCurrentHand(index+1);
		bet(getBet());
		return;
	}

	public boolean doubleDown(){
		double b=bet.get(getCurrentHand());
		if(balance>=b){
			balance-=b;
			bet.set(getCurrentHand(), 2*b);
			hands.get(getCurrentHand()).drawCard();
			return true;
		}
		System.out.println("[!]Não tem créditos suficientes para efectuar o double");
		return false;
	}
	
	public boolean bet(double b){
		if(balance>=b){
			setBalance(-b);
			bet.set(getCurrentHand(), b);
			return true;
		}else{
			System.out.println("[!]Não tem créditos suficientes para efectuar o bet");
		}
		return false;
	}
	
	public void addCredit(int i){
		balance += i;
	}
		
	public void cleanPlayerHands(){
		setNumbHands();
		int i = getNumHands()-1;
		while(i>0){
			hands.get(i).cleanHand();
			hands.remove(i);
			bet.remove(i);
			setNumbHands();
			i=getNumHands()-1;
		}
		hands.get(0).cleanHand();
		bet.set(0, (double) 0);
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
