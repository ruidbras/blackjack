package blackjack;

public class Strategy {
	private double plays;
	private double playerbj;
	private double dealerbj;
	private double wins;
	private double loses;
	private double pushes;
	private final double balanceinit;
	private int bet;
	private int minbet;
	private int maxbet;
	
	Strategy(double i, int min, int max){
		balanceinit = i;
		plays = 0;
		playerbj = 0;
		dealerbj = 0;
		wins = 0;
		loses = 0;
		pushes = 0;
		minbet = min;
		maxbet = max;
		bet = min;
	}
	
	public int getBet(double current){
		int b;
		b = (int)current + bet;
		if(b<minbet){
			return minbet;
		}else if(b>maxbet){
			return maxbet;
		}else{
			return b;
		}
	}
	
	public void addPlays(){
		plays+=1;
	}
	
	public void addPlayerbj(){
		playerbj+=1;
	}
	
	public void addDealerbj(){
		dealerbj+=1;
	}
	
	public void addWins(){
		wins+=1;
		bet = minbet;
	}
	
	public void addLoses(){
		loses+=1;
		bet = -minbet;
	}
	
	public void addPushes(){
		pushes+=1;
		bet = 0;
	}
	
	public void printStats(double balance){
		System.out.println("BJ P/D	:	"+playerbj/plays+"/"+dealerbj/plays);
		System.out.println("Win		:	"+wins/plays);
		System.out.println("Lose		:	"+loses/plays);
		System.out.println("Push		:	"+pushes/plays);
		System.out.println("Balance		:	"+balance+"("+(balance-balanceinit)*100/balanceinit+"%)");
	}
	
	public void resetBet(){
		bet = minbet;
	}

}
