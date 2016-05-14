package blackjack;


public class WhiteChip extends Chip{
	//White Chip extends the class Chip
	//This Constructor invokes super() to create a "White Chip"
	//White Chips have the value of 1 and they are represented by the String correspondent to their color: "white".
	//It receives the parameter n which is the number of white chips.
	public WhiteChip(int n) {
		super(1, "white", n);
	}

}
