package blackjack;


public abstract class Chip {

	int value;
	String color;
	private int numberOfChips;
	

	public Chip(int v, String c, int n){
		this.value = v;
		this.color = c;
		this.setNumberOfChips(n);
	}


	@Override
	public String toString() {
		return getNumberOfChips() + " ";
	}


	public int getNumberOfChips() {
		return numberOfChips;
	}


	public void setNumberOfChips(int numberOfChips) {
		this.numberOfChips = numberOfChips;
	}
	
}
