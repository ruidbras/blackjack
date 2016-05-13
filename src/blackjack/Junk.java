package blackjack;

public class Junk extends CollectionOfCards{

	Junk(){
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
	
}
