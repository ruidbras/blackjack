package blackjack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
	
	public static void main(String[] args)  throws IOException {
		
		int shufflecount = 0;
		int min_bet=0;
		int max_bet=0;
		double balance=0;
		int shoe=0;
		int shuffle=0;
		int s_number=0;
		//boolean interactivemode=false;
		String mode = args[0];
		File cmd = null;
		FileReader rCmd = null;
		boolean af = false; //if true use af if not use basic strategy
		boolean strat;
		strat = false; // true for basic strategy and false for Hi-lo strategy
		/* Read arguments */
		if(mode.equals("-i")){
				min_bet = Integer.parseInt(args[1]);
				max_bet = Integer.parseInt(args[2]);
				balance = Integer.parseInt(args[3]);
				shoe = Integer.parseInt(args[4]);
				shuffle = Integer.parseInt(args[5]);
				//interactivemode = true;
		}else if(mode.equals("-d")){
				min_bet = Integer.parseInt(args[1]);
				max_bet = Integer.parseInt(args[2]);
				balance = Integer.parseInt(args[3]);
				shoe = Integer.parseInt(args[4]);
				shuffle = Integer.parseInt(args[5]);
				String current = System.getProperty("user.dir");
				cmd = new File(current + "/src/teste.txt");
				rCmd =  new FileReader(cmd);
		}else if(mode.equals("-s")){
				String st;
				min_bet = Integer.parseInt(args[1]);
				max_bet = Integer.parseInt(args[2]);
				balance = Integer.parseInt(args[3]);
				shoe = Integer.parseInt(args[4]);
				shuffle = Integer.parseInt(args[5]);
				s_number = Integer.parseInt(args[6]);
				Pattern pattern = Pattern.compile("-");
				Matcher matcher = pattern.matcher(args[7]);
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
		}else{
				System.out.println("Error in arguments");
				return;
		}
		
		
		BS bs = new BS(3);
		Deck deck = new Deck(shoe, bs);
		Junk junk = new Junk();
		Player player = new Player(balance);
		Dealer dealer = new Dealer(deck, junk);
		Scanner sc=new Scanner(System.in);
		/* os inputs de srategy é suposto terem a aposta min e max */
		Strategy strategy = new Strategy(player.getBalance(), min_bet, max_bet);
		Game game = new Game(deck, junk, player, dealer, strategy);
		game.checkInputs(min_bet, max_bet, balance, shoe, shuffle);
		
		System.out.println("Type what you want to do? (bet/exit)");
		deck.shuffle();
		
		while(true){
			String in = null;
			
			//Add junk to deck when deck is at ?%
			if((!game.ingame())&&game.getPercentageDeck()>shuffle){
				//deck.printDeck();
				deck.addCards(junk.cards);
				System.out.println(deck.countCards());
				junk.emptyCards();
				deck.shuffle();
				++shufflecount;
				strategy.printStats(player.getBalance());//mudar para final exclusivo ao modo -s
				strategy.resetBet();
				bs.resetCounts();
			}
			
			/* Funcionamento */
			if(mode.equals("-i")){
				in = sc.nextLine();
			}else if(mode.equals("-d")){
				int c;
				String bet = "";
				if((c=rCmd.read())!=-1){
					if((char)c != ' ' && c != 13 && (char)c != '\n'){
						if((char)c == 'b'){
							if((char)(c = rCmd.read())==' '){
								while((char)(c = rCmd.read())!=' '){
									bet+=(char)c;
								}
								System.out.print("b "+bet+" ");
								in = "b "+bet;
							}
						}else if((char)c == 'a' && (char)(rCmd.read())=='d'){
							System.out.print("ad ");
							in = "ad";
						}else if((char)c == 's' && (char)(rCmd.read())=='t'){
							System.out.print("st ");
							in = "st";
						}else{
							System.out.print((char)c+" ");
							in =""+(char)c;
						}
					}else{
						in = " ";
					}
				}else{
					in="q";
					rCmd.close();
				}
			}else if(mode.equals("-s")){	
				if(shufflecount<s_number&&player.getBalance()>0){
					//System.out.println("BS:"+strat+"  HL:"+(!strat)+"  AF:"+af);
					if(game.ingame()){
						/* Give advice on the next play */
						System.out.println(bs.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), strat));
						in =Character.toString(bs.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), strat));
						if((bs.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), strat))=='d'){
							in="2";
							System.out.println("advice 2");
						}
						if(!strat){
							if(bs.getCount()>=3&&game.ingame()&&dealer.canHaveBlackjack()&&game.firstplay()&&(!game.insuranceMode())&&(!game.wasASplit()))
								in="i";
						}
					}else{
						/* Give advice on the next bet */
						//System.out.println("Ilustrativo.. oldbet = "+game.getOldbet());
							if(af){
								if(bs.getAfcount()>=2){
									System.out.println("b "+player.getOldbet()*2);
									in="b "+player.getOldbet()*2;
								}else{
									System.out.println("b "+min_bet);
									in="b "+min_bet;
								}
							}else{
								System.out.println("b " +strategy.getBet());
								in="b "+strategy.getBet();
							}
					}
				}else{
					in="q";
				}
			}else{
				sc.close();
				return;
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
