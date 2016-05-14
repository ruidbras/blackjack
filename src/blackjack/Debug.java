package blackjack;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
// TODO: Auto-generated Javadoc
/**
 * This class extends an abstract class "Mode". Is is used when the program is
 * supposed to get the instructions through a file containing all the instructions.
 * It also receives a file path in order to create his costume shoe.
 * @author Pedro Esteves, Ricardo Cristino, Rui Brás
 * @version 1.0
 */
public class Debug  extends Mode{
	
	/** The cmd. */
	FileReader cmd = null;
	
	/** The r cmd. */
	BufferedReader rCmd = null;
	
	/** The readshoepath. */
	private String readshoepath = null;
	
	/** The in. */
	private String in;
	/**
	 * This constructor invokes the super constructor and it gives values to
	 * the Strings with the paths for the instructions and shoe files.
	 * The paths received must come with the full path after the current path
	 * where the jar was ran.
	 * @param args arguments typed on the command line
	 */
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
	
	/**
	 * Simple get of the file for the read of the shoe.
	 *
	 * @return returns String with the shoe file path
	 */
	public String getReadshoepath(){
		return readshoepath;
	}
	
	/**
	 * This method returns a String that results from the read of the instructions
	 * file.
	 *
	 * @return returns String with the next instruction
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
							}
							
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