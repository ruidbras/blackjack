package blackjack;

import java.util.LinkedList;

//This class has a LinkedList storing objects of type Card.
public abstract class CollectionOfCards {
	
	private LinkedList<Card> cards;
	//Creates an empty LinkedList<Card>.
	
	//Returns the LinkedList<Card> cards.
	public LinkedList<Card> getCards(){
		return cards;
	}
	public CollectionOfCards() {
		cards=new LinkedList<Card>();
	}
	//Appends all the elements from a LinkedList<Card> h in the end cards.
	public void addCards(LinkedList<Card> h){
		cards.addAll(h);
	}
	//Returns a double corresponding to the size of cards.
	public double countCards(){
		return cards.size();
	}
	//Removes all elements from cards.
	public void emptyCards(){
		cards.clear();
	}

}