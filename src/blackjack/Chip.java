package blackjack;


//This class represents a type of chip used to make bets.
//When a Chip is created, in fact, it can correspond to a multiple number of real chips.
//This happens because each Chip has 3 parameters:
//value: each type of chip has a different value
//color: each type of chip is differentiated by its color
//numberOfChips: each type of chip has a number correspondent to the number of chips of that type that the player has
public abstract class Chip {

	int value;
	String color;
	private int numberOfChips;
	
	//The Chip is constructed receiving its three parameters
	public Chip(int v, String c, int n){
		this.value = v;
		this.color = c;
		this.setNumberOfChips(n);
	}

	//Returns the number of chips of a type of Chip as a String (used to print on the graphical interface)
	@Override
	public String toString() {
		return getNumberOfChips() + " ";
	}

	//This method is used to retrieve the number of chips of a type of Chip
	public int getNumberOfChips() {
		return numberOfChips;
	}

	//This method is used to change the number of chips of a type of Chip
	public void setNumberOfChips(int numberOfChips) {
		this.numberOfChips = numberOfChips;
	}
	
}

