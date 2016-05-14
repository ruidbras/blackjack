package blackjack;

import blackjack.Chip;

// TODO: Auto-generated Javadoc
/**
 * The Class PileOfChips.
 */
public class PileOfChips {


	/** The balance. */
	private double balance;
	
	/** The white stack. */
	private Chip whiteStack = new WhiteChip(0);
	
	/** The red stack. */
	private Chip redStack = new RedChip(0);
	
	/** The green stack. */
	private Chip greenStack = new GreenChip(0);
	
	/** The black stack. */
	private Chip blackStack = new BlackChip(0);
	
	/**
	 * Instantiates a new pile of chips.
	 *
	 * @param b the b
	 */
	//constructor
	public PileOfChips(double b){
		this.balance = b;
		divideBalance();
	}
	
	/**
	 * Gets the balance.
	 *
	 * @return the balance
	 */
	//get the player balance to display
	public double getBalance(){
		return balance;
	}
	
	/**
	 * Update pile.
	 *
	 * @param wonBet the won bet
	 */
	//add
	public void updatePile(double wonBet){
		balance +=wonBet;
		divideBalance();

		/*System.out.println("Your pile has now: ");
		System.out.println(whiteStack.numberOfChips +" white chips");
		System.out.println(redStack.numberOfChips +" red chips");
		System.out.println(greenStack.numberOfChips +" green chips");
		System.out.println(blackStack.numberOfChips +" black chips");*/
	}
	
	/**
	 * Divide balance.
	 */
	public void divideBalance(){
		int n = (int) (balance/100);
		getBlackStack().setNumberOfChips(n);
		double r = balance%100;
		n = (int) (r/25);
		getGreenStack().setNumberOfChips(n);
		r = (balance)%100%25;
		n = (int) (r/5);
		getRedStack().setNumberOfChips(n);
		r = (balance)%100%25%5;
		n = (int) (r/1);
		getWhiteStack().setNumberOfChips(n);
	}
	
	/**
	 * Change chips.
	 *
	 * @param bet the bet
	 */
	public void changeChips(int bet){
		if (bet%100%25%5%1!=0){
			System.out.println("You must bet an integer value");
			return;
		}
		else{
			
		}
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PileOfChips [balance=" + balance + ", whiteStack=" + getWhiteStack() + ", redStack=" + getRedStack()
				+ ", greenStack=" + getGreenStack() + ", blackStack=" + getBlackStack() + "]";
	}

	/**
	 * Gets the white stack.
	 *
	 * @return the white stack
	 */
	public Chip getWhiteStack() {
		return whiteStack;
	}

	/**
	 * Sets the white stack.
	 *
	 * @param whiteStack the new white stack
	 */
	public void setWhiteStack(Chip whiteStack) {
		this.whiteStack = whiteStack;
	}

	/**
	 * Gets the red stack.
	 *
	 * @return the red stack
	 */
	public Chip getRedStack() {
		return redStack;
	}

	/**
	 * Sets the red stack.
	 *
	 * @param redStack the new red stack
	 */
	public void setRedStack(Chip redStack) {
		this.redStack = redStack;
	}

	/**
	 * Gets the green stack.
	 *
	 * @return the green stack
	 */
	public Chip getGreenStack() {
		return greenStack;
	}

	/**
	 * Sets the green stack.
	 *
	 * @param greenStack the new green stack
	 */
	public void setGreenStack(Chip greenStack) {
		this.greenStack = greenStack;
	}

	/**
	 * Gets the black stack.
	 *
	 * @return the black stack
	 */
	public Chip getBlackStack() {
		return blackStack;
	}

	/**
	 * Sets the black stack.
	 *
	 * @param blackStack the new black stack
	 */
	public void setBlackStack(Chip blackStack) {
		this.blackStack = blackStack;
	}
	
	/*public static void main(String[] args){
	
	PileOfChips pile = new PileOfChips(100.5);
	System.out.println(pile);
	pile.updatePile(-1);
	System.out.println(pile);
	pile.updatePile(1);
	System.out.println(pile);
	
	
	
	}*/
}
