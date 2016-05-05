package blackjack;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public class Deck extends CollectionOfCards{
	
	
	Card temp; 
	
	Deck(int shoe){
		int j=1,w=0;
		for(int i=0;i<shoe*52;i++){
			if(i==52+52*w){
				j=1;
				w++;
				if(w==shoe){
					return;
				}
			}
			if(i<13+52*w){
				if(i==52*w){
					cards.add(new Card("A","D"));
					j++;
				}else if(i==10+52*w){
					cards.add(new Card("J","D"));
					j++;
				}else if(i==11+52*w){
					cards.add(new Card("Q","D"));
					j++;
				}else if(i==12+52*w){
					cards.add(new Card("K","D"));
					j++;
				}else{
					cards.add(new Card(String.valueOf(j++),"D"));
				}
			}
			if(i==13+52*w){
				j=1;
			}
			if(i>=13+52*w && i< 26+52*w){
				if(i==13+52*w){
					cards.add(new Card("A","S"));
					j++;
				}else if(i==23+52*w){
					cards.add(new Card("J","S"));
					j++;
				}else if(i==24+52*w){
					cards.add(new Card("Q","S"));
					j++;
				}else if(i==25+52*w){
					cards.add(new Card("K","S"));
					j++;
				}else{
					cards.add(new Card(String.valueOf(j++),"S"));
				}
			}
			if(i==26+52*w){
				j=1;
			}
			if(i>=26+52*w && i< 39+52*w){
				if(i==26+52*w){
					cards.add(new Card("A","C"));
					j++;
				}else if(i==36+52*w){
					cards.add(new Card("J","C"));
					j++;
				}else if(i==37+52*w){
					cards.add(new Card("Q","C"));
					j++;
				}else if(i==38+52*w){
					cards.add(new Card("K","C"));
					j++;
				}else{
					cards.add(new Card(String.valueOf(j++),"C"));
				}
			}
			if(i==39+52*w){
				j=1;
			}
			if(i>=39+52*w && i< 52+52*w){
				if(i==39+52*w){
					cards.add(new Card("A","H"));
					j++;
				}else if(i==49+52*w){
					cards.add(new Card("J","H"));
					j++;
				}else if(i==50+52*w){
					cards.add(new Card("Q","H"));
					j++;
				}else if(i==51+52*w){
					cards.add(new Card("K","H"));
					j++;
				}else{
					cards.add(new Card(String.valueOf(j++),"H"));
				}
			}
		}
		
	}
	
	Deck(String path) throws IOException{
		int c=0;
		String value;
		String suit;
		File shoereader = new File(path);
		FileReader rShoereader = new FileReader(shoereader);
		while((c=rShoereader.read())!=-1){
			if((char)c != ' ' && c != 13 && (char)c != '\n'){
				if(Character.isDigit((char)c)){
					value = "" + (char)c;
					if((c=rShoereader.read())!=-1){
						if(Character.isDigit((char)c)){
							value += (char)c;
							if((c=rShoereader.read())!=-1){
								suit = "" + (char)c;
								//System.out.println(value + " " + suit);
								cards.add(new Card(value, suit));
							}
						}else{
							suit = "" + (char)c;
							//System.out.println(value + " " + suit);
							cards.add(new Card(value, suit));
						}
					}
				}else{
					value = "" + (char)c;
					if((c=rShoereader.read())!=-1){
						suit = "" + (char)c;
						cards.add(new Card(value, suit));
					}
				}
			}
		}
		rShoereader.close();
	}
	
	
	
	
	public void shuffle(){
		System.out.println("Shuffling the shoe...");
		Collections.shuffle(cards);
	}
	
	public Card dealCard(){
		if(cards.size()>0){
			temp = cards.get(0);
			cards.remove(0);
			return temp;
		}
		return null;
	}
}
