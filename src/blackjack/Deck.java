package blackjack;

import java.util.LinkedList;
import java.util.Collections;
import java.util.Iterator;

public class Deck {
	
	LinkedList<Card> deck = new LinkedList<Card>();
	int shoe;
	Iterator<Card> it = deck.iterator();
	Card temp; 
	BS bs;
		
	/*public Deck(){
		for(int i = 0; i<50; i++){
			deck.add(new Card("A","D"));
			deck.add(new Card("3","D"));
			deck.add(new Card("A","D"));
			deck.add(new Card("9","D"));
		}
	}*/
	
	/*inicializa o deck com cartas de shoe baralhos*/
	public Deck(int shoe, BS bs){
		this.shoe=shoe;
		this.bs = bs;
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
					deck.add(new Card("A","D"));
					j++;
				}else if(i==10+52*w){
					deck.add(new Card("J","D"));
					j++;
				}else if(i==11+52*w){
					deck.add(new Card("Q","D"));
					j++;
				}else if(i==12+52*w){
					deck.add(new Card("K","D"));
					j++;
				}else{
					deck.add(new Card(String.valueOf(j++),"D"));
				}
			}
			if(i==13+52*w){
				j=1;
			}
			if(i>=13+52*w && i< 26+52*w){
				if(i==13+52*w){
					deck.add(new Card("A","S"));
					j++;
				}else if(i==23+52*w){
					deck.add(new Card("J","S"));
					j++;
				}else if(i==24+52*w){
					deck.add(new Card("Q","S"));
					j++;
				}else if(i==25+52*w){
					deck.add(new Card("K","S"));
					j++;
				}else{
					deck.add(new Card(String.valueOf(j++),"S"));
				}
			}
			if(i==26+52*w){
				j=1;
			}
			if(i>=26+52*w && i< 39+52*w){
				if(i==26+52*w){
					deck.add(new Card("A","C"));
					j++;
				}else if(i==36+52*w){
					deck.add(new Card("J","C"));
					j++;
				}else if(i==37+52*w){
					deck.add(new Card("Q","C"));
					j++;
				}else if(i==38+52*w){
					deck.add(new Card("K","C"));
					j++;
				}else{
					deck.add(new Card(String.valueOf(j++),"C"));
				}
			}
			if(i==39+52*w){
				j=1;
			}
			if(i>=39+52*w && i< 52+52*w){
				if(i==39+52*w){
					deck.add(new Card("A","H"));
					j++;
				}else if(i==49+52*w){
					deck.add(new Card("J","H"));
					j++;
				}else if(i==50+52*w){
					deck.add(new Card("Q","H"));
					j++;
				}else if(i==51+52*w){
					deck.add(new Card("K","H"));
					j++;
				}else{
					deck.add(new Card(String.valueOf(j++),"H"));
				}
			}
		}
	}
	
	public void shuffle(){
		Collections.shuffle(deck);
	}
	
	public void printDeck(){
		for(Card aux:deck){
			System.out.print(aux+" ");
		}
		System.out.println();
	}
	
	public Card dealCard(){
		if(deck.size()>0){
			int t;
			temp = deck.get(0);
			deck.remove(0);
			t=temp.getHardValue();
			if(t<7){
				bs.countcards(1);
			}else if(t>9){
				bs.countcards(-1);
			}
			if(temp.getHardValue()==5){
				bs.afcount(1);
			}else if(temp.getHardValue()==11){
				bs.afcount(-1);
			}
			return temp;
		}
		return null;
	}
	
	public void addJunk(Junk j){
		deck.addAll(j.cards);
	}
	
	public double countCards(){
		return deck.size();
	}
}
