package blackjack;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Deck deck = new Deck(1);
		deck.printDeck();
		Junk junk = new Junk();
		Player player = new Player(deck,junk,1000);
		Dealer dealer = new Dealer(deck, junk);
		Scanner sc=new Scanner(System.in);
		Strategy strategy = new Strategy(player.getBalance());
		Game game = new Game(deck, junk, player, dealer, strategy);
		
		System.out.println("Type what you want to do? (bet/exit)");
		deck.shuffle();
		
		while(true){

			String in = sc.nextLine();
			
			//Add junk to deck when deck is at ?%
			if(!game.ingame()&&game.getPercentageDeck()<90){
				deck.printDeck();
				deck.addJunk(junk);
				junk.emptyJunk();
				deck.shuffle();
			}
			if(in.equals("b")&&(!game.ingame())){
				System.out.println("How much you want to bet?");
				try{
					game.makeBet(Double.parseDouble(sc.nextLine()));
				}catch(Exception name){
					System.out.println("Invalid argument");
				}
				
			}else if(in.equals("d")&&(!game.ingame())){
				game.deal();
			}
			else if(in.equals("h")&&game.ingame()){
				game.hit();
			}
			else if(in.equals("s")&&game.ingame()){
				game.stand();
			}
			else if(in.equals("2")&&game.ingame()){
				game.dowbleDown();
			}
			else if(in.equals("st")){
				strategy.printStats(player.getBalance());
			}
			else if(in.equals("i")&&game.ingame()&&dealer.canHaveBlackjack()){
				game.insurance();
			}
			
			
			//surrender
			else if(in.equals("u")&&game.ingame()){
				System.out.println("Not available yet");
			}
			
			
			//Split
			else if(in.equals("p")&&game.ingame()&&player.getHand().cardsSameValue()){
				game.split();
				//preciso ver as bets e o dinheiro ainda
				
			}

			else if(in.equals("q")){
				sc.close();
				return;
			}
			else if(in.equals("$")){
				System.out.println("Player current balance is "+player.getBalance());
			}
			else{
				System.out.println("Illegal command");
			}
		}
	}
}
