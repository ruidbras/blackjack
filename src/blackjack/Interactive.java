package blackjack;

import java.util.Scanner;

public class Interactive extends Mode{

	int shoe;
	int shuffle;
	Scanner sc;
	String in;
	
	public Interactive(String[] args) {
		super(args);
		shoe = Integer.parseInt(args[4]);
		shuffle = Integer.parseInt(args[5]);
		sc = new Scanner(System.in);
	}

	public String getInstruction(){
		in = sc.nextLine();
		if(in.equals("q"))sc.close();
		return in;
	}

	public int getShoe() {
		return shoe;
	}


	public int getShuffle() {
		return shuffle;
	}

	
}
