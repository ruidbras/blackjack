package blackjack;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class BS {
	char[][] hard;
	char[][] pair;
	char[][] soft;
	int count;
	int shoe;
	
	public BS(int shoe) throws IOException {
		int c, i = 0 , j = 0;
		this.shoe = shoe;
		String current = System.getProperty("user.dir");
		System.out.println(current);
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
	
	public void countcards(int c){
		count+=c;
	}
	
	public char getadvice(int l, int c, boolean s, boolean p){
		//l is the total of the Player's hand and c is the total of the first card of the Dealer's hand
		if(p){
			System.out.println("pair" + (l/2-2) + (c-2));
			return pair[l/2-2][c-2];
		}else if(s){
			System.out.println("soft" + (l-13) + (c-2));
			return soft[l-13][c-2];
		}else{
			System.out.println("hard" + (l-5) + (c-2));
			return hard[l-5][c-2];
		}
		
	}
	
	public char HL(int l, int c, boolean s, boolean p){
		int truecount = Math.round(count/shoe);
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
	
	public void printBS(){
		int k, l;
		System.out.println("Matriz Hard");
		for(k=0; k<16; k++){
			for(l=0; l<10; l++){
				System.out.print(hard[k][l]);
			}
			System.out.print('\n');
		}
		System.out.println("Matriz Pair");
		for(k=0; k<10; k++){
			for(l=0; l<10; l++){
				System.out.print(pair[k][l]);
			}
			System.out.print('\n');
		}
		System.out.println("Matriz Soft");
		for(k=0; k<9; k++){
			for(l=0; l<10; l++){
				System.out.print(soft[k][l]);
			}
			System.out.print('\n');
		}
		
	}
	
	
	
	public static void main(String[] args) throws IOException{
		/*int c,i,j;
		j=0;
		i=0;
		char[][] hard = new char[16][10];
		String current = System.getProperty("user.dir");
		System.out.println(current);
		File f = new File(current + "/src/Hard.txt.txt");
		try {
			FileReader r = new FileReader(f);
			
					while((c=r.read())!=-1)
						if((char)c != ' ' && c != 13 && (char)c != '\n' && i < 16){
							if(j>9){
								j=0;
								++i;
							}
							hard[i][j]=(char)c;
							++j;
						}
			
			for(int k=0; k<16; k++){
				for(int l=0; l<10; l++){
					System.out.print(hard[k][l]);
				}
				System.out.print('\n');
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		//BS bs = new BS();
		
		//bs.printBS();
		
	}

	

}
