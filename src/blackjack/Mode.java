package blackjack;

public abstract class Mode {
	int min_bet;
	int max_bet;
	int balance;
	
	public Mode(String[] args) {
		min_bet = Integer.parseInt(args[1]);
		max_bet = Integer.parseInt(args[2]);
		balance = Integer.parseInt(args[3]);
	}
	public abstract void gameMode();

}
