package blackjack;

import java.io.IOException;
import java.util.Scanner;


public class Main {
	
	
	
	
	public static void main(String[] args)  throws IOException {
		
		
		int min_bet=0;
		int max_bet=0;
		double balance=0;
		int shoe=0;
		int shuffle=0;
		int s_number=0;
		boolean interactivemode=false;
		String mode = args[0];
		
		/* Read arguments */
		if(mode.equals("-i")){
				min_bet = Integer.parseInt(args[1]);
				max_bet = Integer.parseInt(args[2]);
				balance = Integer.parseInt(args[3]);
				shoe = Integer.parseInt(args[4]);
				shuffle = Integer.parseInt(args[5]);
				interactivemode = true;
		}else if(mode.equals("-d")){
				min_bet = Integer.parseInt(args[1]);
				max_bet = Integer.parseInt(args[2]);
				balance = Integer.parseInt(args[3]);
				//shoe = Integer.parseInt(args[4]);
				//shuffle = Integer.parseInt(args[5]);
		}else if(mode.equals("-s")){	
				min_bet = Integer.parseInt(args[1]);
				max_bet = Integer.parseInt(args[2]);
				balance = Integer.parseInt(args[3]);
				shoe = Integer.parseInt(args[4]);
				shuffle = Integer.parseInt(args[5]);
				s_number = Integer.parseInt(args[6]);
		}else{
				System.out.println("Error in arguments");
				return;
		}
		
		
		BS bs = new BS(3);
		Deck deck = new Deck(shoe, bs);
		deck.printDeck();
		Junk junk = new Junk();
		Player player = new Player(deck,junk, balance);
		Dealer dealer = new Dealer(deck, junk);
		Scanner sc=new Scanner(System.in);
		/* os inputs de srategy é suposto terem a aposta min e max */
		Strategy strategy = new Strategy(player.getBalance(), min_bet, max_bet);
		Game game = new Game(deck, junk, player, dealer, strategy);
		game.checkInputs(min_bet, max_bet, balance, shoe, shuffle);
		
		System.out.println("Type what you want to do? (bet/exit)");
		deck.shuffle(); // Há necessidade ?
		
		
		
		
		while(true){
			String in = null;
			/* Funcionamento */
			if(mode.equals("-i")){
				in = sc.nextLine();
			}else if(mode.equals("-d")){
				
			}else if(mode.equals("-s")){	
				
			}else{
				sc.close();
				return;
			}
			
			
			//Add junk to deck when deck is at ?%
			if(!game.ingame()&&game.getPercentageDeck()<shuffle){
				//deck.printDeck();
				deck.addJunk(junk);
				System.out.println(deck.countCards());
				junk.emptyJunk();
				deck.shuffle();
			}
			if(in.charAt(0)=='b'&&(!game.ingame())){
				try{
					double b = Double.parseDouble(in.substring(in.indexOf(" ")));
					if(game.betLimit(b, min_bet, max_bet)){	
						game.makeBet(b);
					}else{
						System.out.println("Invalid bet. Your bet must be within the range "+min_bet+" and "+max_bet);
					}
				}catch(Exception name){
					System.out.println("Invalid argument");
				}
			}else if(in.equals("d")&&(!game.ingame())){
				game.deal(min_bet);
			}
			else if(in.equals("h")&&game.ingame()){
				game.hit();
			}
			else if(in.equals("s")&&game.ingame()){
				game.stand();
			}
			else if(in.equals("2")&&game.ingame()&&game.firstplay()){
				game.dowbleDown();
			}
			else if(in.equals("st")){
				strategy.printStats(player.getBalance());
			}
			else if(in.equals("ad")){
				boolean af = true; //if true use af if not use basic strategy
				boolean strat;
				strat = false; // true for basic strategy and false for Hi-lo strategy
				if(game.ingame()){
					/* Give advice on the next play */
					System.out.println(bs.advice(player.getHand().getTotal(), dealer.getHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().isPair(), game.firstplay(), strat));
				}else{
					/* Give advice on the next bet */
					System.out.println("Ilustrativo.. oldbet = "+game.getOldbet());
					if(af){
						if(bs.getAfcount()>=2){
							System.out.println("b "+game.getOldbet()*2);
						}else{
							System.out.println("b "+min_bet);
						}
					}else{
						System.out.println("b " +strategy.getBet());
					}
				}
			}
			else if(in.equals("i")&&game.ingame()&&dealer.canHaveBlackjack()&&game.firstplay()&&(!game.insuranceMode())&&(!game.wasASplit())){
				game.insurance();
			}
			
			//surrender
			else if(in.equals("u")&&game.ingame()&&game.firstplay()){
				System.out.println("Not available yet");
			}
			
			
			//Split
			else if(in.equals("p")&&game.ingame()&&player.getHand().cardsSameValue()){
				game.split();
			}

			else if(in.equals("q")){
				sc.close();
				return;
			}
			else if(in.equals("$")){
				System.out.println("Player current balance is "+player.getBalance());
			}
			else{
				System.out.println(in+": Illegal command");
			}
		}
	}
}
