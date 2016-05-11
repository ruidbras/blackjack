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
			System.out.println("dealer's hand "+ hand.getFirst().toString() + " X");
		}
	}
	
	public Card dealCard(){
		Card temp=deck.dealCard();
		if(temp==null){
			deck.addCards(junk.cards);
			junk.emptyCards();
			deck.shuffle();
			return deck.dealCard();
		}
		return temp;
	}
	
	public void drawCardToDealer(){
		hand.addCard(dealCard());
	}
	
	
	public void drawFirstFour(Player p){
		//Apenas verificacoes de erros se chamarmos a função quando nao devemos
		if(hand.countCards()!=0&&p.getHand().countCards()!=0&&p.getNumHands()==1){
			return;
		}
		hand.addCard(dealCard());
		hand.addCard(dealCard());
		p.getHand().addCard(dealCard());
		p.getHand().addCard(dealCard());
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
		return "dealer's hand " + hand;
	}
	
	
}
