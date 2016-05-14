package blackjack;


// TODO: Auto-generated Javadoc

/**
 * The Class Chip.
 * This class represents a type of chip used to make bets.
 * When a Chip is created, in fact, it can correspond to a multiple number of real chips.
 * This happens because each Chip has 3 parameters:
 * value: each type of chip has a different value
 * color: each type of chip is differentiated by its color
 * numberOfChips: each type of chip has a number correspondent to the number of chips of that type that the player has
 */
public abstract class Chip {

	/** The value. */
	int value;
	
	/** The color. */
	String color;
	
	/** The number of chips. */
	private int numberOfChips;
	
	/**
	 * Instantiates a new chip.
	 * The Chip is constructed receiving its three parameters
	 *
	 * @param v the v
	 * @param c the c
	 * @param n the n
	 */
	public Chip(int v, String c, int n){
		this.value = v;
		this.color = c;
		this.setNumberOfChips(n);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/**
	 * Returns the number of chips of a type of Chip as a String (used to print on the graphical interface)
	 */
	@Override
	public String toString() {
		return getNumberOfChips() + " ";
	}

	/**
	 * Gets the number of chips.
	 * This method is used to retrieve the number of chips of a type of Chip
	 *
	 * @return the number of chips
	 */
	public int getNumberOfChips() {
		return numberOfChips;
	}

	/**
	 * Sets the number of chips.
	 * This method is used to change the number of chips of a type of Chip
	 *
	 * @param numberOfChips the new number of chips
	 */
	public void setNumberOfChips(int numberOfChips) {
		this.numberOfChips = numberOfChips;
	}
	
}

