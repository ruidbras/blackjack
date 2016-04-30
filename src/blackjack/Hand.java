package blackjack;

import java.util.Iterator;
import java.util.LinkedList;

public class Hand {
	/* Fields */
	LinkedList<Card> hand;
	private int total;
	private Deck deck;
	private Junk junk;
	private boolean handCanBeHit;
	
	Hand(Deck d, Junk j){
		hand = new LinkedList<Card>();
		total = 0;
		deck = d;
		junk = j;
		handCanBeHit=true;
	}
	
	public void setHandCanBeHit(boolean c){
		handCanBeHit=c;
	}
	public boolean getHandCanBeHit(){
		return handCanBeHit;
	}
	
	public boolean twoAces(){
		if(countCards()==2){
			if(hand.get(0).getHardValue()==11&&hand.get(1).getHardValue()==11){
				return true;
			}
		}
		return false;
	}
	
	public Card getFirst(){
		return hand.get(0);
	}
	
	public int getTotal(){
		return total;
	}
	
	public boolean blackjack(){
		if (total==21){
			if (countCards()==2){
				return true;
			}
		}
		return false;
	}
	
	public boolean cardsSameValue(){
		if (countCards()==2){
			if(hand.get(0).getSoftValue()==hand.get(1).getSoftValue()){
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
	
	public boolean isSoft(){
		int r = 0;
		for(Card card: hand){
			r+=card.getSoftValue();
		}
		if(r == this.total)
			return false;
		return true;
	}
	
	public boolean isPair(){
		if((hand.size())== 2 && this.cardsSameValue() )
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return hand + " Total:" + total;
 	}
	
	
}
