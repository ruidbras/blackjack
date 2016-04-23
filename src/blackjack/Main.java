package blackjack;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Deck deck = new Deck(4);
		Junk junk = new Junk();
		Player player = new Player(deck,junk,1000);
		Dealer dealer = new Dealer(deck, junk);
		Scanner sc=new Scanner(System.in);
		Game game = new Game(player, dealer);
		int estado=0;
		
		System.out.println("Type what you want to do? (bet/exit)");
		deck.shuffle();
		
		while(true){
			
			//System.out.println(player.getBalance());
			
			/*if(player.hand.genTotal()>21){
				System.out.println("Bust!");
				System.out.println("Dealers Hand: "+dealer.hand);
				dealer.hand.cleanHand();
				player.hand.cleanHand();
				deck.addJunk(junk);
				junk.emptyJunk();
				deck.shuffle();
				estado=0;
			}*/
			
			String in = sc.nextLine();
						
			if(in.equals("b")&&(!game.ingame())){
				System.out.println("How much you want to bet?");
				game.makeBet(Integer.parseInt(sc.nextLine()));
				/*deck.shuffle(); //Delete
				System.out.println("How much you want to bet?");
				//if he has enough money
				if(player.bet(Integer.parseInt(sc.nextLine()))){
					dealer.drawHand();
					dealer.printDealersFirstTwo();
					System.out.println("Your hand: "+player.hand);
					if(player.hand.blackjack()){
						System.out.println("Blackjack!");
						System.out.println("Dealers Hand: "+dealer.hand);
						player.setBalance(player.bet*2.5);
						dealer.hand.cleanHand();
						player.hand.cleanHand();
						deck.addJunk(junk);
						junk.emptyJunk(); //Delete
						deck.shuffle(); //Delete
					}
					if(player.hand.cardsEqual()){
						System.out.println("you can split cards");
						estado=2;
					}
					if(dealer.canHaveBlackjack()){
						System.out.println("Can make insurance");
				}
				estado=1;
				}else{
					estado=0;
				}*/
			}else if(in.equals("d")&&(!game.ingame())){
				/*player.hit();
				System.out.println("Your hand: "+ player.hand);*/
				game.deal();
			}
			else if(in.equals("h")&&game.ingame()){
				/*player.hit();
				System.out.println("Your hand: "+ player.hand);*/
				game.hit();
			}
			else if(in.equals("s")&&game.ingame()){
				/*dealer.finalize();
				System.out.println("Dealers Hand: "+dealer.hand);
				if(player.hand.genTotal()>dealer.hand.genTotal()&&dealer.hand.genTotal()<=21){
					System.out.println("you win");
					player.setBalance((player.bet)*2);
				}else if(player.hand.genTotal()==dealer.hand.genTotal()){
					System.out.println("tie");
					player.setBalance(player.bet);
				}else if(dealer.hand.genTotal()>21){
					System.out.println("you win");
					player.setBalance((player.bet)*2);
				}
				else{
					System.out.println("you lose");
				}
				dealer.hand.cleanHand();
				player.hand.cleanHand();
				deck.addJunk(junk);
				junk.emptyJunk();
				deck.shuffle();
				estado=0;*/
				game.stand();
			}
			else if(in.equals("double")&&estado==1){
				/*player.doubleDown();
				System.out.println("Your hand: "+ player.hand);
				estado=2;*/
				System.out.println("Not available yet");
			}
			else if(in.equals("exit")){
				sc.close();
				return;
			}
			else if(in.equals("$")){
				System.out.println("Player current balance is "+player.getBalance());
			}
			else{
				System.out.println("Comando inválido");
			}
			
			
		}
	}
}
