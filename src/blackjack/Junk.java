package blackjack;

// TODO: Auto-generated Javadoc
/**
 * This class extends CollectionOfCards and it's used to store temporarily the used cards.
 * @author Pedro Esteves, Ricardo Cristino, Rui Brás
 * @version 1.0
 *
 */
public class Junk extends CollectionOfCards{

	/**
	 * Instantiates a new junk.
	 */
	public Junk(){
	}
	
	/**
	 * It has a method to calculate the total value of the cards. This was useful to store Cards that were already erased from the hands
	 * in order to implement the graphical interface
	 *
	 * @return the int
	 */
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
