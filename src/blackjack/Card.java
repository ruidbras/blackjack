package blackjack;

public class Card {
	private final String num;
	private final String suit;
	private final int softValue;
	private final int hardValue;
	
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
	
	public String getNum(){
		return num;
	}
	
	public int getSoftValue() {
		return softValue;
	}
	public int getHardValue() {
		return hardValue;
	}
	public String toString() {
		return num+suit;
	}
}