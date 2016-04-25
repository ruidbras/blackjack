package blackjack;

public class PileOfChips {


	private double balance;
	private int numberOfChips;
	private Chip[] chip = new Chip[4]; 
	//postion 0 = white
	//postion 1 = red
	//postion 2 = green
	//postion 3 = black
	
	public PileOfChips(double b, int nw, int nr, int ng, int nb){
		this.balance = b;
		this.chip[0].color = "white";
		this.chip[0].value = 1;
		this.chip[0].numberOfChips = nw;
		this.chip[1].color = "red";
		this.chip[1].value = 5;
		this.chip[1].numberOfChips = nr;
		this.chip[2].color = "green";
		this.chip[2].value = 25;
		this.chip[2].numberOfChips = ng;
		this.chip[3].color = "black";
		this.chip[3].value = 100;
		this.chip[3].numberOfChips = nb;
	}
	
	public double getBalance() {
		return balance;
	}

	public void updatePile(Chip w, Chip r, Chip g, Chip b){
		this.chip[0].numberOfChips = w.numberOfChips;
		this.chip[1].numberOfChips = r.numberOfChips;
		this.chip[2].numberOfChips = g.numberOfChips;
		this.chip[3].numberOfChips = b.numberOfChips;
	}



}
