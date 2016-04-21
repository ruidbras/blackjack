package blackjack;

import java.util.Iterator;
import java.util.LinkedList;

public class Hand {
	/* Fields */
	LinkedList<Card> hand;
	private int total;
	private Deck deck;
	private Junk junk;

	
	Hand(Deck d, Junk j){
		hand = new LinkedList<Card>();
		total = 0;
		deck = d;
		junk = j;
	}
	
	public boolean blackjack(){
		if (total==21){
			if (countCards()==2){
				return true;
			}
		}
			
		return false;
	}

	public boolean cardsEqual(){
		if (countCards()==2){
			if(hand.get(0)==hand.get(1)){
				return true;
			}
		}
		return false;
	}
	
	public int countCards(){
		int numCards=0;
		Iterator<Card> it = hand.iterator();
		while(it.hasNext()){
			numCards++;
			it.equals(it.next());
		}
		return numCards;
	}
	
	public Card getFirst(){
		return hand.get(0);
	}
	
	public void drawCard(){
		hand.add(deck.dealCard());
		total = genTotal();
	}
	
	public int genTotal(){
		int r=0;
		for(Card card: hand){
			r+=card.getSoftValue();
		}
		for(Card card: hand){
			if(card.getHardValue()!=card.getSoftValue()){
				if((r-1+card.getHardValue())<=21)
					r+= (card.getHardValue()-1);
			}
		}
		return r;
	}
	
	public void cleanHand(){
		junk.addToJunk(hand);
		hand.clear();
	}
	

	@Override
	public String toString() {
		return hand + "   Total:" + total;
 	}
	
	
}
