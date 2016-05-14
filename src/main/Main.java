package main;

import java.io.IOException;

import blackjack.Dealer;
import blackjack.Debug;
import blackjack.Game;
import blackjack.GameSimulation;
import blackjack.GameType;
import blackjack.Interactive;
import blackjack.Junk;
import blackjack.Mode;
import blackjack.Player;
import blackjack.Shoe;
import blackjack.Simulation;
import blackjack.Statistics;
import blackjack.Strategy;
import graphical.App;

/**
 * This class is the main and it associate all the classes and algorithms needed
 * to implement the blackjack game.
 * @author Pedro Esteves, Ricardo Cristino, Rui Bras
 * @version 1.0
 */
public class Main {
	
	public static void main(String[] args)  throws IOException {
		
		boolean interactive = false;
		boolean debug = false;
		boolean simulation = false;
		String mode_type = args[0];
		Mode mode;
		Shoe shoe ;
		Strategy str;
		Junk junk;
		Player player;
		Dealer dealer;
		Statistics statistics;
		GameType game;
		String in;
		/**
		 * The first argument is read and stored in mode_type, indicating the mode
		 * that the player intends to use. Based on the different modes the game
		 * does different initializations like the object mode. 
		 */
		if(mode_type.equals("-i")){
				mode = new Interactive(args);
				str = new Strategy(((Interactive) mode).getShoe());
				shoe = new Shoe(((Interactive) mode).getShoe());
				shoe.shuffle();
				interactive = true;
		}else if(mode_type.equals("-d")){
				mode = new Debug(args);
				shoe = new Shoe(((Debug) mode).getReadshoepath());
				str = new Strategy((int) (Math.round(shoe.countCards()/52)));
				debug = true;
		}else if(mode_type.equals("-s")){
				mode = new Simulation(args);
				str = new Strategy(((Simulation) mode).getShoe());
				shoe = new Shoe(((Simulation) mode).getShoe());
				shoe.shuffle();
				simulation = true;
		}else{
				System.out.println("Error in arguments");
				return;
		}
		/**
		 * Makes some initializations common to all modes.
		 */
		junk = new Junk();
		player = new Player(mode.getBalance());
		dealer = new Dealer(shoe, junk);
		statistics = new Statistics(mode.getBalance(), mode.getMin_bet(), mode.getMax_bet());
		
		/**
		 * Based on the mode it creates a new Game or a new GameSimulation and it does different checks on the inputs.
		 */
		if(interactive){
			game = new Game(shoe, junk, player, dealer, statistics, str);
			game.checkInputs(mode.getMin_bet(), mode.getMax_bet(), mode.getBalance(), ((Interactive) mode).getShoe(), ((Interactive) mode).getShuffle(), false);
		}else if(debug){
			game = new Game(shoe, junk, player, dealer, statistics, str);
			game.checkInputs(mode.getMin_bet(), mode.getMax_bet(), mode.getBalance(), 0, 0, true);
		}else{
			game = new GameSimulation(shoe, junk, player, dealer, statistics, str);
			((Simulation) mode).setObj(str, shoe, game, player, dealer, statistics);
			game.checkInputs(mode.getMin_bet(), mode.getMax_bet(), mode.getBalance(), ((Simulation) mode).getShoe(), ((Simulation) mode).getShuffle(), false);	
		}
		
		/**
		 * Verifies if the user intends to use a graphical interface or not.
		 */
		if(mode_type.equals("-i")&&args[6].equals("-gui")){
			try {
				App window = new App(player, dealer, shoe, junk, game, statistics, str, mode.getMin_bet(), mode.getMax_bet(), mode.getBalance());
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/**
		 * Only prints a welcome message if game is running in debug or interactive modes.
		 */
		if(!simulation) System.out.println("Welcome to Blackjack! Type what you want to do? (bet/exit)");
		
		/**
		 * Cycle where the game occurs. On the beginning of the cycle different verifications are made
		 * based on the type of mode running. Then a instruction is looked for and after the instruction
		 * is found, it runs an "if" module in order to get the right action performed.
		 */
		while(true){
			
			if(!simulation)System.out.println("");
			//Add junk to deck when deck is at ?%
			if(simulation){
				/**
				 * When the game ended a play and the shuffle percentage is reached it
				 * put the cards back on the shoe and shuffles. It also resets all the counts. 
				 */
				if((!game.ingame())&&game.getPercentageDeck()>((Simulation) mode).getShuffle()&&(!debug)){
					shoe.addCards(junk.getCards());
					junk.emptyCards();
					shoe.shuffle();
					statistics.resetBet();
					str.resetCounts();
				}
				/**
				 * When the number of shuffles is reached the program prints the statistics
				 * and quits.
				 */
				if((!game.ingame())&&shoe.getShufflecount()==((Simulation) mode).getS_number()){
					statistics.printStats(player.getBalance());
					return;
				}
			}else if(interactive){
				/**
				 * * When the game ended a play and the shuffle percentage is reached it
				 * put the cards back on the shoe and shuffles. It also resets all the counts.
				 */
				if((!game.ingame())&&game.getPercentageDeck()>((Interactive) mode).getShuffle()&&(!debug)){
					shoe.addCards(junk.getCards());
					junk.emptyCards();
					shoe.shuffle();
					System.out.println("Shuffling the shoe...");
					statistics.resetBet();
					str.resetCounts();
				}
			}
			/**
			 * Gets the next instruction based on the mode running.
			 */
			in = mode.getInstruction();
			/**
			 * "if" module that detects the instruction and performs the respective action. 
			 */
			if(in.charAt(0)=='b'&&(!game.ingame())){
				/**
				 * In this case, there's no value for the bet requested and so the program
				 * assumes the minimum bet.
				 */
				if(in.length()==1){
					game.makeBet(0,mode.getMin_bet());
				}
				else{
					try{
						// Looks for the number after the "b " string.
						double b = Double.parseDouble(in.substring(in.indexOf(" ")));
						if(game.betLimit(b, mode.getMin_bet(), mode.getMax_bet())){	
							game.makeBet(b,mode.getMin_bet());
						}else{
							// If the bet requested doesn't respect the boundaries is displays a warning 
							System.out.println("Invalid bet. Your bet must be within the range "+mode.getMin_bet()+" and "+mode.getMax_bet());
						}
					}catch(Exception name){
						System.out.println("Invalid argument");
					}
					if(simulation){
						// On simulation mode it makes an automatic bet 
						game.deal(mode.getMin_bet());
					}
				
				}
			}else if(in.equals("d")){
				// Initiates the play
				game.deal(mode.getMin_bet());
			}
			else if(in.equals("h")){
				// Player asks a new card for the hand
				game.hit();
			}
			else if(in.equals("s")){
				// Player stands on the current hand
				game.stand();
			}
			else if(in.equals("2")){
				// Player asks to double his current bet
				game.doubleDown();
			}
			else if(in.equals("st")){
				// Prints all the statistics of the game so far
				statistics.printStats(player.getBalance());
			}
			else if(in.equals("ad")){
				// Prints the advice for the current play
				if(game.ingame()){
					/* Give advice on the next play with both strategies*/
					char a=str.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), true);
					System.out.println("Basic Strategy: "+ a);
					a=str.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), false);
					if(str.getCount()>=3&&game.firstplay()&&(!game.insuranceMode()))a='i';
					System.out.println("Hi-lo Strategy: "+ a);
				}else{
					/* Give advice on the next bet */
					if(str.getAfcount()>=2){
						System.out.println("Ace-Five Strategy: b "+player.getOldbet()*2);
					}else{
						System.out.println("Ace-Five Strategy: b "+mode.getMin_bet());
					}
					System.out.println("Normal Strategy: b " +statistics.getBet(player.getOldbet()));
				}
			}
			else if(in.equals("i")){
				// Player asks to place an insurance
				game.insurance();
			}
			else if(in.equals("u")){
				// Player asks to surrender
				game.surrender();
			}
			else if(in.equals("p")){
				// Players asks to make a split on the current hand
				game.split();
			}
			else if(in.equals("q")){
				// Player quits the game
				if(simulation)statistics.printStats(player.getBalance());
				System.out.println("bye");
				return;
			}
			else if(in.equals("$")){
				// Prints the current balance of the player
				System.out.println("Player current balance is "+player.getBalance());
			}
			else if(in.equals(" ")){
				// It doesn't do nothing, its here just to contemplate the possibility of the debug sending " "
			}
			else{
				// Displays warning on every illegal/unknown command
				System.out.println(in+": Illegal command");
			}
		}
	}
}