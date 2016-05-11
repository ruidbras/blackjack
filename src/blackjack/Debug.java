package blackjack;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Debug  extends Mode{
	
	FileReader cmd = null;
	BufferedReader rCmd = null;
	String readshoepath = null;
	String in;
	
	public Debug(String args[]){
		super(args);
		String current = System.getProperty("user.dir");
		readshoepath = current + args[4];
		try {
			cmd = new FileReader(current + args[5]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rCmd =  new BufferedReader(cmd);
	}
	
	public String getReadshoepath(){
		return readshoepath;
	}

	public String getInstruction() throws IOException {
		int c;
		String bet = "";
		if((c=rCmd.read())!=-1){
			if((char)c != ' ' && c != 13 && (char)c != '\n'){
				if((char)c == 'b'){
					if((char)(c = rCmd.read())==' '){
						
						rCmd.mark(1);
						while((char)(c = rCmd.read())!=' '){
							
							if(!Character.isDigit((char)c)){
								rCmd.reset();
								return in="b";
							} //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
							
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
		return in;
		}
		return "q";
	}
}