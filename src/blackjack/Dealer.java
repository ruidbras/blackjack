package blackjack;

public class Dealer {
	Hand hand;
	Deck deck;
	Junk junk;

	/* Constructor */
	Dealer(Deck d, Junk j){
		hand = new Hand();
		deck=d;
		junk=j;
	}
	
	/* Method */
	public Hand getDealerHand() {
		return hand;
	}
	
	public boolean canHaveBlackjack(){
		if(hand.countCards()==2){
			if(hand.cards.get(0).getHardValue()==11){
				return true;
			}
		}
		return false;
	}
	
	public void printDealersFirstTwo(){
		if (hand.countCards()==2){
			System.out.println("Dealers hand: ["+ hand.getFirst() + ", ?]");
		}
	}
	
	public Card dealCard(){
		if(deck.dealCard()==null){
			System.out.println(junk);
			deck.addCards(junk.cards);
			junk.emptyCards();
			deck.shuffle();
			System.out.println(deck);
		}
		return deck.dealCard();
	}
	
	public void drawCardToDealer(){
		hand.addCard(dealCard());
	}
	
	
	public void drawHandToDealer(){
		if(hand.countCards()!=0){
			return;
		}
		hand.addCard(dealCard());
		hand.addCard(dealCard());
	}
	
	public void finalize(){
		while(hand.genTotal()<17)
			hand.addCard(dealCard());
	}

	public void cleanDealerHand(){
		junk.addCards(hand.cards);
		hand.emptyCards();
	}
	
	public void cleanPlayerHands(Player p){
		p.setNumbHands();
		int i = p.getNumHands()-1;
		while(i>0){
			junk.addCards(p.hands.get(i).cards);
			p.hands.get(i).emptyCards();
			p.hands.remove(i);
			p.bet.remove(i);
			p.setNumbHands();
			i=p.getNumHands()-1;
		}
		junk.addCards(p.hands.get(i).cards);
		p.hands.get(0).emptyCards();
		p.hands.get(0).setHandCanBeHit(true);
		p.bet.set(0, (double) 0);
		p.setNumbHands();
		p.setCurrentHand(0);
	
}
	
	@Override
	public String toString() {
		return "Dealer: " + hand;
	}
	
	
}
