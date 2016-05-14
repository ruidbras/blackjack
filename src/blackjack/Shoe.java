package blackjack;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;


/**This class inherits CollectionOfCards and has a parameter shufflecount that stores the number of shuffles performed to the list of cards
 * during the game.
 * 
 * @author Pedro Esteves, Ricardo Cristino, Rui Brás
 * @version 1.0
 */
public class Shoe extends CollectionOfCards{
	
	private int shufflecount = -1;
	
	/**Creates a shoe with a number of n_decks decks.
	 * 
	 * @param n_decks
	 */
	public Shoe(int n_decks){
		int j=1,w=0;
		for(int i=0;i<n_decks*52;i++){
			if(i==52+52*w){
				j=1;
				w++;
				if(w==n_decks){
					return;
				}
			}
			if(i<13+52*w){
				if(i==52*w){
					getCards().add(new Card("A","D"));
					j++;
				}else if(i==10+52*w){
					getCards().add(new Card("J","D"));
					j++;
				}else if(i==11+52*w){
					getCards().add(new Card("Q","D"));
					j++;
				}else if(i==12+52*w){
					getCards().add(new Card("K","D"));
					j++;
				}else{
					getCards().add(new Card(String.valueOf(j++),"D"));
				}
			}
			if(i==13+52*w){
				j=1;
			}
			if(i>=13+52*w && i< 26+52*w){
				if(i==13+52*w){
					getCards().add(new Card("A","S"));
					j++;
				}else if(i==23+52*w){
					getCards().add(new Card("J","S"));
					j++;
				}else if(i==24+52*w){
					getCards().add(new Card("Q","S"));
					j++;
				}else if(i==25+52*w){
					getCards().add(new Card("K","S"));
					j++;
				}else{
					getCards().add(new Card(String.valueOf(j++),"S"));
				}
			}
			if(i==26+52*w){
				j=1;
			}
			if(i>=26+52*w && i< 39+52*w){
				if(i==26+52*w){
					getCards().add(new Card("A","C"));
					j++;
				}else if(i==36+52*w){
					getCards().add(new Card("J","C"));
					j++;
				}else if(i==37+52*w){
					getCards().add(new Card("Q","C"));
					j++;
				}else if(i==38+52*w){
					getCards().add(new Card("K","C"));
					j++;
				}else{
					getCards().add(new Card(String.valueOf(j++),"C"));
				}
			}
			if(i==39+52*w){
				j=1;
			}
			if(i>=39+52*w && i< 52+52*w){
				if(i==39+52*w){
					getCards().add(new Card("A","H"));
					j++;
				}else if(i==49+52*w){
					getCards().add(new Card("J","H"));
					j++;
				}else if(i==50+52*w){
					getCards().add(new Card("Q","H"));
					j++;
				}else if(i==51+52*w){
					getCards().add(new Card("K","H"));
					j++;
				}else{
					getCards().add(new Card(String.valueOf(j++),"H"));
				}
			}
		}
		
	}
	
	/**Receives as input the path of the txt file, containing the number and suit of all the cards to store in the shoe.
	 * 
	 * @param path
	 * @throws IOException
	 */
	public Shoe(String path) throws IOException{
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
								getCards().add(new Card(value, suit));
							}
						}else{
							suit = "" + (char)c;
							//System.out.println(value + " " + suit);
							getCards().add(new Card(value, suit));
						}
					}
				}else{
					value = "" + (char)c;
					if((c=rShoereader.read())!=-1){
						suit = "" + (char)c;
						getCards().add(new Card(value, suit));
					}
				}
			}
		}
		rShoereader.close();
	}
	
	/**Returns shufflecount.
	 * 
	 * @return
	 */
	public int getShufflecount(){
		return shufflecount;
	}
	
	/**Shuffles the list of cards with the method Collections.shuffle().
	 * 
	 */
	public void shuffle(){
		Collections.shuffle(getCards());
		++shufflecount;
	}
	
	/**Returns the first card of the list of cards and removes it from the list, shifting all elements for the left.
	 * If the list of cards is empty, the method returns null.
	 * 
	 * @return
	 */
	protected Card dealCard(){
		Card temp;
		if(getCards().size()>0){
			temp = getCards().get(0);
			getCards().remove(0);
			return temp;
		}
		return null;
	}
}