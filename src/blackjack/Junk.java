package blackjack;

public class Junk extends CollectionOfCards{

	public Junk(){
	}
	
	public int genTotal(){
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
	
}
