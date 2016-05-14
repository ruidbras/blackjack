package blackjack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
// TODO: Auto-generated Javadoc
/**
 * This class extends an abstract class "Mode". Is is used when the program is
 * supposed to get the instructions based only on the advices provided by the
 * program.
 * It receives a value n_decks that indicates the number of decks used, a value
 * shuffle that indicates how much of the shoe is used (in %) until it adds the
 * missing cards and shuffles and a s_number that indicates the number of shuffles
 * the program should do when playing until the termination of the game.
 * @author Pedro Esteves, Ricardo Cristino, Rui Brás
 * @version 1.0
 */
public class Simulation extends Mode{
	
	/** The n_decks. */
	int n_decks;
	
	/** The shuffle. */
	int shuffle;
	
	/** The s_number. */
	int s_number;
	
	/** The strat. */
	boolean strat = false;
	
	/** The af. */
	boolean af = false;
	
	/** The str. */
	Strategy str;
	
	/** The shoe. */
	Shoe shoe;
	
	/** The game. */
	GameType game;
	
	/** The player. */
	Player player;
	
	/** The dealer. */
	Dealer dealer;
	
	/** The statistics. */
	Statistics statistics;
	
	/** The in. */
	String in;
	
	/**
	 * This constructor invokes the super constructor and it gives values to
	 * the n_decks, shuffle and s_number parameters. It also receives a String
	 * which determine the strategies used by the simulation(booleans strat and af).
	 * @param args arguments typed on the command line
	 */
	public Simulation(String[] args) {
		super(args);
		
		n_decks = Integer.parseInt(args[4]);
		shuffle = Integer.parseInt(args[5]);
		s_number = Integer.parseInt(args[6]);
		
		Pattern pattern = Pattern.compile("-");
		Matcher matcher = pattern.matcher(args[7]);
		String st;
		if(matcher.find()){
			st = args[7].substring(0,matcher.start());
			if(args[7].substring(matcher.end()).equals("AF")){
				af=true;
			}else{
				System.out.println("Error in strategy argument");
			}
		}else{
			st=args[7];
		}
		if(st.equals("BS")){
			strat = true;
		}else if(st.equals("HL")){
			strat = false;
		}else{
			
			System.out.println("Error in strategy argument");
			return;
		}
		
	}
	/**
	 * This method is invoked to give value to parameters (objects) used by
	 * this class. This method must be invoked before using the "getInstruction"
	 * method for a proper used of the game.
	 * @param str Strategy class used
	 * @param shoea Shoe class used
	 * @param game2 Game class used
	 * @param playera Player class used
	 * @param dealera Dealer class used
	 * @param statistics Statistics class used
	 */
	public void setObj(Strategy str, Shoe shoea, GameType game2, Player playera, Dealer dealera, Statistics statistics){
		this.str = str;
		shoe = shoea;
		game = game2;
		player = playera;
		dealer = dealera;
		this.statistics = statistics;
	}
	/**
	 * This method returns a String that results from the output generated
	 * by the advice given by Strategy class.
	 * @return returns String with the next instruction
	 */
	public String getInstruction(){
		
			if(game.ingame()){
				/* Give advice on the next play */
				
				in =Character.toString(str.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), strat));
				if((str.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), strat))=='d'){
					in="2";
					if(!player.getHand().canDouble()){
						if(player.getHand().getTotal()==18){
							in="s";
						}else{
							in="h";
						}
					}
					if(player.getBalance()<player.getBet()){
						System.out.println("[!]Not enough credits");
						in = "q";
					}
				}
				if(!strat){
					if(str.getCount()>=3&&game.ingame()&&dealer.canHaveBlackjack()&&game.firstplay()&&(!game.insuranceMode())&&(!game.wasASplit())){
						in="i";
						if(player.getBalance()<player.getBet()){
							System.out.println("[!]Not enough credits");
							in = "q";
						}
					}
				}
			}else{
				/* Give advice on the next bet */
					if(af){
						if(str.getAfcount()>=2){
							in="b "+player.getOldbet()*2;
							if(player.getOldbet()*2>max_bet){
								in = "b " + max_bet;
							}else if(player.getOldbet()*2<min_bet){
								in = "b " + min_bet;
							}
							if(player.getBalance()<player.getOldbet()*2){
								System.out.println("[!]Not enough credits");
								in = "q";
							}
						}else{
							in="b "+min_bet;
							if(player.getBalance()<min_bet){
								System.out.println("[!]Not enough credits");
								in = "q";
							}
						}
					}else{
						in="b "+statistics.getBet(player.getOldbet());
						if(statistics.getBet(player.getOldbet())>max_bet){
							in = "b " + max_bet;
						}else if(statistics.getBet(player.getOldbet())<min_bet){
							in = "b " + min_bet;
						}
						if(player.getBalance()<statistics.getBet(player.getOldbet())){
							System.out.println("[!]Not enough credits");
							in = "q";
						}
					}
			}
		
		return in;
	}
	
	/**
	 * Simple get of the shoe value.
	 *
	 * @return returns n_decks (integer)
	 */
	public int getShoe(){
		return n_decks;
	}
	
	/**
	 * Simple get of the shuffle value.
	 *
	 * @return returns shuffle (integer)
	 */
	public int getShuffle(){
		return shuffle;
	}
	
	/**
	 * Simple get of the number of shuffles value.
	 *
	 * @return returns s_number (integer)
	 */
	public int getS_number(){
		return s_number;
	}
	
}
