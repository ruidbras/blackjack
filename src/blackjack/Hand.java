package blackjack;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Hand {
	/* Fields */
	List<Card> hand;
	private int total;
	private Deck deck;
	private Junk junk;

	
	Hand(Deck d, Junk j){
		hand = new LinkedList<Card>();
		total = 0;
		deck = d;
		junk = j;
	}
	
	public void drawCard(){
		if(total<21){
			hand.add(deck.dealCard());
			total = gentotal();
		}else{
			System.out.println("[!]Não é possível pedir mais cartas");
		}
	}
	
	public int gentotal(){
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
		Iterator<Card> it = hand.iterator();
		while(it.hasNext()){
			Card element = it.next();
			junk.getCards().add(element);
			it.remove();
		}
		total = 0;
	}
	

	@Override
	public String toString() {
		return "Hand [hand=" + hand + "]" + "   Total:" + total;
 	}
	
	
}
