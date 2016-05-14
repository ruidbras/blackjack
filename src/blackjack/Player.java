package blackjack;

import java.util.LinkedList;

/**
 * This class represents a player of blackjack, that has Player has two lists: the list of Hands and the list of bets.
 * The hands and bets are related in a way that if a hand and a bet are in the same indexes in the corresponding Lists
 * than the bet value is the bet associated to that hand.
 * numHands corresponds to the number of hands that player has at some instant.
 * currentHand gives the index of the current hand in play in the list hands.
 * insuranceBet is the value bet in insurance.
 * oldbet is the first bet from the previous hand played.
 * @author Pedro Esteves, Ricardo Cristino, Rui Brás
 * @version 1.0
 */
public class Player {

	LinkedList<Hand> hands;
	private PileOfChips pile;
	public LinkedList<Double> bet;
	int numbHands;
	int currentHand;
	double insuranceBet;
	double oldbet;
	
	/**This constructor receives i that is the balance of the player, to initialize the PileOfChips.
	 * 
	 * @param i
	 */
	public Player(double i){
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
	
	/**Returns pile.
	 * 
	 * @return
	 */
	public PileOfChips getPile() {
		return pile;
	}
	
	/**Returns the hand in index i from the list hands.
	 * 
	 * @param i
	 * @return
	 */
	public Hand getHand(int i){
		return hands.get(i);
	}
	/**Empties list of cards from hand in position i in the list hands, calling method emptyCards() from the abstract class CollectionOfCards.
	 * 
	 * @param i
	 */
	protected void emptyHand(int i){
		hands.get(i).emptyCards();
	}
	/**Removes the double value in index i of the list bet.
	 * 
	 * @param i
	 */
	protected void removeBet(int i){
		getBets().remove(i);
	}
	/**Removes the Hand in index i of the list hands.
	 * 
	 * @param i
	 */
	protected void removeHand(int i){
		getHands().remove(i);
	}
	
	/**Gets the list LinkedList<Card> of the hand in position i from hands list.
	 * 
	 * @param i
	 * @return
	 */
	public LinkedList<Card> getCards(int i){
		return hands.get(i).getCards();
	}
	/**Returns bet.
	 * 
	 * @return
	 */
	public LinkedList<Double> getBets(){
		return bet;
	}
	/**Returns hands.
	 * 
	 * @return
	 */
	public LinkedList<Hand> getHands(){
		return hands;
	}
	/**Returns oldbet.
	 * 
	 * @return
	 */
	public double getOldbet(){
		return oldbet;
	}
	/**Returns insuranceBet.
	 * 
	 * @return
	 */
	public double getInsuranceBet(){
		return insuranceBet;
	}
	/**Returns numbHands.
	 * 
	 * @return
	 */
	public int getNumHands(){
		return numbHands;
	}
	/**Returns currentHand.
	 * 
	 * @return
	 */
	public int getCurrentHand(){
		return currentHand;
	}
	/**Returns the Hand that is currently in play.
	 * 
	 * @return
	 */
	public Hand getHand() {
		return hands.get(getCurrentHand());
	}
	/**Gets the balance from players pile.
	 * 
	 * @return
	 */
	public double getBalance() {
		return getPile().getBalance();
	}
	/**Returns the bet associated to the hand currently in play.
	 * 
	 * @return
	 */
	public double getBet() {
		return bet.get(getCurrentHand());
	}
	/**Updates the pile adding the value d if it is positive, or removing if it is negative.
	 * 
	 * @param d
	 */
	protected void setBalance(double d) {
		getPile().updatePile(d);
	}
	
	/**Associates PileOfChips pile.
	 * 
	 * @param pile
	 */
	private void setPile(PileOfChips pile) {
		this.pile = pile;
	}
	
	/**Returns the number of hands in game.
	 * 
	 */
	protected void setNumbHands(){
		numbHands=hands.size();
	}
	
	 /**
	  * When player hits insurance, it's set insuranceBet to the current bet value, than it's verified
	  * if balance is enough to do this play before updating balance.
	  * @return
	  */
	protected boolean setInsuranceBet(){
		insuranceBet=getBet();
		if(getBalance()<insuranceBet){
			return false;
		}
		setBalance(-insuranceBet);
		return true;
	}
	
	
	/**
	 * When we have only one hand, this function set's its associated bet to zero, if we have more than one hand it just removes the bets.
	 */
	protected void setBetZero(){
		bet.set(getCurrentHand(),(double)0);
	}
	

	/**Updates current hand.
	 * 
	 * @param n
	 */
	protected void setCurrentHand(int n){
		currentHand=n;
	}
	
	/**Adds a card c to the current hand.
	 * 
	 * @param c
	 */
	protected void hit(Card c){
		getHand().addCard(c);
		
	}
	
	/**This function verifies if the player already made the bet (if typed b command). Returns false if that condition wasn't verified and
	 * true if it was verified. When it returns true, it also sets the oldbet to the value of the new bet.
	 *  
	 * @return
	 */
	public boolean deal(){
		if(getBet()==0){
			return false;
		}
		oldbet = bet.get(getCurrentHand());
		return true;
	}
	
	
	/**This function gets the value of the actual bet of associated to the current hand in play, than debits it in players balance,
	 * doubles the value of the bet and adds a card to the hand in play from the list of hands.
	 * 
	 * @param c
	 */
	protected void doubleDown(Card c){
		double b=bet.get(getCurrentHand());
		setBalance(-b);
		bet.set(getCurrentHand(), 2*b);
		hands.get(getCurrentHand()).addCard(c);
	}
	

	/**
	 * This function is used when it's typed the command b is retyped. If the player retypes bet command, the previous bet is re-added to the
	 * to player's balance. If the player typed only "b", if it wasn't any hand played before it bets a value corresponding to the minimum bet,
	 * if it were played other hands before, than it bets the value played in the previous hand.
	 * If the played typed "b value" than it bets the specified value
	 * In the end the new value of the bet is discounted in the player's balance and the bet value is updated
	 * If this function returns false it's because the player didn't have enough credits to conclude the action.
	 * 
	 * @param b
	 * @param min_bet
	 * @return
	 */
	protected boolean reBet(double b, double min_bet){
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
	

	/**This function checks the b command and sets a value to the bet. If the player types "b" the bet value is set to the value of the bet
	 * from the previous hand played, if there was no previous hand the value is set to the minimum bet.
	 * If it was typed "b value" it bets value.
	 * All this operations are only allowed if the player have a balance greater or equal than the bet value.
	 * In the end balance and bet values are updated.
	 * If this function returns false it's because the player didn't have enough credits to perform the action.
	 * 
	 * @param b
	 * @param min_bet
	 * @return
	 */
	protected boolean bet(double b, double min_bet){
		if(b==0){
			if(oldbet==0){
				if(getPile().getBalance()<min_bet){
					return false;
				}
				setBalance(-min_bet);
				bet.set(getCurrentHand(), min_bet);
				return true;
			}
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

	/**This function receives two arguments from type Card.
	 * When the function is called, a new empty Hand is created and added to the list hands, in the index after the position of the splitting hand.
	 * A new bet is also added in bet list, in the same index as the new Hand, and it's set to the value of the bet of the splitting hand.
	 * The card in index 1 from the splitting hand is added to the new hand, card a is added to the splitted hand, and card b is added to the new hand.
	 * If the splitting hand is a pair of aces, the hand state handCanBeHit is set to false in both new hands to prevent any further hit or double.
	 * The only allowed actions after the split of two aces is standing of splitting if there is another pair of aces.
	 * 
	 * @param a
	 * @param b
	 */
	protected void split(Card a, Card b){
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
	

	/**This function returns true if all hands have busted or false if not all hands have busted. Values of the elements of the list bet are set to zero
	 * in game class when a hand busts.
	 * 
	 * @return
	 */
	public boolean allHandsBusted(){
		for(Double d: bet){
			if(d!=0){
				return false;
			}
		}
		return true;
	}
	
	/**When it was splits, it's typed in the terminal the hand that is currently in play.
	 * 
	 */
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
	/**Makes the first prints after commands s, h, 2 and p. Function receives a string corresponding the action made ("2", "h", "2" or "p").
	 * 
	 * @param action
	 */
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
	/**Prints that player busts if there is only one hand ore the hand that have busted if there were more than one hand.
	 * 
	 */
	public void printBustedHand(){
		if (numbHands!=1){
			System.out.println("player busts "+"["+(getCurrentHand()+1)+"]");
		}else{
			System.out.println("player busts");
		}
	}
	/**Prints the final result of the game, and indicates which hand won, pushed and lost.
	 * 
	 * @param str
	 */
	public void printWLP(String str){
		if (numbHands!=1) System.out.println("player "+str+" ["+(getCurrentHand()+1)+"] and his current balance is "+getBalance());
		else  System.out.println("player "+str+" and his current balance is "+getBalance());
	}
	
	/**Returns a string with the hand currently in play.
	 * 
	 */
	@Override
	public String toString() {
		if (numbHands!=1){
			return "player's hand "+"["+(getCurrentHand()+1)+"] "+getHand().toString();
		}
		return "player's hand " + getHand().toString();
	}

}
