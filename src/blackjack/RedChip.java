package blackjack;


import blackjack.Chip;

public class RedChip extends Chip{

	//Red Chip extends the class Chip
	//This Constructor invokes super() to create a "Red Chip"
	//Green Chips have the value of 5 and they are represented by the String correspondent to their color: "red".
	//It receives the parameter n which is the number of red chips.
	public RedChip(int n) {
		super(5, "red", n);
	}

}