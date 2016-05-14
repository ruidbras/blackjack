package blackjack;

import java.util.Scanner;
/**
 * This class extends an abstract class "Mode". Is is used when the program is
 * supposed to get the instructions through an input on the command line.
 * It receives a value shoe that indicates the number of decks used and a value
 * shuffle that indicates how much of the shoe is used (in %) until it adds the
 * missing cards and shuffles.
 * @author Pedro Esteves, Ricardo Cristino, Rui Brás
 * @version 1.0
 */
public class Interactive extends Mode{

	private int shoe;
	private int shuffle;
	Scanner sc;
	private String in;
	/**
	 * This constructor invokes the super constructor and it gives values to
	 * the shoe and shuffle parameters. It also creates a new scanner for the
	 * read of the inputs. The parameters shoe a shuffle are respectively 
	 * argument number 4 and 5.
	 * @param args arguments typed on the command line
	 */
	public Interactive(String[] args) {
		super(args);
		shoe = Integer.parseInt(args[4]);
		shuffle = Integer.parseInt(args[5]);
		sc = new Scanner(System.in);
	}
	
	/**
	 * This method returns a String that results from the input from the command
	 * line.
	 * @return returns String with the next instruction
	 */
	public String getInstruction(){
		System.out.print("#");
		in = sc.nextLine();
		if(in.equals(""))in="void";
		if(in.equals("q"))sc.close();
		return in;
	}
	
	/**
	 * Simple get of the shoe value
	 * @return returns shoe (integer)
	 */
	public int getShoe() {
		return shoe;
	}

	/**
	 * Simple get of the shuffle value
	 * @return returns shuffle (integer)
	 */
	public int getShuffle() {
		return shuffle;
	}

	
}

