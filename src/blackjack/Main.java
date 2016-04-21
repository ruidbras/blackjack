package blackjack;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Deck deck = new Deck(4);
		Junk junk = new Junk();
		Player player = new Player(deck,junk,1000);
		Dealer dealer = new Dealer(deck, junk);
		Scanner sc=new Scanner(System.in);
		int estado=0;
		
		System.out.println("Type what you want to do? (bet/exit)");
		deck.shuffle();
		
		while(true){
			
			System.out.println(player.getBalance());
			
			if(player.hand.genTotal()>21){
				System.out.println("Bust!");
				System.out.println("Dealers Hand: "+dealer.hand);
				dealer.hand.cleanHand();
				player.hand.cleanHand();
				deck.addJunk(junk);
				junk.emptyJunk();
				deck.shuffle();
				estado=0;
			}
			
			String in = sc.nextLine();
						
			if(in.equals("bet")&&estado==0){
				deck.shuffle();
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
						junk.emptyJunk();
						deck.shuffle();
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
				}
			}
			
			else if(in.equals("hit")&&estado!=0){
				player.hit();
				System.out.println("Your hand: "+ player.hand);
			}
			else if(in.equals("stand")&&estado!=0){
				dealer.finalize();
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
				estado=0;
			}
			else if(in.equals("double")&&estado==1){
				player.doubleDown();
				System.out.println("Your hand: "+ player.hand);
				estado=2;
			}
			else if(in.equals("exit")){
				sc.close();
				return;
			}else{
				System.out.println("Comando inválido");
			}
			
			
		}
	}
}
