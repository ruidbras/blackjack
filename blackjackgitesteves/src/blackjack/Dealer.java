package blackjack;

public class Dealer {
	Hand hand;

	/* Constructor */
	Dealer(Deck d, Junk j){
		hand = new Hand(d,j);
	}
	
	/* Method */
	public Hand getHand() {
		return hand;
	}
	
	public boolean canHaveBlackjack(){
		if(hand.countCards()==2){
			if(hand.hand.get(0).getHardValue()==10){
				return true;
			}
		}
		return false;
	}
	
	public void drawHand(){
		if(hand.countCards()!=0){
			return;
		}
		hand.drawCard();
		hand.drawCard();
	}
	
	public void printDealersFirstTwo(){
		if (hand.countCards()==2){
			System.out.println("Dealers hand: ["+ hand.getFirst() + ", ?]");
		}
	}
	
	public void finalize(){
		while(hand.genTotal()<17)
			hand.drawCard();
	}

	@Override
	public String toString() {
		return "Dealer: " + hand;
	}
	
	
}
