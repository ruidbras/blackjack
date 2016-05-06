package blackjack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Simulation extends Mode{
	
	int shoe;
	int shuffle;
	int s_number;
	String st;
	boolean strat = false;
	boolean af = false;
	
	public Simulation(String[] args) {
		super(args);
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
	}

}
