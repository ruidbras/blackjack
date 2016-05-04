package blackjack;


public abstract class Chip {

	int value;
	String color;
	int numberOfChips;
	

	public Chip(int v, String c, int n){
		this.value = v;
		this.color = c;
		this.numberOfChips = n;
	}


	@Override
	public String toString() {
		return numberOfChips + " ";
	}

	
	
	/*public Chip(int i, String string){
	this.value=i;
	this.color=string;
	}*/

	/*
	public void addChip(Chip c){
		numberOfChips++;
	}

	public void removeChip(Chip c){
		numberOfChips--;
	}*/
	
}
