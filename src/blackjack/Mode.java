package blackjack;

import java.io.IOException;
/**
 * This class implements an abstract class "Mode" that implements a common method
 * for all the "child" classes. It has three variables also common for all the
 * "child" classes (minimum bet, maximum bet and balance). 
 * @author Pedro Esteves, Ricardo Cristino, Rui Brás
 * @version 1.0
 *
 */
public abstract class Mode {
	protected int min_bet;
	protected int max_bet;
	private int balance;
	/**
	 * This constructor gives a value to the three varibles. Their values are
	 * read form the first three arguments (args). 
	 * @param args arguments typed on the command line
	 */
	public Mode(String[] args) {
		min_bet = Integer.parseInt(args[1]);
		max_bet = Integer.parseInt(args[2]);
		balance = Integer.parseInt(args[3]);
	}
	/**
	 * Method to be written by the classes implementing the class "Mode".
	 * It should give a String with the next action to be processed by the game.
	 * @return returns a String with the next instruction
	 * @throws IOException used when the instruction is read on the command line
	 */
	public abstract String getInstruction() throws IOException;
	
	/**
	 * Simple get of the minimum bet
	 * @return returns min_bet (integer)
	 */
	public int getMin_bet(){
		return min_bet;
	}
	/**
	 * Simple get of the maximum bet
	 * @return returns max_bet (integer)
	 */
	public int getMax_bet(){
		return max_bet;
	}
	/**
	 * Simple get of the balance
	 * @return returns balance (integer)
	 */
	public int getBalance(){
		return balance;
	}
	
}
