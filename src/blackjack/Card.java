package blackjack;

//This class has four parameters. num is a string that can be "1" to "10", "J", "Q", "K" or "A".
//suit can be "S" (Spades), "C" (Clubs), "D" (Diamonds) and "H" (Hearts).
//softValue and hardValue correspond to the same number as cards from 1 to 10, than from K, Q and J both correspond.
//For A, softValue=1 and hardValue=11.
public class Card {
	private final String num;
	private final String suit;
	private final int softValue;
	private final int hardValue;
	
	//This constructor defines num and suit by receiving its strings as an input than, depending on this strings, define the correct
	//soft and hard values of the cards.
	public Card(String num, String suit){
		this.num=num;
		this.suit=suit;
		if(num.equals("A")){
			this.softValue=1;
			this.hardValue=11;
		}
		else if(num.equals("J")||num.equals("Q")||num.equals("K")){
			softValue=10;
			hardValue=10;
		}
		else{
			softValue=Integer.valueOf(num);
			hardValue=Integer.valueOf(num);
		}
	}
	//Returns num.
	public String getNum(){
		return num;
	}
	
	//Returns softValue.
	public int getSoftValue() {
		return softValue;
	}
	//Returns hardValue.
	public int getHardValue() {
		return hardValue;
	}
	
	//Returns suit.
	 public String getSuit() {
		return suit;
	}
	
	public //Returns a string in the format num+suit.
	 String toString() {
		return num+getSuit();
	}
}