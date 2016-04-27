package blackjack;

import java.util.LinkedList;

public class Player {
	
	/* Field */
	LinkedList<Hand> hands= new LinkedList<Hand>();
	private double balance;
	public double[] bet;//meter vector de bets
	public double oldbet;
	public int numbHands; //number of hands in the list
	public int currentHand;//current hand in play
	public double insuranceBet;
	
	/* Constructor */
	Player(Deck d, Junk j, int i){
		hands.add(new Hand(d,j));
		balance = i; 
		numbHands=hands.size();
		currentHand=0;
		bet = new double[20];
		insuranceBet=0;
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
		return bet[getCurrentHand()];
	}
	
	public void setBalance(double d) {
		this.balance += d;
	}
	
	public void setNumbHands(){
		numbHands=hands.size();
	}
	
	public void setInsuranceBet(){
		insuranceBet=getBet();
		setBalance(-insuranceBet);
	}
	
	public void setBetZero(){
		bet[getCurrentHand()]=0;
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
			
			hands.get(index+1).hand.add(hands.get(index).hand.get(1));
			hands.get(index).hand.remove(1);
			hands.get(index+1).drawCard();
			hands.get(index).drawCard();
			setCurrentHand(index+1);
			bet(getBet());
		}catch(Exception name){}
	}

	public void doubleDown(){
		if(balance>=getBet()){
			balance-=getBet();
			bet(getBet());
			hands.get(getCurrentHand()).drawCard();
		}else{
			System.out.println("[!]Não tem créditos suficientes para efectuar a aposta");
		}
	}
	
	public boolean bet(double b){
		double temp;
		int n = getCurrentHand();
		if(bet[n]!=0){
			temp = bet[n];
			balance += bet[n];
			bet[n] = 0;
			if(balance>=b){
				balance-=b;
				bet[n] = b;
				oldbet = b;
				return true;
			}else{
				System.out.println("[!]Não tem créditos suficientes para efectuar a aposta");
				bet[n] = temp;
				return false;
			}
		}
		if(balance>=b){
			balance-=b;
			bet[n] = b;
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
		for(int i=1; i<getNumHands(); i++){
			hands.get(i).cleanHand();
			hands.remove(i);
			setNumbHands();
		}
		hands.get(0).cleanHand();
		bet[0]=0;
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
