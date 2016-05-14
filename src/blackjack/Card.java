package blackjack;

// TODO: Auto-generated Javadoc
//This class has four parameters. num is a string that can be "1" to "10", "J", "Q", "K" or "A".
//suit can be "S" (Spades), "C" (Clubs), "D" (Diamonds) and "H" (Hearts).
//softValue and hardValue correspond to the same number as cards from 1 to 10, than from K, Q and J both correspond.
/**
 * The Class Card.
 */
//For A, softValue=1 and hardValue=11.
public class Card {
	
	/** The num. */
	private final String num;
	
	/** The suit. */
	private final String suit;
	
	/** The soft value. */
	private final int softValue;
	
	/** The hard value. */
	private final int hardValue;
	
	//This constructor defines num and suit by receiving its strings as an input than, depending on this strings, define the correct
	/**
	 * Instantiates a new card.
	 *
	 * @param num the num
	 * @param suit the suit
	 */
	//soft and hard values of the cards.
	public Card(String num, String suit){
		this.num=num;
		this.suit=suit;
		if(num.equals("A")){
			this.softValue=1;
			this.hardValue=11;
		}
		else if(num.equals("J")||num.equals("Q")||num.equals("K")){
			softValue=10;
			hardValue=10;
		}
		else{
			softValue=Integer.valueOf(num);
			hardValue=Integer.valueOf(num);
		}
	}
	
	/**
	 * Gets the num.
	 *
	 * @return the num
	 */
	//Returns num.
	public String getNum(){
		return num;
	}
	
	/**
	 * Gets the soft value.
	 *
	 * @return the soft value
	 */
	//Returns softValue.
	public int getSoftValue() {
		return softValue;
	}
	
	/**
	 * Gets the hard value.
	 *
	 * @return the hard value
	 */
	//Returns hardValue.
	public int getHardValue() {
		return hardValue;
	}
	
	/**
	 * Gets the suit.
	 *
	 * @return the suit
	 */
	//Returns suit.
	 public String getSuit() {
		return suit;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public //Returns a string in the format num+suit.
	 String toString() {
		return num+getSuit();
	}
}