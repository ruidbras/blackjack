package blackjack;

public class Chip {

	int value;
	String color;
	int numberOfChips;
	
	public Chip(int v, String c, int n){
		this.value = v;
		this.color = c;
		this.numberOfChips = n;
	}
	
	public void addChip(Chip c){
		numberOfChips++;
	}

	public void removeChip(Chip c){
		numberOfChips--;
	}
}
