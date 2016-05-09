package blackjack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Debug  extends Mode{
	
	File cmd = null;
	FileReader rCmd = null;
	String readshoepath = null;
	String in;
	
	public Debug(String args[]){
		super(args);
		String current = System.getProperty("user.dir");
		readshoepath = current + args[4];
		cmd = new File(current + args[5]);
		try {
			rCmd =  new FileReader(cmd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
						while((char)(c = rCmd.read())!=' '){
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