package blackjack;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Strategy {
	char[][] hard;
	char[][] pair;
	char[][] soft;
	int count;
	int shoe;
	int afcount;
	
	public Strategy(int shoe) throws IOException {
		int c, i = 0 , j = 0;
		this.shoe = shoe;
		String current = System.getProperty("user.dir");
		File h = new File(current + "/src/Hard.txt.txt");
		try {
			FileReader rh = new FileReader(h);
			hard = new char[17][10];
					while((c=rh.read())!=-1)
						if((char)c != ' ' && c != 13 && (char)c != '\n' && i < 17){
							if(j>9){
								j=0;
								++i;
							}
							hard[i][j]=(char)c;
							++j;
						}
			rh.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Pair Table */
		File p = new File(current + "/src/Pair.txt.txt");
		try {
			FileReader rp = new FileReader(p);
			i = 0;
			j= 0;
			pair = new char[10][10];
					while((c=rp.read())!=-1)
						if((char)c != ' ' && c != 13 && (char)c != '\n' && i < 10){
							if(j>9){
								j=0;
								++i;
							}
							pair[i][j]=(char)c;
							++j;
						}
			rp.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Soft Table */
		File s = new File(current + "/src/Soft.txt.txt");
		try {
			FileReader rs = new FileReader(s);
			i = 0;
			j= 0;
			soft = new char[9][10];
					while((c=rs.read())!=-1)
						if((char)c != ' ' && c != 13 && (char)c != '\n' && i < 9){
							if(j>9){
								j=0;
								++i;
							}
							soft[i][j]=(char)c;
							++j;
						}
			rs.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void countCard(Card card){
		int t = card.getHardValue();
		if(t<7){
			count(1);
		}else if(t>9){
			count(-1);
		}
		if(t==5){
			afcount(1);
		}else if(t==11){
			afcount(-1);
		}
	}
	
	public void count(int c){
		count+=c;
	}
	
	public void afcount(int c){
		afcount+=c;
	}
	
	public int getAfcount(){
		return afcount;
	}
	
	public int getCount(){
		return Math.round(count/shoe);
	}
	
	public void resetCounts(){
		afcount=0;
		count=0;
	}
	
	public char getadvice(int l, int c, boolean s, boolean p){
		//l is the total of the Player's hand and c is the total of the first card of the Dealer's hand
		if(p){
			//System.out.println("pair" + (l/2-2) + (c-2));
			return pair[l/2-2][c-2];
		}else if(s){
			//System.out.println("soft" + (l-13) + (c-2));
			return soft[l-13][c-2];
		}else{
			//System.out.println("hard" + (l-5) + (c-2));
			return hard[l-5][c-2];
		}
		
	}
	
	public char HL(int l, int c, boolean s, boolean p){
		int truecount = Math.round(count/shoe);
		//System.out.println("count: "+count+"    truecount: "+truecount+"   XvsY: "+l+"vs"+c);
		switch(l){
			case 20:
				if(c==5 && p){
					if(truecount >= 5){
						return 'p';
					}else{
						return 's';
					}
				}else if(c==6 && p){
					if(truecount >= 4){
						return 'p';
					}else{
						return 's';
					}
				}else{
					return getadvice(l,c,s,p);
				}
			case 16:
				if(c==10){
					if(truecount >= 0){
						return 's';
					}else{
						return 'h';
					}
				}else if(c==9){
					if(truecount >= 5){
						return 's';
					}else{
						return 'h';
					}
				}else{
					return getadvice(l,c,s,p);
				}
			case 15:
				if(c==10){
					if(truecount >= 0 && truecount < 3){
						return 'u';
					}else if(truecount>3){
						return 's';
					}else{
						return 'h';
					}
				}else if(c==9){
					if(truecount >= 2){
						return 'u';
					}else{
						return 'h';
					}
				}else if(c==11){
					if(truecount >= 1){
						return 'u';
					}else{
						return 'h';
					}
				}else{
					return getadvice(l,c,s,p);
				}
			case 14:
				if(c==10){
					if(truecount >= 3){
						return 'u';
					}else{
						return 'h';
					}
				}else{
					return getadvice(l,c,s,p);
				}
			case 13:
				if(c==2){
					if(truecount >= -1){
						return 's';
					}else{
						return 'h';
					}
				}else if(c==3){
					if(truecount >= -2){
						return 's';
					}else{
						return 'h';
					}
				}else{
					return getadvice(l,c,s,p);
				}
			case 12:
				if(c==2){
					if(truecount >= 3){
						return 's';
					}else{
						return 'h';
					}
				}else if(c==3){
					if(truecount >= 2){
						return 's';
					}else{
						return 'h';
					}
				}else if(c==4){
					if(truecount >= 0){
						return 's';
					}else{
						return 'h';
					}
				}else if(c==5){
					if(truecount >= -2){
						return 's';
					}else{
						return 'h';
					}
				}else if(c==6){
					if(truecount >= -1){
						return 's';
					}else{
						return 'h';
					}
				}else{
					return getadvice(l,c,s,p);
				}
			case 11:
				if(c==11){
					if(truecount >= 1){
						return 'd';
					}else{
						return 'h';
					}
				}else{
					return getadvice(l,c,s,p);
				}
			case 10:
				if(c==11){
					if(truecount >= 4){
						return 'd';
					}else{
						return 'h';
					}
				}else if(c==10){
					if(truecount >= 4){
						return 'd';
					}else{
						return 'h';
					}
				}else{
					return getadvice(l,c,s,p);
				}
			case 9:
				if(c==2){
					if(truecount >= 1){
						return 'd';
					}else{
						return 'h';
					}
				}else if(c==7){
					if(truecount >= 3){
						return 'd';
					}else{
						return 'h';
					}
				}else{
					return getadvice(l,c,s,p);
				}
				
			default:
				return getadvice(l,c,s,p);
		
		}
			
	}
	
	
	public char advice(int l, int c, boolean s, boolean p, boolean firstplay, boolean bs ){
		char r;
		if(bs){
			/* Using the basic strategy  */
			r = getadvice(l,c,s,p);
			/* If it's not the first play and the advice is a special command
			 * the advice must adapt */
			if(!firstplay){
				/* Adaptation for the double command */
				if(r == 'd'){
					if(s){
						if(l>=17){
							r = 'h';
						}else{
							r = 's';
						}
					}else{
						r = 'h';
					}
				}
				/* Adaptation for the surrender command */
				if(r == 'u'){
					r = 'h';
				}
			}
		}else{
			/* Using the Hi-lo strategy */
			r = HL(l,c,s,p);
			/* Every adaptation in this case it's to hit */
			//System.out.println(firstplay);
			if(!firstplay){
				if(r=='p'){
					r = 's';
				}else if(r=='d' || r =='u'){
					r = 'h';
				}
			}
		}
		return r;
	}
}
