package blackjack;

import java.util.LinkedList;


public class Junk {
	
	LinkedList<Card> cards;

	Junk(){
		cards = new LinkedList<Card>();
	}
	
	/* Methods */
	/* Envia as cartas de novo para o Deck */

	
	public LinkedList<Card> getCards() {
		return cards;
	}
	public void emptyJunk(){
		cards.clear();
	}
	public void addToJunk(LinkedList<Card> h){
		cards.addAll(h);
	}

	@Override
	public String toString() {
		return "Junk [cards=" + cards + "]";
	}
	
}
