package blackjack;

import java.util.LinkedList;

public class Player {
	
	/* Field */
	LinkedList<Hand> hands;
	private PileOfChips pile;
	public LinkedList<Double> bet;//bets vector
	public int numbHands; //number of hands in the list
	public int currentHand;//current hand in play
	public double insuranceBet;
	public double oldbet;
	
	/* Constructor */
	Player(double i){
		hands= new LinkedList<Hand>();
		bet = new LinkedList<Double>();
		bet.add((double)0);
		hands.add(new Hand());
		pile = new PileOfChips(i);
		numbHands=1;
		currentHand=0;
		insuranceBet=0;
		oldbet=0;
	}
	
	/* Get's */
	
	public double getOldbet(){
		return oldbet;
	}
	
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
		return pile.getBalance();
	}
	
	public double getBet() {
		return bet.get(getCurrentHand());
	}
	
	public void setBalance(double d) {
		pile.updatePile(d);
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
		if(getNumHands()==1){
			bet.set(0, (double)0);
			return;
		}
		bet.remove(getCurrentHand());
	}

	public void setCurrentHand(int n){
		currentHand=n;
	}
	
	public void addCredit(double i){ //nunca usada ainda
		pile.updatePile(i);
	}
	

	public void hit(Card c){
		getHand().addCard(c);
		
	}
	
	public boolean deal(double min_bet){
		//if player hits deal without specify the bet value
		if(getBet()==0){
		/* If it is the first deal of the game and the player didn't specify the value of the bet
		* it's given the min_bet value to the bet, otherwise it's given the value of the previous bet
		*/
			if(oldbet==0){
				oldbet = min_bet;
				if(!bet(min_bet))return false;
			}else if(!bet(oldbet)) return false;
		}
		return true;
	}
	
	public boolean doubleDown(Card c){
		double b=bet.get(getCurrentHand());
		if(pile.getBalance()>=b){
			setBalance(-b);
			bet.set(getCurrentHand(), 2*b);
			hands.get(getCurrentHand()).addCard(c);
			return true;
		}
		System.out.println("[!]Nao tem creditos suficientes para efectuar o double");
		return false;
	}
	
	public boolean bet(double b){
		if(pile.getBalance()>=b){
			setBalance(-b);
			bet.set(getCurrentHand(), b);
			oldbet=b;
			return true;
		}else{
			System.out.println("[!]Nao tem creditos suficientes para efectuar o bet");
		}
		return false;
	}
	
	public void split(Card a, Card b){
		if(pile.getBalance()<getBet()){
			return;
		}
		int index=getCurrentHand();
		hands.add(index+1, new Hand());
		setNumbHands();
		if (hands.get(index).twoAces()){
			hands.get(index).setHandCanBeHit(false);
			hands.get(index+1).setHandCanBeHit(false);
		}
		hands.get(index+1).cards.add(hands.get(index).cards.get(1));
		bet.add(index+1, getBet());
		hands.get(index).cards.remove(1);
		hands.get(index+1).addCard(a);
		hands.get(index).addCard(b);
		setCurrentHand(index+1);
		bet(getBet());
		System.out.println("player makes split");
		return;
	}
	
	/*After a split runs all hands to check if there are hands of two aces, than the boolean result says if hand can or can not
	be hit or doubled*/
		public boolean runHands(){
			//se não for false é porque não foi um split de AAs
			if(getHand().getHandCanBeHit()){
				return true;
			}
			while(getCurrentHand()>=0){
				if(getHand().twoAces()){
					setCurrentHand(getCurrentHand());
					return false;
				}
				setCurrentHand(getCurrentHand()-1);
			}
			setCurrentHand(0);
			return false;
		}

	@Override
	public String toString() {
		if (numbHands!=1){
			return "Player Hands: "+ hands;
		}
		return "Player Hand: " + hands;
	}

}
