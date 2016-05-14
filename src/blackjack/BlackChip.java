package blackjack;


// TODO: Auto-generated Javadoc
/**
 * The Class BlackChip.
 */
public class BlackChip extends Chip{

	/**
	 * Instantiates a new black chip.
	 * Black Chip extends the class Chip
	 * This Constructor invokes super() to create a "Black Chip"
	 * Black Chips have the value of 100 and they are represented by the String correspondent to their color: "black".
	 * It receives the parameter n which is the number of black chips.
	 * 
	 * @param n the n
	 */
	public BlackChip(int n) {
		super(100, "black", n);
	}

}