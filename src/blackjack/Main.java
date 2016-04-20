package blackjack;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Deck deck = new Deck(4);
		Junk junk = new Junk();
		Player player = new Player(deck, junk, 1000);
		Dealer dealer = new Dealer(deck, junk);
		
		deck.shuffle();
		deck.shuffle();
		deck.printDeck();
		
		/*player.bet(10);
		dealer.getFirst();
		System.out.println(dealer.toString());
		System.out.println(player.toString());
		player.hit();
		System.out.println(player.toString());*/
		
		/*hand.drawCard();
		System.out.println(hand.toString());
		hand.drawCard();
		System.out.println(hand.toString());
		hand.drawCard();
		System.out.println(hand.toString());
		deck.printDeck();
		hand.cleanHand();
		System.out.println(hand.toString());
		hand.drawCard();
		System.out.println(hand.toString());
		hand.drawCard();
		System.out.println(hand.toString());
		hand.drawCard();
		System.out.println(hand.toString());
		deck.printDeck();
		hand.cleanHand();
		System.out.println(junk.toString());*/
		
		
		
	}

}
