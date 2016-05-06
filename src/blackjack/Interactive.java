package blackjack;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interactive extends Mode{

	int shoe;
	int shuffle;
	Deck deck;
	BS bs;
	Junk junk;
	Player player;
	Dealer dealer;
	Scanner sc;
	Strategy strategy;
	
	public Interactive(String[] args) {
		super(args);
		shoe = Integer.parseInt(args[4]);
		shuffle = Integer.parseInt(args[5]);
		deck = new Deck(shoe);
		try {
			bs = new BS(shoe);
		} catch (IOException e) {
			e.printStackTrace();
		}
		junk = new Junk();
		player = new Player(balance);
		dealer = new Dealer(deck, junk);
		sc=new Scanner(System.in);
		/* os inputs de srategy é suposto terem a aposta min e max */
		strategy = new Strategy(player.getBalance(), min_bet, max_bet);
		Game game = new Game(deck, junk, player, dealer, strategy, bs);
	}

	@Override
	public void gameMode() {
		
		
		
		game.checkInputs(min_bet, max_bet, balance, shoe, shuffle, false);
		
		deck.shuffle();
		System.out.println("Type what you want to do? (bet/exit)");
		
		while(true){
			String in = null;
			//Add junk to deck when deck is at ?%
			if((!game.ingame())&&game.getPercentageDeck()>shuffle){
				//deck.printDeck();
				deck.addCards(junk.cards);
				junk.emptyCards();
				deck.shuffle();
				strategy.printStats(player.getBalance());//mudar para final exclusivo ao modo -s
				strategy.resetBet();
				bs.resetCounts();
				if(deck.shufflecount==s_number) return;
			}
			

		
			in = sc.nextLine();

	

			
			
			
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
				if(mode.equals("-s")){
					game.deal(min_bet);
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
				game.doubleDown();
			}
			else if(in.equals("st")){
				strategy.printStats(player.getBalance());
			}
			else if(in.equals("ad")){
				if(game.ingame()){
					/* Give advice on the next play */
					char a=bs.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), strat);
					if(!strat){
						if(bs.getCount()>=3&&game.firstplay()&&(!game.insuranceMode()))
							a='i';
					}
					System.out.println(a);
				}else{
					/* Give advice on the next bet */
					System.out.println("Ilustrativo.. oldbet = "+player.getOldbet());
					if(af){
						if(bs.getAfcount()>=2){
							System.out.println("b "+player.getOldbet()*2);
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
				game.surrender();
			}
			
			
			//Split
			else if(in.equals("p")&&game.ingame()&&player.getHand().cardsSameValue()){
				game.split();
			}

			else if(in.equals("q")){
				//if(mode.equals("-d"))
					//rCmd.close();
				sc.close();
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
