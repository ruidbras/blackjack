package blackjack;

import blackjack.Chip;

public class PileOfChips {


	private double balance;
	private Chip whiteStack = new WhiteChip(0);
	private Chip redStack = new RedChip(0);
	private Chip greenStack = new GreenChip(0);
	private Chip blackStack = new BlackChip(0);
	
	//constructor
	public PileOfChips(double b){
		this.balance = b;
		divideBalance();
	}
	
	//get the player balance to display
	public double getBalance(){
		return balance;
	}
	
	//add
	public void updatePile(double wonBet){
		balance +=wonBet;
		divideBalance();

		System.out.println("Your pile has now: ");
		System.out.println(whiteStack.numberOfChips +" white chips");
		System.out.println(redStack.numberOfChips +" red chips");
		System.out.println(greenStack.numberOfChips +" green chips");
		System.out.println(blackStack.numberOfChips +" black chips");
	}
	
	public void divideBalance(){
		int n = (int) (balance/100);
		blackStack.numberOfChips = n;
		double r = balance%100;
		n = (int) (r/25);
		greenStack.numberOfChips = n;
		r = (balance)%100%25;
		n = (int) (r/5);
		redStack.numberOfChips = n;
		r = (balance)%100%25%5;
		n = (int) (r/1);
		whiteStack.numberOfChips = n;
	}
	
	public void changeChips(int bet){
		if (bet%100%25%5%1!=0){
			System.out.println("You must bet an integer value");
			return;
		}
		else{
			
		}
	}
	
	
	@Override
	public String toString() {
		return "PileOfChips [balance=" + balance + ", whiteStack=" + whiteStack + ", redStack=" + redStack
				+ ", greenStack=" + greenStack + ", blackStack=" + blackStack + "]";
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
