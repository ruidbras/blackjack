package blackjack;


public class GreenChip extends Chip{
	
	//Green Chip extends the class Chip
	//This Constructor invokes super() to create a "Green Chip"
	//Green Chips have the value of 25 and they are represented by the String correspondent to their color: "green".
	//It receives the parameter n which is the number of green chips.
	public GreenChip(int n) {
		super(25, "green", n);
	}

}
