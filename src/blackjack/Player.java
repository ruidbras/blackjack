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
		setPile(new PileOfChips(i));
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
		return getPile().getBalance();
	}
	
	public double getBet() {
		return bet.get(getCurrentHand());
	}
	
	public void setBalance(double d) {
		getPile().updatePile(d);
	}
	
	
	//Returns the number of hands in game.
	public void setNumbHands(){
		numbHands=hands.size();
	}
	
	/*When player hits insurance, it's set insuranceBet to the current bet value, than it's verified
	 * if balance is enough to do this play before updating balance.
	 */
	public boolean setInsuranceBet(){
		insuranceBet=getBet();
		if(getBalance()<insuranceBet){
			return false;
		}
		setBalance(-insuranceBet);
		return true;
	}
	
	/*When we have only one hand, this function set's its associated bet to zero, if we
	 * have more than one hand it just removes the bets.
	 */
	public void setBetZero(){
		bet.set(getCurrentHand(),(double)0);
	}
	

	
	//Updates current hand
	public void setCurrentHand(int n){
		currentHand=n;
	}
	
	
	//Delivers a new card to the current hand
	public void hit(Card c){
		getHand().addCard(c);
		
	}
	
	public boolean deal(){
		if(getBet()==0){
			return false;
		}
		oldbet = bet.get(getCurrentHand());
		return true;
	}
	
	public boolean doubleDown(Card c){
		double b=bet.get(getCurrentHand());
		if(getPile().getBalance()>=b){
			setBalance(-b);
			bet.set(getCurrentHand(), 2*b);
			hands.get(getCurrentHand()).addCard(c);
			return true;
		}
		return false;
	}
	
	public boolean reBet(double b, double min_bet){
		if(b==0){
			setBalance(bet.get(getCurrentHand()));
			if(oldbet==0){
				if(getPile().getBalance()<min_bet){
					return false;
				}
				setBalance(-min_bet);
				bet.set(getCurrentHand(), min_bet);
				return true;
			}
			//Otherwise it's given the value of the previous bet
			else{
				if(getPile().getBalance()<oldbet){
					return false; 
				}
				setBalance(-oldbet);
				bet.set(getCurrentHand(), oldbet);
				return true;
			}	
		}
		if((getPile().getBalance()+bet.get(getCurrentHand()))>=b){
			setBalance(bet.get(getCurrentHand()));
			setBalance(-b);
			bet.set(getCurrentHand(), b);
			return true;
		}
		return false;
	}
	
	public boolean bet(double b, double min_bet){
		//if player bets without specifying the bet value
		if(b==0){
			// If it is the first bet of the game and the player didn't specify the value, it's given the min_bet value to the bet
			if(oldbet==0){
				if(getPile().getBalance()<min_bet){
					return false;
				}
				setBalance(-min_bet);
				bet.set(getCurrentHand(), min_bet);
				return true;
			}
			//Otherwise it's given the value of the previous bet
			else{
				if(getPile().getBalance()<oldbet){
					return false; 
				}
				setBalance(-oldbet);
				bet.set(getCurrentHand(), oldbet);
				return true;
			}
		}
		if(getPile().getBalance()<b){
			return false;
		}
		setBalance(-b);
		bet.set(getCurrentHand(), b);
		return true;
	}
	
	
	public void split(Card a, Card b){
		int index=getCurrentHand();
		hands.add(index+1, new Hand());
		setNumbHands();
		if (hands.get(index).twoAces()){
			hands.get(index).setHandCanBeHit(false);
			hands.get(index+1).setHandCanBeHit(false);
		}
		hands.get(index+1).getCards().add(hands.get(index).getCards().get(1));
		bet.add(index+1, getBet());
		bet(getBet(),0);
		hands.get(index).getCards().remove(1);
		hands.get(index+1).addCard(a);
		hands.get(index).addCard(b);
		return;
	}
	

		
	public boolean allHandsBusted(){
		for(Double d: bet){
			if(d!=0){
				return false;
			}
		}
		return true;
	}
	
	public void printPlayingHand(){
		if (numbHands!=1){
			if(getCurrentHand()==0){
				System.out.println("playing "+(getCurrentHand()+1)+"st hand...");
			}
			else if(getCurrentHand()==1){
				System.out.println("playing "+(getCurrentHand()+1)+"nd hand...");
			}else if(getCurrentHand()==2){
				System.out.println("playing "+(getCurrentHand()+1)+"rd hand...");
			}else{
				System.out.println("playing "+(getCurrentHand()+1)+"th hand...");
			}
		}
	}
	
	public void printStart(String action){
		if(action=="s"){
			if (numbHands!=1){
				System.out.println("player stands ["+(getCurrentHand()+1)+"]");
			}else{
				System.out.println("player stands");
			}
		}
		if(action=="h"){
			if (numbHands!=1){
				System.out.println("player hits ["+(getCurrentHand()+1)+"]");
			}else{
				System.out.println("player hits");
			}
			System.out.println(toString());
		}
		if(action=="2"){
			if (numbHands!=1){
				System.out.println("player doubles ["+(getCurrentHand()+1)+"]");
			}else{
				System.out.println("player doubles");
			}
			System.out.println(toString());
		}
		if(action=="p"){
			System.out.println("player is splitting");
			printPlayingHand();
			System.out.println(toString());
		}
	}
	
	public void printBustedHand(){
		if (numbHands!=1){
			System.out.println("player busts "+"["+(getCurrentHand()+1)+"]");
		}else{
			System.out.println("player busts");
		}
	}
	
	public void printWLP(String str){
		if (numbHands!=1) System.out.println("player "+str+" ["+(getCurrentHand()+1)+"] and his current balance is "+getBalance());
		else  System.out.println("player "+str+" and his current balance is "+getBalance());
	}
	
	@Override
	public String toString() {
		if (numbHands!=1){
			return "player's hand "+"["+(getCurrentHand()+1)+"] "+getHand().toString();
		}
		return "player's hand " + getHand().toString();
	}

	public PileOfChips getPile() {
		return pile;
	}

	public void setPile(PileOfChips pile) {
		this.pile = pile;
	}

}
