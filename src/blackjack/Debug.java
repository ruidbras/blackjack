package blackjack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Debug  extends Mode{
	
	File cmd = null;
	FileReader rCmd = null;
	String readshoepath = null;
	
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

}
