package blackjack;

import java.util.LinkedList;
import java.util.List;

public class Junk {
	
	List<Card> cards;

	Junk(){
		cards = new LinkedList<Card>();
	}
	
	/* Methods */
	/* Envia as cartas de novo para o Deck */
	public void removeAll(){
		
	}

	public List<Card> getCards() {
		return cards;
	}

	@Override
	public String toString() {
		return "Junk [cards=" + cards + "]";
	}
	
	
	
	
}
