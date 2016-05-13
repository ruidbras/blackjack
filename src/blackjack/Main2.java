package blackjack;

import java.io.IOException;


public class Main2 {
	
	public static void main(String[] args)  throws IOException {
		
		boolean interactive = false;
		boolean debug = false;
		boolean simulation = false;
		String mode_type = args[0];
		Mode mode;
		Shoe shoe ;
		BS bs;
		Junk junk;
		Player player;
		Dealer dealer;
		Strategy strategy;
		Game game;
		String in;
		/* Read arguments */
		if(mode_type.equals("-i")){
				mode = new Interactive(args);
				bs = new BS(((Interactive) mode).getShoe());
				shoe = new Shoe(((Interactive) mode).getShoe());
				shoe.shuffle();
				interactive = true;
		}else if(mode_type.equals("-d")){
				mode = new Debug(args);
				shoe = new Shoe(((Debug) mode).getReadshoepath());
				bs = new BS((int) (Math.round(shoe.countCards()/52)));
				debug = true;
		}else if(mode_type.equals("-s")){
				mode = new Simulation(args);
				bs = new BS(((Simulation) mode).getShoe());
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
		strategy = new Strategy(mode.getBalance(), mode.getMin_bet(), mode.getMax_bet());
		game = new Game(shoe, junk, player, dealer, strategy, bs);
		
		if(interactive){
			game.checkInputs(mode.getMin_bet(), mode.getMax_bet(), mode.getBalance(), ((Interactive) mode).getShoe(), ((Interactive) mode).getShuffle(), false);
		}else if(debug){
			game.checkInputs(mode.getMin_bet(), mode.getMax_bet(), mode.getBalance(), 0, 0, true);
		}else{
			((Simulation) mode).setObj(bs, shoe, game, player, dealer, strategy);
			game.checkInputs(mode.getMin_bet(), mode.getMax_bet(), mode.getBalance(), ((Simulation) mode).getShoe(), ((Simulation) mode).getShuffle(), false);	
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
					shoe.addCards(junk.cards);
					junk.emptyCards();
					shoe.shuffle();
					strategy.resetBet();
					bs.resetCounts();
				}
				if((!game.ingame())&&shoe.shufflecount==((Simulation) mode).getS_number()){
					strategy.printStats(player.getBalance());
					return;
				}
			}else if(interactive){
				if((!game.ingame())&&game.getPercentageDeck()>((Interactive) mode).getShuffle()&&(!debug)){
					shoe.addCards(junk.cards);
					junk.emptyCards();
					shoe.shuffle();
					System.out.println("Shuffling the shoe...");
					strategy.resetBet();
					bs.resetCounts();
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
			}else if(in.equals("d")&&(!game.ingame())){
				game.deal(mode.getMin_bet());
			}
			else if(in.equals("h")&&game.ingame()){
				game.hit();
			}
			else if(in.equals("s")&&game.ingame()){
				game.stand();
			}
			else if(in.equals("2")&&game.ingame()&&game.firstplay()){
				game.doubleDown();
			}
			else if(in.equals("st")){
				strategy.printStats(player.getBalance());
			}
			else if(in.equals("ad")){
				if(game.ingame()){
					/* Give advice on the next play */
					char a=bs.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), true);
					System.out.println("Basic Strategy: "+ a);
					a=bs.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), false);
					if(bs.getCount()>=3&&game.firstplay()&&(!game.insuranceMode()))a='i';
					System.out.println("Hi-lo Strategy: "+ a);
				}else{
					/* Give advice on the next bet */
					//System.out.println("Ilustrativo.. oldbet = "+player.getOldbet());
					if(bs.getAfcount()>=2){
						System.out.println("Ace-Five Strategy: b "+player.getOldbet()*2);
					}else{
						System.out.println("Ace-Five Strategy: b "+mode.getMin_bet());
					}
					System.out.println("Normal Strategy: b " +strategy.getBet(player.getOldbet()));
				}
			}
			else if(in.equals("i")&&game.ingame()&&dealer.canHaveBlackjack()&&game.firstplay()&&(!game.insuranceMode())&&(!game.wasASplit())){
				game.insurance();
			}
			else if(in.equals("u")&&game.ingame()&&game.firstplay()){
				game.surrender();
			}
			else if(in.equals("p")&&game.ingame()&&player.getHand().cardsSameValue()){
				game.split();
			}
			else if(in.equals("q")){
				if(simulation)strategy.printStats(player.getBalance());
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
