package blackjack;

import java.util.LinkedList;

public abstract class CollectionOfCards {
	
	public LinkedList<Card> cards;
	
	public CollectionOfCards() {
		cards=new LinkedList<Card>();
	}
	public void addCards(LinkedList<Card> h){
		cards.addAll(h);
	}
	
	public double countCards(){
		return cards.size();
	}
	
	public void emptyCards(){
		cards.clear();
	}

}