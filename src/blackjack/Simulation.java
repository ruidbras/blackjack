package blackjack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Simulation extends Mode{
	
	int n_decks;
	int shuffle;
	int s_number;
	boolean strat = false;
	boolean af = false;
	BS bs;
	Shoe shoe;
	Game game;
	Player player;
	Dealer dealer;
	Strategy strategy;
	String in;
	
	
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
	
	public void setObj(BS bsa, Shoe shoea, Game gamea, Player playera, Dealer dealera, Strategy strategya){
		bs = bsa;
		shoe = shoea;
		game = gamea;
		player = playera;
		dealer = dealera;
		strategy = strategya;
	}
	
	public String getInstruction(){
		
			//System.out.println("BS:"+strat+"  HL:"+(!strat)+"  AF:"+af);
			if(game.ingame()){
				/* Give advice on the next play */
				//System.out.println(bs.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), strat));
				in =Character.toString(bs.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), strat));
				if((bs.advice(player.getHand().getTotal(), dealer.getDealerHand().getFirst().getHardValue(), player.getHand().isSoft(), player.getHand().cardsSameValue(), game.firstplay(), strat))=='d'){
					in="2";
					if(!player.getHand().worthForDouble()){
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
					if(bs.getCount()>=3&&game.ingame()&&dealer.canHaveBlackjack()&&game.firstplay()&&(!game.insuranceMode())&&(!game.wasASplit())){
						in="i";
						if(player.getBalance()<player.getBet()){
							System.out.println("[!]Not enough credits");
							in = "q";
						}
					}
				}
			}else{
				/* Give advice on the next bet */
				//System.out.println("Ilustrativo.. oldbet = "+game.getOldbet());
					if(af){
						if(bs.getAfcount()>=2){
							//System.out.println("b "+player.getOldbet()*2);
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
							//System.out.println("b "+min_bet);
							in="b "+min_bet;
							if(player.getBalance()<min_bet){
								System.out.println("[!]Not enough credits");
								in = "q";
							}
						}
					}else{
						//System.out.println("b " +strategy.getBet(player.getOldbet()));
						in="b "+strategy.getBet(player.getOldbet());
						if(strategy.getBet(player.getOldbet())>max_bet){
							in = "b " + max_bet;
						}else if(strategy.getBet(player.getOldbet())<min_bet){
							in = "b " + min_bet;
						}
						if(player.getBalance()<strategy.getBet(player.getOldbet())){
							System.out.println("[!]Not enough credits");
							in = "q";
						}
					}
			}
		
		return in;
	}
	
	public int getShoe(){
		return n_decks;
	}
	
	public int getShuffle(){
		return shuffle;
	}
	
	public int getS_number(){
		return s_number;
	}
	
}
