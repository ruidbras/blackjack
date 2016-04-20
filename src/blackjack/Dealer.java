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
	
	public void getFirst(){
		hand.drawCard();
		hand.drawCard();
	}
	
	public void finalize(){
		while(hand.gentotal()<17)
			hand.drawCard();
	}

	@Override
	public String toString() {
		return "Dealer: " + hand;
	}
	
	
}
