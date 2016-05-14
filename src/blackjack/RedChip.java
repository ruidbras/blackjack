package blackjack;


import blackjack.Chip;

// TODO: Auto-generated Javadoc
/**
 * The Class RedChip.
 */
public class RedChip extends Chip{


	/**
	 * Instantiates a new red chip.
	 * Red Chip extends the class Chip
	 * This Constructor invokes super() to create a "Red Chip"
	 * Green Chips have the value of 5 and they are represented by the String correspondent to their color: "red".
	 * It receives the parameter n which is the number of red chips.
	 * 
	 * @param n the n
	 */
	public RedChip(int n) {
		super(5, "red", n);
	}

}