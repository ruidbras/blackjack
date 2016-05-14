package main;

import java.io.IOException;

import blackjack.Strategy;
import blackjack.Dealer;
import blackjack.Debug;
import blackjack.Game;
import blackjack.Interactive;
import blackjack.Junk;
import blackjack.Mode;
import blackjack.Player;
import blackjack.Shoe;
import blackjack.Simulation;
import blackjack.Statistics;
import graphical.App;


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
		Game game;
		String in;
		/* Read arguments */
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
		
		junk = new Junk();
		player = new Player(mode.getBalance());
		dealer = new Dealer(shoe, junk);
		statistics = new Statistics(mode.getBalance(), mode.getMin_bet(), mode.getMax_bet());
		game = new Game(shoe, junk, player, dealer, statistics, str);
		
		if(interactive){
			game.checkInputs(mode.getMin_bet(), mode.getMax_bet(), mode.getBalance(), ((Interactive) mode).getShoe(), ((Interactive) mode).getShuffle(), false);
		}else if(debug){
			game.checkInputs(mode.getMin_bet(), mode.getMax_bet(), mode.getBalance(), 0, 0, true);
		}else{
			((Simulation) mode).setObj(str, shoe, game, player, dealer, statistics);
			game.checkInputs(mode.getMin_bet(), mode.getMax_bet(), mode.getBalance(), ((Simulation) mode).getShoe(), ((Simulation) mode).getShuffle(), false);	
		}
		 
		if(mode_type.equals("-i")&&args[6].equals("-gui")){
			try {
				App window = new App(player, dealer, shoe, junk, game, statistics, str, mode.getMin_bet(), mode.getMax_bet(), mode.getBalance());
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(!simulation) System.out.println("Type what you want to do? (bet/exit)");
		
		while(true){
			
			System.out.println("");
			//System.out.println(player.getBalance());
			//System.out.println(shoe.cards.size()+junk.cards.size());
			//System.out.println(shoe.cards.size());
			
			//Add junk to deck when deck is at ?%
			if(simulation){
				if((!game.ingame())&&game.getPercentageDeck()>((Simulation) mode).getShuffle()&&(!debug)){
					shoe.addCards(junk.getCards());
					junk.emptyCards();
					shoe.shuffle();
					statistics.resetBet();
					str.resetCounts();
				}
				if((!game.ingame())&&shoe.getShufflecount()==((Simulation) mode).getS_number()){
					statistics.printStats(player.getBalance());
					return;
				}
			}else if(interactive){
				if((!game.ingame())&&game.getPercentageDeck()>((Interactive) mode).getShuffle()&&(!debug)){
					shoe.addCards(junk.getCards());
					junk.emptyCards();
					shoe.shuffle();
					System.out.println("Shuffling the shoe...");
					statistics.resetBet();
					str.resetCounts();
				}
			}
				
			in = mode.getInstruction();

			if(in.charAt(0)=='b'&&(!game.ingame())){
				
				if(in.length()==1){
					game.makeBet(0,mode.getMin_bet());
				}
				else{
					try{
						double b = Double.parseDouble(in.substring(in.indexOf(" ")));
						if(game.betLimit(b, mode.getMin_bet(), mode.getMax_bet())){	
							game.makeBet(b,mode.getMin_bet());
						}else{
							System.out.println("Invalid bet. Your bet must be within the range "+mode.getMin_bet()+" and "+mode.getMax_bet());
						}
					}catch(Exception name){
						System.out.println("Invalid argument");
					}
					if(simulation){
						game.deal(mode.getMin_bet());
					}
				
				}
			}else if(in.equals("d")){
				game.deal(mode.getMin_bet());
			}
			else if(in.equals("h")){
				game.hit();
			}
			else if(in.equals("s")){
				game.stand();
			}
			else if(in.equals("2")){
				game.doubleDown();
			}
			else if(in.equals("st")){
				statistics.printStats(player.getBalance());
			}
			else if(in.equals("ad")){
				if(game.ingame()){
					/* Give advice on the next play */
					char a=str.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), true);
					System.out.println("Basic Strategy: "+ a);
					a=str.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), false);
					if(str.getCount()>=3&&game.firstplay()&&(!game.insuranceMode()))a='i';
					System.out.println("Hi-lo Strategy: "+ a);
				}else{
					/* Give advice on the next bet */
					//System.out.println("Ilustrativo.. oldbet = "+player.getOldbet());
					if(str.getAfcount()>=2){
						System.out.println("Ace-Five Strategy: b "+player.getOldbet()*2);
					}else{
						System.out.println("Ace-Five Strategy: b "+mode.getMin_bet());
					}
					System.out.println("Normal Strategy: b " +statistics.getBet(player.getOldbet()));
				}
			}
			else if(in.equals("i")){
				game.insurance();
			}
			else if(in.equals("u")){
				game.surrender();
			}
			else if(in.equals("p")){
				game.split();
			}
			else if(in.equals("q")){
				if(simulation)statistics.printStats(player.getBalance());
				System.out.println("bye");
				return;
			}
			else if(in.equals("$")){
				System.out.println("Player current balance is "+player.getBalance());
			}
			else if(in.equals(" ")){
				
			}
			else{
				System.out.println(in+": Illegal command");
			}
		}
	}
}