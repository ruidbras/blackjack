package blackjack;

import java.util.LinkedList;

public class Hand extends CollectionOfCards{

	private int total;
	private boolean handCanBeHit;
	
	Hand(){
		total = 0;
		handCanBeHit=true;
	}

	public boolean getHandCanBeHit(){
		return handCanBeHit;
	}
	
	public LinkedList<Card> getCards(){
		return cards;
	}
	
	public int getTotal(){
		return total;
	}
	
	public Card getFirst(){
		return cards.get(0);
	}
	
	public void setHandCanBeHit(boolean c){
		handCanBeHit=c;
	}
	
	public boolean twoAces(){
		if(countCards()==2){
			if(cards.get(0).getHardValue()==11&&cards.get(1).getHardValue()==11){
				return true;
			}
		}
		return false;
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
			if(cards.get(0).getSoftValue()==cards.get(1).getSoftValue()){
				return true;
			}
		}
		return false;
	}
	public boolean worthForDouble(){
		if(cards.size()==2){
			if(genTotal()==9||genTotal()==10||genTotal()==11){
				return true;
			}
		}
		return false;
	}

	public boolean isSoft(){
		int r = 0;
		for(Card card: cards){
			r+=card.getSoftValue();
		}
		if(r == this.total)
			return false;
		return true;
	}
	
	public void addCard(Card card){
		cards.add(card);
		total=genTotal();
	}
	
	public int genTotal(){
		int r=0;
		for(Card card: cards){
			r+=card.getSoftValue();
		}
		for(Card card: cards){
			if(card.getHardValue()!=card.getSoftValue()){
				if((r-1+card.getHardValue())<=21)
					r+= (card.getHardValue()-1);
			}
		}
		return r;
	}
	
	@Override
	public String toString() {
		String ret="";
		for(Card card: cards){
			ret = ret + card.toString()+" ";
		}
		
		return ret + "(" + total+")";
 	}
	
	
}
