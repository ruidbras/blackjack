package blackjack;

import java.util.LinkedList;

/**This class has a LinkedList storing objects of type Card.
 * @author Pedro Esteves, Ricardo Cristino, Rui Bras
 * @version 1.0
 * */
public abstract class CollectionOfCards {
	
	
	private LinkedList<Card> cards;
	
	/**
	 * Creates an empty LinkedList<Card>.
	 */
	public CollectionOfCards() {
		cards=new LinkedList<Card>();
	}
	
	/**Returns the LinkedList<Card> cards.
	 * 
	 * @return
	 */
	public LinkedList<Card> getCards(){
		return cards;
	}
	
	/**Appends all the elements from a LinkedList<Card> h in the end cards.
	 * 
	 * @param h
	 */
	public void addCards(LinkedList<Card> h){
		cards.addAll(h);
	}
	
	/**
	 * Returns a double corresponding to the size of cards.
	 * @return
	 */
	public double countCards(){
		return cards.size();
	}
	
	/**
	 * Removes all elements from cards.
	 */
	public void emptyCards(){
		cards.clear();
	}
}