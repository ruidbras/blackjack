package blackjack;

// TODO: Auto-generated Javadoc
/**
 * This class has associated three classes. The Hand of the dealer, Shoe containing all the cards to deliver dealer
 *  and player, and Junk to store all used cards.
 * @author Pedro Esteves, Ricardo Cristino, Rui Bras
 * @version 1.0
 *
 */

public class Dealer {
	
	/** The hand. */
	private Hand hand;
	
	/** The shoe. */
	private Shoe shoe;
	
	/** The junk. */
	private Junk junk;

	
	/**
	 * The constructor receives as inputs the Shoe and Junk to associate and creates a new empty Hand.
	 *
	 * @param d the d
	 * @param j the j
	 */
	public Dealer(Shoe d, Junk j){
		hand = new Hand();
		shoe=d;
		junk=j;
	}
	
	/**
	 * Returns dealer's hand.
	 *
	 * @return the dealer hand
	 */
	public Hand getDealerHand() {
		return hand;
	}

	/**
	 * Verifies if dealer's shown card is an Ace, returns true if it is and false if it's not.
	 *
	 * @return true, if successful
	 */
	public boolean canHaveBlackjack(){
		if(hand.countCards()==2){
			if(hand.getCards().get(0).getHardValue()==11){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Prints in the terminal the initial hand of the dealer, hiding the hole card with X.
	 */
	public void printDealersFirstTwo(){
		if (hand.countCards()==2){
			System.out.println("dealer's hand "+ hand.getFirst().toString() + " X");
		}
	}

	/**
	 * Accesses shoe and takes a card from the beginning of the LinkedList<Card>. If the shoe is empty the junk is added back to the deck
	 * and re-shuffled. After this, a card from the beginning of the LinkedList<Card> is returned.
	 *
	 * @return the card
	 */
	protected Card dealCard(){
		Card temp=shoe.dealCard();
		if(temp==null){
			shoe.addCards(junk.getCards());
			junk.emptyCards();
			shoe.shuffle();
			return shoe.dealCard();
		}
		return temp;
	}
	
	/**
	 * Adds a new card to the hand of the dealer, this card is took from the shoe.
	 */
	public void drawCardToDealer(){
		hand.addCard(dealCard());
	}
	
	/**
	 * Distributes the first four cards of the game, the first two are dealt to the dealer and the next two to the player.
	 * It must receive a Player as input.
	 *
	 * @param p the p
	 */
	public void drawFirstFour(Player p){
		if(hand.countCards()!=0&&p.getHand().countCards()!=0&&p.getNumHands()==1){
			return;
		}
		hand.addCard(dealCard());
		hand.addCard(dealCard());
		p.getHand().addCard(dealCard());
		p.getHand().addCard(dealCard());
	}

	/**
	 * Adds the cards from dealer's hand to the junk, and then empties the elements from his hand.
	 */
	public void cleanDealerHand(){
		junk.addCards(hand.getCards());
		hand.emptyCards();
	}
	
	/**
	 * Receives as input a Player, than removes all hands and bets of an index greater than 0 from the respective lists hands and bet,
	 * and leaves the first hand of the list. The cards from the hands are added to the junk and bet from the first index is set to 0.
	 *
	 * @param p the p
	 */
	public void cleanPlayerHands(Player p){
		p.setNumbHands();
		int i = p.getNumHands()-1;
		while(i>0){
			junk.addCards(p.getCards(i));
			p.emptyHand(i);
			p.removeHand(i);
			p.removeBet(i);
			p.setNumbHands();
			i=p.getNumHands()-1;
		}
		junk.addCards(p.hands.get(i).getCards());
		p.emptyHand(0);
		p.getHand(0).setHandCanBeHit(true);
		p.getBets().set(0, (double) 0);
		p.setNumbHands();
		p.setCurrentHand(0);
	
}

	/**
	 * Returns a string in the format "dealer's hand: " + hand.toString().
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "dealer's hand " + hand;
	}
}