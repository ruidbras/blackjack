package blackjack;


// TODO: Auto-generated Javadoc
/**This class inherits the class CollectionOfCards and also has two parameters: int total and boolean handCanBeHit.
 * 
 * @author Pedro Esteves, Ricardo Cristino, Rui Brás
 * @version 1.0
 *
 */
public class Hand extends CollectionOfCards{
	
	
	/** Total is the value of the hand handCanBeHit is a state that defines if a hand can receive another card or not. */
	private int total;
	
	/** The hand can be hit. */
	private boolean handCanBeHit;
	
	/**Constructs an empty LinkedList<Card> implicitly invoking super() constructor, also sets total to 0 and handCanBeHit to true.
	 * 
	 */
	Hand(){
		total = 0;
		handCanBeHit=true;
	}
	
	/**
	 * Returns handCanBeHit.
	 *
	 * @return the hand can be hit
	 */
	public boolean getHandCanBeHit(){
		return handCanBeHit;
	}
	
	/**
	 * Returns the total value.
	 *
	 * @return the total
	 */
	public int getTotal(){
		return total;
	}
	
	/**
	 * Returns the first card of the hand list.
	 *
	 * @return the first
	 */
	public Card getFirst(){
		return getCards().get(0);
	}
	
	/**
	 * Sets the state variable hand can be hit to a boolean value received as input.
	 *
	 * @param c the new hand can be hit
	 */
	public void setHandCanBeHit(boolean c){
		handCanBeHit=c;
	}
	
	/**
	 * Returns true if there are two cards in the hand and if both have the same hard value 11.
	 *
	 * @return true, if successful
	 */
	public boolean twoAces(){
		if(countCards()==2){
			if(getCards().get(0).getHardValue()==11&&getCards().get(1).getHardValue()==11){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if there are two cards in the hand and if the total performs 21.
	 *
	 * @return true, if successful
	 */
	public boolean blackjack(){
		if (total==21){
			if (countCards()==2){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if two cards have the same value.
	 * Checks if their are two cards in the hand and if they have the same soft value.
	 *
	 * @return true, if successful
	 */
	public boolean cardsSameValue(){
		if (countCards()==2){
			if(getCards().get(0).getSoftValue()==getCards().get(1).getSoftValue()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the hand has two cards and if the value of the hand is 9, 10 or 11. Returns true if hand can be doubled or false otherwise.
	 *
	 * @return true, if successful
	 */
	public boolean canDouble(){
		if(getCards().size()==2){
			if(genTotal()==9||genTotal()==10||genTotal()==11){
				return true;
			}
		}
		return false;
	}
	

	/**
	 * Determines if the hand is soft, it sums all soft values from the cards and compares to the total value calculated by method genTotal(),
	 * if it is equal, the method returns false.
	 *
	 * @return true, if is soft
	 */
	public boolean isSoft(){
		int r = 0;
		for(Card card: getCards()){
			r+=card.getSoftValue();
		}
		if(r == this.total)
			return false;
		return true;
	}
	
	/**
	 * Adds a new card to the hand and sets the new value of the hand.
	 *
	 * @param card the card
	 */
	protected void addCard(Card card){
		getCards().add(card);
		total=genTotal();
	}
	
	/**
	 * Calculates the value of the hand, taking into account the soft and hard values of the cards.
	 *
	 * @return the int
	 */
	private int genTotal(){
		int r=0;
		for(Card card: getCards()){
			r+=card.getSoftValue();
		}
		for(Card card: getCards()){
			if(card.getHardValue()!=card.getSoftValue()){
				if((r-1+card.getHardValue())<=21)
					r+= (card.getHardValue()-1);
			}
		}
		return r;
	}
	
	/**
	 * Prints the cards of the hand and the and's value.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		String ret="";
		for(Card card: getCards()){
			ret = ret + card.toString()+" ";
		}
		
		return ret + "(" + total+")";
 	}
	
	
}