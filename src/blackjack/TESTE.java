package blackjack;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TESTE {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String current = System.getProperty("user.dir");
		File cmd = new File(current + "/src/teste.txt");
		FileReader rCmd =  new FileReader(cmd);
		int c;
		String bet = "";
		while((c=rCmd.read())!=-1){
			if((char)c != ' '){
				if((char)c == 'b'){
					if((char)(c = rCmd.read())==' '){
						while((char)(c = rCmd.read())!=' '){
							bet+=(char)c;
						}
						System.out.print("b "+bet+" ");
					}
				}else if((char)c == 'a' && (char)(rCmd.read())=='d'){
					System.out.print("ad ");
				}else if((char)c == 's' && (char)(rCmd.read())=='t'){
					System.out.print("st ");
				}else{
					System.out.print((char)c+" ");
				}
			}
		}
		rCmd.close();
		
	
		
	}

}
