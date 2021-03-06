package blackjack;

// TODO: Auto-generated Javadoc
/**
 * This class has ten parameters that contain informations about one game of blackjack. The parameters are:
 * plays - the total number of hands that player had during the game;
 * playerbj - the total number of blackjacks obtained by the player;
 * dealerbj - the total number of blackjacks obtained by the dealer;
 * wins - total number of times a player wins a hand;
 * loses - total number of times a player loses a hand;
 * pushes - total number of times a player pushes a hand;
 * bet - stores the value that must be added to the previous bet;
 * balanceinit - initial balance of the player;
 * minbet - minimum bet allowed;
 * maxbet - maximum bet allowed.
 * @author Pedro Esteves, Ricardo Cristino, Rui Bras
 * @version 1.0
 */

public class Statistics {
	
	/** The plays. */
	public int plays;
	
	/** The playerbj. */
	public int playerbj;
	
	/** The dealerbj. */
	public int dealerbj;
	
	/** The wins. */
	public int wins;
	
	/** The loses. */
	public int loses;
	
	/** The pushes. */
	public int pushes;
	
	/** The balanceinit. */
	public final double balanceinit;
	
	/** The bet. */
	public int bet;
	
	/** The minbet. */
	public int minbet;
	
	/** The maxbet. */
	public int maxbet;
	
	/**
	 * Constructor receives a value to set balanceinit, bet, minbet and maxbet, the rest of the parameters are set to zero.
	 *
	 * @param i the i
	 * @param min the min
	 * @param max the max
	 */
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
	
	/**
	 * This method returns the value suggested by the basic strategy for the next bet. 
	 * This value is based on the previous bet and on the result of the previous play (win, lose or push).
	 *
	 * @param current the current
	 * @return the bet
	 */
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
	/**
	 * Adds one unit to plays.
	 */
	public void addPlays(){
		plays+=1;
	}
	/**
	 * Adds one unit to playerbj.
	 */
	public void addPlayerbj(){
		playerbj+=1;
	}
	/**
	 * Adds one unit to dealerbj.
	 */
	public void addDealerbj(){
		dealerbj+=1;
	}
	/**
	 * Adds one unit to wins.
	 */
	public void addWins(){
		wins+=1;
		bet = minbet;
	}
	/**
	 * Adds one unit to losses, and subtracts minbet to bet.
	 */
	public void addLoses(){
		loses+=1;
		bet = -minbet;
	}
	/**
	 * Adds one unit to pushes.
	 */
	public void addPushes(){
		pushes+=1;
		bet = 0;
	}
	
	/**
	 * This method prints several statistics of the game. It prints the percentage of blackjacks done by the player and the dealer, 
	 * the percentage of wins, loses and pushes, and lastly it prints the current balance and the percentage (positive or negative)
	 * in comparison with the initial balance.
	 *
	 * @param balance the balance
	 */
	public void printStats(double balance){
		System.out.println("BJ P/D	:	"+(float)playerbj/plays+"/"+(float)dealerbj/plays);
		System.out.println("Win		:	"+(float)wins/plays);
		System.out.println("Lose		:	"+(float)loses/plays);
		System.out.println("Push		:	"+(float)pushes/plays);
		System.out.println("Balance		:	"+balance+"("+(balance-balanceinit)*100/balanceinit+"%)");
	}
	/**
	 * Sets bet to minbet.
	 */
	public void resetBet(){
		bet = minbet;
	}

}
