package blackjack;

//This class counts the plays (every hand player had during all the game), player's and dealer's blackjacks, wins, losses and pushes

public class Statistics {
	public double plays;
	public double playerbj;
	public double dealerbj;
	public double wins;
	public double loses;
	public double pushes;
	public final double balanceinit;
	public int bet;
	public int minbet;
	public int maxbet;
	
	public Statistics(double i, int min, int max){
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
