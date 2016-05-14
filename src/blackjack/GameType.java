package blackjack;

/**This abstract Class has a Player, a Dealer, a Strategy, a Shoe, a Junk, a Statistics, some Boolean variables to indicate states and an Integer variable.
* This class has is used to perform all the interactions between the player and the dealer, to implement every moves in each side rule, and to
* define the rules of the blackjack game.
* During the game all the wins, loses, pushes and blackjacks occurrences are counted to calculate statistics.
* ingame is a boolean variable that indicates if a player is in game or not, this variable is set to true when it's typed d, and set to false when the game ends and the dealer removes all the cards from the table.
* wasASplit is set to true when it's made the first split of the game.
* insuranceMode is set to true after making insurance.
* firstplay means that the player is currently playing one hand that stills have only two cards or not.
* alreadyBet is set to true after making b command successfully.
* count_splits is the number of splits performing during one play (from the first time that it's typed d until the cards are removed). This value can be 0 to 3, and no more than 3.
* @author Pedro Esteves, Ricardo Cristino, Rui Bras
* @version 1.0
*/
public abstract class GameType {
	protected Player player;
	protected Dealer dealer;
	protected Strategy str;
	private Shoe shoe;
	private Junk junk;
	protected Statistics statistics;
	private boolean ingame;
	private boolean wasASplit;
	private boolean insuranceMode;
	private boolean firstplay;
	private boolean alreadyBet;
	protected int count_splits;

	/**The constructor*/
	public GameType(Shoe shoe, Junk junk, Player p, Dealer d, Statistics s, Strategy str){
		player = p;
		dealer = d;
		statistics = s;
		this.shoe = shoe;
		this.junk=junk;
		wasASplit=false;
		ingame=false;
		insuranceMode=false;
		firstplay = true;
		alreadyBet=false;
		count_splits=0;
		this.str = str;
	}
	
	/**Returns ingame.*/
	public boolean ingame(){
		return ingame;
	}
	/**Sets ingame to state.*/
	protected void setInGame(boolean state){
		ingame=state;
	}
	/**Returns wasASplit.*/
	public boolean wasASplit(){
		return wasASplit;
	}
	/**Sets wasASplit to state.*/
	protected void setWasASplit(boolean state){
		wasASplit=state;
	}
	/**Returns firstplay.*/
	public boolean firstplay(){
		return firstplay;
	}
	/**Sets firstplay to state*/
	protected void setFirstplay(boolean state){
		firstplay=state;
	}
	/**Returns counts_plits.*/
	public int count_splits(){
		return count_splits;
	}
	/**Returns insuranceMode.*/
	public boolean insuranceMode(){
		return insuranceMode;
	}
	/**Sets insuranceMode to state.*/
	protected void setInsuranceMode(boolean state){
		insuranceMode=state;
	}
	/**Returns alreadyBet.*/
	protected boolean alreadyBet(){
		return alreadyBet;
	}
	/**Sets alreadyBet to state.*/
	protected void setAlreadyBet(boolean state){
		alreadyBet=state;
	}
	
	public abstract void makeBet(double b, double min_bet);
	public abstract void deal(double min_bet);
	public abstract void split();
	public abstract void insurance();
	public abstract void hit();
	public abstract void stand();
	public abstract boolean dealerFinalizeCards();
	public abstract void finalizeDealer();
	public abstract void doubleDown();
	public abstract void cleanTable();
	public abstract void surrender();
	
	/**Returns the percentage of shoe played.*/
	public double getPercentageDeck(){
		return junk.countCards()/(junk.countCards()+shoe.countCards())*100;
	}
	
	/** This method receives the minimum bet, maximum bet, balance, shoe, shuffle percentage and readshoe. readshoe indicates if the shoe
	 *is read from a file or not. It always verifies if minimum bet is greater or equal to 1, if maximum bet is greater than 10 times the
	 *minimum bet and lesser than 20 times the minimum bet, and if balance is greater than 50 times the minimum bet.
	 *Only when readshoe is false the conditions shoe must be greater than 4 and lesser than 8 and Shuffle must be greater than 10 
	 *and lesser than 100 are verified.	
	*/
	public void checkInputs(double min_bet, double max_bet, double balance, int shoe, double shuffle, boolean readshoe){
		if(min_bet < 1){
			System.out.println("Minimum Bet must be greater or equal to 1");
			System.exit(0);
		}
		if(max_bet<10*min_bet || max_bet>20*min_bet){
			System.out.println("Max Bet must be greater than 10 times the Min Bet and lesser than 20 times the Min Bet");
			System.exit(0);
		}
		if(balance<50*min_bet){
			System.out.println("Balance must be greater than 50 times the Min Bet");
			System.exit(0);
		}
		if((shoe<4 || shoe>8) && !readshoe){
			System.out.println("Shoe must be greater than 4 and lesser than 8");
			System.exit(0);
		}
		if((shuffle<10 || shuffle>100) && !readshoe){
			System.out.println("Shuffle must be greater than 10 and lesser than 100");
			System.exit(0);
		}
	}
	
	
	/**Checks if the bet is between the minimum and maximum values allowed.*/
	public boolean betLimit(double b, double min_bet, double max_bet){
		if(b>=min_bet && b<=max_bet){
			return true;
		}
		else{
			return false;
		}	
	}
	
}
