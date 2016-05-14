package graphical;

import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

import blackjack.Strategy;
import blackjack.Card;
import blackjack.Dealer;
import blackjack.Game;
import blackjack.Junk;
import blackjack.Player;
import blackjack.Shoe;
import blackjack.Statistics;

public class App{

	public JFrame frame;
	public JPanel panel= new JPanel();
	private JTextArea screen;
	private JTextArea rules_screen;
	private JLabel lblBalanceValue, jlbNumWhite, jlbNumRed, jlbNumGreen, jlbNumBlack;
	public int min_bet, max_bet;
	public Game g;
	public Junk j;
	public Statistics statistics;
	public Strategy str;
	public Dealer d;
	public Player p;
	public Boolean af, strat;
	public double balance;
	int i=1;
	final Image images = new Image();
	final Color newColor = new Color(0, 102 , 51);
	private JTextField textField;
	public Container layeredPane;
	
	//Create the application.
	public App(Player player, Dealer dealer, Shoe deck, Junk junk, Game game, Statistics statistics, Strategy str, int min_bet, int max_bet, double b) throws IOException {
		initialize(player, dealer, deck, junk, game, statistics, str, min_bet, max_bet, b);
	}

	//Initialize the contents of the frame.
	private void initialize(Player player, Dealer dealer, Shoe deck,Junk junk, Game game, Statistics statistics, Strategy str, int bet_min, int bet_max, double b) throws IOException {
		/////////////////////////////////////////////////////////////////////////////////////////
		//MAIN WINDOW	
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1600, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);  // Don't let the user change the size of the window
		/////////////////////////////////////////////////////////////////////////////////////////
		//PARAMETERS
		min_bet = bet_min;
		max_bet = bet_max;
		balance = b;
		g = game;
		j = junk;
		this.statistics = statistics;
		d = dealer;
		p = player;
		this.str = str;
		///////////////////////////////////////////////////////////////////////////////////////
		//BALANCE LABELS
		jlbNumWhite = new JLabel(Integer.toString(p.getPile().getWhiteStack().getNumberOfChips()));
		jlbNumWhite.setForeground(Color.WHITE);
		jlbNumWhite.setFont(new Font("Arial", Font.BOLD, 16));
		jlbNumWhite.setBounds(335, 740, 311, 23);
		frame.getContentPane().add(jlbNumWhite);

		jlbNumRed = new JLabel(Integer.toString(p.getPile().getRedStack().getNumberOfChips()));
		jlbNumRed.setForeground(Color.WHITE);
		jlbNumRed.setFont(new Font("Arial", Font.BOLD, 16));
		jlbNumRed.setBounds(430, 740, 311, 23);
		frame.getContentPane().add(jlbNumRed);

		jlbNumGreen = new JLabel(Integer.toString(p.getPile().getGreenStack().getNumberOfChips()));
		jlbNumGreen.setForeground(Color.WHITE);
		jlbNumGreen.setFont(new Font("Arial", Font.BOLD, 16));
		jlbNumGreen.setBounds(520, 740, 311, 23);
		frame.getContentPane().add(jlbNumGreen);

		jlbNumBlack = new JLabel(Integer.toString(p.getPile().getBlackStack().getNumberOfChips()));
		jlbNumBlack.setForeground(Color.WHITE);
		jlbNumBlack.setFont(new Font("Arial", Font.BOLD, 16));
		jlbNumBlack.setBounds(615, 740, 311, 23);
		frame.getContentPane().add(jlbNumBlack);

		
		lblBalanceValue = new JLabel(Double.toString(balance));
		lblBalanceValue.setFont(new Font("Arial", Font.PLAIN, 30));
		lblBalanceValue.setForeground(Color.ORANGE);
		lblBalanceValue.setBounds(155, 780, 179, 34);
		frame.getContentPane().add(lblBalanceValue);

		///////////////////////////////////////////////////////////////////////////
		//DONE till now
		screen = makeScreen();
		rules_screen = makeRulesScreen();
		StringBuilder s = new StringBuilder();
		s.append("Important Rules:\n\nYour pile will always have the higher possible chips\nMaximum of 4 hands\n"); 
		s.append("Dealer must stand on 17\n");
		printStringScreen(rules_screen, s.toString());
		makeButton();
		makeLabel();
    	makeImg(290, 760, 380, 108, Color.DARK_GRAY, 53);
		JPanel panel = new JPanel();
		panel.setBounds(34, 11, 740, 668);
		frame.getContentPane().add(panel);
		panel.setBackground(newColor);
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		panel.setLayout(null);
		textField = new JTextField();
		textField.setBounds(34, 726, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		
	}
	
	///////////////////////////////////////////////////////////
	
    private class ActionBet implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	try{
        		if(!g.ingame()){
        			frame.repaint();
        			frame.revalidate();
        			if(g.betLimit(Double.parseDouble(textField.getText()), min_bet, max_bet)){
        				g.makeBet(Double.parseDouble(textField.getText()), min_bet);
        			}else{
        				printStringScreen(screen, "Bet not valid (between the accepted range\n");
        			}
        		}
			}catch(Exception a){
				if(textField.getText().equals("")){
					g.makeBet(0, min_bet);
				}else{
					printStringScreen(screen, "Not a number. Please insert a valid bet or just click on bet\n");
				}
				
			}
        	updateBalance();
        }
    }
    
    private class ActionDeal implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	EventQueue.invokeLater( new Runnable() {
            @Override public void run() {    
                if(!g.ingame()){
            		try{
            			g.deal(Double.parseDouble(textField.getText()));
            		}catch(Exception a){
            			g.deal(min_bet);
            		}
                	if(p.deal()){
			    		handsLabels();
						int space = 0;
		        		for(Card card: g.handPlayer.getCards()){
		        			int k = searchCard(card);
		        			makeImg(170+space, 555, 73, 110, newColor, k);
		        			space += 73;
		        		}
		
			    		space = 0;
			    		int k = searchCard(g.handDealer.getCards().getFirst());
			    		makeImg(230+space, 155, 73, 110, newColor, k);
			    		space += 73;
			    		makeImg(230+space, 155, 73, 110, newColor, 52);
			    		frame.revalidate();
	                	}else{
	                		printStringScreen(screen, "You must bet first\n");
    			
	                	}
                	}
                }
            }); 
        }
    }
    
    private class ActionExit implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
			System.exit(0);
        }
    }
    
    private class ActionClear implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	frame.revalidate();
        	frame.repaint();
        }
    }
    
    private class ActionHit implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	frame.repaint();
        	frame.revalidate();
        	EventQueue.invokeLater( new Runnable() {
                @Override public void run() {   
                if(g.ingame()){
                		g.hit();
               	}
    			int space = 0;
    			int vertical = 0;
	        	handsLabels();
        		for(Card card: g.handPlayer.getCards()){
        			int k = searchCard(card);
        			makeImg(170+space, 555, 73, 110, newColor, k);
        			space += 73;
        		}
        		space = 0;
        		vertical-=100;
        		for(Card card: g.handPlayer2.getCards()){
        			int k = searchCard(card);
        			makeImg(170+space, 555+vertical, 73, 110, newColor, k);
        			space += 73;
        		}
        		space = 0;
        		vertical-=100;
        		for(Card card: g.handPlayer3.getCards()){
        			int k = searchCard(card);
        			makeImg(170+space, 555+vertical, 73, 110, newColor, k);
        			space += 73;
        		}
        		space = 0;
        		vertical-=100;
        		for(Card card: g.handPlayer4.getCards()){
        			int k = searchCard(card);
        			makeImg(170+space, 555+vertical, 73, 110, newColor, k);
        			space += 73;
        		}
    		updateBalance();

    		space = 0;
    
    			if(d.getDealerHand().countCards()==2){
    				int k = searchCard(d.getDealerHand().getFirst());
		    		makeImg(230+space, 155, 73, 110, newColor, k);
		    		space += 73;
		    		makeImg(230+space, 155, 73, 110, newColor, 52);
		    		//frame.revalidate();
    			}else{
    				for(Card card: g.handDealer.getCards()){
    				int k = searchCard(card);
    				makeImg(230+space, 155, 73, 110, newColor, k);
    				space += 73;
    	    		JPanel panel = new JPanel();
    	    		panel.setBackground(newColor);
    				panel.setBounds(40, 155, 180, 30);
    				panel.add(createLabel("Dealer's hand", Color.WHITE, "ARIAL", 16, 130, 155, 180, 30));
    				panel.add(createLabel("("+String.valueOf(g.handDealer.genTotal())+")", Color.WHITE, "ARIAL", 16, 130, 155, 180, 30));
    				frame.getContentPane().add(panel);
    				}
    			}
    			handsLabels();
        		frame.revalidate();
                }   
    	    }); 
        }
    }
    private class ActionStand implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	//frame.repaint();
        	frame.revalidate();
        	EventQueue.invokeLater( new Runnable() {
            @Override public void run() {   
            	if(g.ingame()){
		       		g.stand();
            	}
        		int space = 0;
        		int vertical = 0;
        		
	        	
        		for(Card card: g.handPlayer.getCards()){
        			int k = searchCard(card);
        			makeImg(170+space, 555, 73, 110, newColor, k);
        			space += 73;
        		}
        		space = 0;
        		vertical-=100;
        		for(Card card: g.handPlayer2.getCards()){
        			int k = searchCard(card);
        			makeImg(170+space, 555+vertical, 73, 110, newColor, k);
        			space += 73;
        		}
        		space = 0;
        		vertical-=100;
        		for(Card card: g.handPlayer3.getCards()){
        			int k = searchCard(card);
        			makeImg(170+space, 555+vertical, 73, 110, newColor, k);
        			space += 73;
        		}
        		space = 0;
        		vertical-=100;
        		for(Card card: g.handPlayer4.getCards()){
        			int k = searchCard(card);
        			makeImg(170+space, 555+vertical, 73, 110, newColor, k);
        			space += 73;
        		}
        		updateBalance();
	
        		space = 0;
        		/*for(Card card: g.handDealer.cards){
        			int k = searchCard(card);
        			makeImg(230+space, 155, 73, 110, newColor, k);
        			space += 73;
        		}
	    		JPanel panel = new JPanel();
	    		panel.setBackground(newColor);
				panel.setBounds(30, 155, 200, 30);
				panel.add(createLabel("Dealer's hand", Color.WHITE, "ARIAL", 16, 130, 155, 200, 30));
				panel.add(createLabel("("+String.valueOf(g.handDealer.genTotal())+")", Color.WHITE, "ARIAL", 16, 130, 155, 200, 30));
				frame.getContentPane().add(panel);
				frame.revalidate();*/
        		handsLabels();
				if((g.handPlayer.genTotal()<22&&g.handPlayer2.genTotal()<22&&g.handPlayer3.genTotal()<22&&g.handPlayer4.genTotal()<22)&&d.getDealerHand().countCards()==2){
    				int k = searchCard(d.getDealerHand().getFirst());
		    		makeImg(230+space, 155, 73, 110, newColor, k);
		    		space += 73;
		    		makeImg(230+space, 155, 73, 110, newColor, 52);
		    		//frame.revalidate();
    			}else{
    				for(Card card: g.handDealer.getCards()){
    				int k = searchCard(card);
    				makeImg(230+space, 155, 73, 110, newColor, k);
    				space += 73;
    	    		JPanel panel = new JPanel();
    	    		panel.setBackground(newColor);
    				panel.setBounds(40, 155, 180, 30);
    				panel.add(createLabel("Dealer's hand", Color.WHITE, "ARIAL", 16,130, 155, 180, 30));
    				panel.add(createLabel("("+String.valueOf(g.handDealer.genTotal())+")", Color.WHITE, "ARIAL", 16, 130, 155, 180, 30));
    				frame.getContentPane().add(panel);
    				}
    			}
				
				
				frame.revalidate();
            	}   
	        }); 	
        }
    }
    
    private class ActionSplit implements ActionListener {
		public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	frame.repaint();
        	frame.revalidate();
        	EventQueue.invokeLater( new Runnable() {
        	@Override public void run() {   
        		if(g.ingame()&&p.getHand().cardsSameValue()){
					g.split();
	        	}
        		handsLabels();
        			int space = 0;
	        		int vertical = 0;
	        		
	        		for(Card card: g.handPlayer.getCards()){
	        			int k = searchCard(card);
	        			makeImg(170+space, 555, 73, 110, newColor, k);
	        			space += 73;
	        		}
	        		space = 0;
	        		vertical-=100;
	        		for(Card card: g.handPlayer2.getCards()){
	        			int k = searchCard(card);
	        			makeImg(170+space, 555+vertical, 73, 110, newColor, k);
	        			space += 73;
	        		}
	        		space = 0;
	        		vertical-=100;
	        		for(Card card: g.handPlayer3.getCards()){
	        			int k = searchCard(card);
	        			makeImg(170+space, 555+vertical, 73, 110, newColor, k);
	        			space += 73;
	        		}
	        		space = 0;
	        		vertical-=100;
	        		for(Card card: g.handPlayer4.getCards()){
	        			int k = searchCard(card);
	        			makeImg(170+space, 555+vertical, 73, 110, newColor, k);
	        			space += 73;
	        		}
	        	
		    		space = 0;
		    		int k = searchCard(g.handDealer.getCards().getFirst());
		    		makeImg(230+space, 155, 73, 110, newColor, k);
		    		space += 73;
		   			makeImg(230+space, 155, 73, 110, newColor, 52);			
					
		   			if(g.count_splits>3){
		   				printStringScreen(screen, "Can't split more\n");
		   			}
		   			frame.revalidate();
			}
        }); 
      }
  }
    
    
    private class ActionDoubleDown implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	if(g.ingame()&&g.firstplay()){
        			g.doubleDown();
        	}
        	int space = 0;
    		for(Card card: g.handPlayer.getCards()){
    			int k = searchCard(card);
    			makeImg(170+space, 555, 73, 110, newColor, k);
    			space += 73;
    		}
    		updateBalance();

    		space = 0;
    		for(Card card: g.handDealer.getCards()){
    			int k = searchCard(card);
    			makeImg(230+space, 155, 73, 110, newColor, k);
    			space += 73;
    		}
    		frame.revalidate();
        }
    }

    private class ActionInsurance implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	if(g.ingame()&&d.canHaveBlackjack()&&g.firstplay()&&(!g.insuranceMode())&&(!g.wasASplit())){
				g.insurance();
        	}
        	int space = 0;
    		for(Card card: g.handPlayer.getCards()){
    			int k = searchCard(card);
    			makeImg(170+space, 555, 73, 110, newColor, k);
    			space += 20;
    		}
    		updateBalance();

    		space = 0;
    		for(Card card: g.handDealer.getCards()){
    			int k = searchCard(card);
    			makeImg(230+space, 155, 73, 110, newColor, k);
    			space += 20;
    		}
    		frame.revalidate();
        }
    }
    
    private class ActionSurrender implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	if(g.ingame()&&g.firstplay()){
        		g.surrender();
        		updateBalance();
        		frame.revalidate();
        	}
        }
    }    
    
	
    private class ActionAdvice implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	StringBuilder ps = new StringBuilder();
        	if(g.ingame()){
				/* Give advice on the next play */
				char a=str.advice(p.getHand().getTotal(), d.getDealerHand().getFirst().getHardValue(), p.getHand().isSoft(), p.getHand().cardsSameValue(), g.firstplay(), true);
				ps.append("Basic Strategy: "+ a + "\n");
				a=str.advice(p.getHand().getTotal(), d.getDealerHand().getFirst().getHardValue(), p.getHand().isSoft(), p.getHand().cardsSameValue(), g.firstplay(), false);
				if(str.getCount()>=3&&g.firstplay()&&(!g.insuranceMode()))a='i';
				ps.append("Hi-lo Strategy: "+ a + "\n");
			}else{
				/* Give advice on the next bet */
				//System.out.println("Ilustrativo.. oldbet = "+player.getOldbet());
				if(str.getAfcount()>=2){
					ps.append("Ace-Five Strategy: b "+p.getOldbet()*2 + "\n");
				}else{
					ps.append("Ace-Five Strategy: b "+min_bet + "\n");
				}
				ps.append("Normal Strategy: b " +statistics.getBet(p.getOldbet()) + "\n");
			}
        	printStringScreen(screen, ps.toString());
			frame.revalidate();
        }
    }

    private class ActionStatistics implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	System.out.println("BJ P/D	:	"+statistics.playerbj/statistics.plays+"/"+statistics.dealerbj/statistics.plays);
        	System.out.println("Win		:	"+statistics.wins/statistics.plays);
        	System.out.println("Lose		:	"+statistics.loses/statistics.plays);
        	System.out.println("Push		:	"+statistics.pushes/statistics.plays);
        	System.out.println("Balance		:	"+p.getBalance()+"("+(p.getBalance()-statistics.balanceinit)*100/statistics.balanceinit+"%)");
            
        	
    		StringBuilder s = new StringBuilder();
    		s.append("BJ P/D		:	"+statistics.playerbj/statistics.plays+"/"+statistics.dealerbj/statistics.plays+"\n"); 
    		s.append("Win		:	"+statistics.wins/statistics.plays+"\n");
    		s.append("Lose		:	"+statistics.loses/statistics.plays+"\n");
    		s.append("Push		:	"+statistics.pushes/statistics.plays+"\n");
    		s.append("Balance		:	"+p.getBalance()+"("+(p.getBalance()-statistics.balanceinit)*100/statistics.balanceinit+"%)\n");
    		s.append("-------------------------------------------------------------------------------------------------------------------\n");
    		printStringScreen(screen, s.toString());
        }
    }
	
    public JButton createButton(String label, ActionListener al, int x, int y, int w, int h) {
        JButton button = new JButton(label);
        button.setBounds(x, y, w, h);
        button.addActionListener(al);
        return button;
    }
	
	public JPanel makeButton(){
		JPanel panel = new JPanel();
		frame.getContentPane().add(createButton("Bet", new ActionBet(), 144, 726, 100, 23));
		frame.getContentPane().add(createButton("Deal", new ActionDeal(), 34, 690, 100, 23));		
		frame.getContentPane().add(createButton("Hit", new ActionHit(), 144, 690, 100, 23));
		frame.getContentPane().add(createButton("Stand", new ActionStand(), 254, 690, 100, 23));
		frame.getContentPane().add(createButton("DoubleDown", new ActionDoubleDown(), 584, 690, 115, 23));
		frame.getContentPane().add(createButton("Split", new ActionSplit(), 364, 690, 100, 23));
		frame.getContentPane().add(createButton("Insurance", new ActionInsurance(), 474, 690, 100, 23));
		frame.getContentPane().add(createButton("Surrender", new ActionSurrender(), 710, 690, 115, 23));
		frame.getContentPane().add(createButton("Advice", new ActionAdvice(), 834, 690, 100, 23));
		frame.getContentPane().add(createButton("Statistics", new ActionStatistics(), 943, 690, 115, 23));
		frame.getContentPane().add(createButton("Clear", new ActionClear(), 1345, 837, 89, 23));
		frame.getContentPane().add(createButton("Exit", new ActionExit(), 1445, 837, 89, 23));
		
		return panel;	
	}
	
    public JLabel createLabel(String label, Color c, String font, int size, int x, int y, int w, int h) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(c);
		lbl.setFont(new Font(font, Font.BOLD, size));
        lbl.setBounds(x, y, w, h);
		return lbl;
    }
	
	public JPanel makeLabel(){
		JPanel panel = new JPanel();
		frame.getContentPane().add(createLabel("Welcome to the POO BlackJack Game", Color.WHITE, "ARIAL", 16, 246, 16, 311, 23));
		frame.getContentPane().add(createLabel("Balance: ", Color.WHITE, "ARIAL", 30, 30, 775, 155, 38));
		return panel;
	}
	
    public JLabel createImg(JPanel panel, Color c, int x, int y, int w, int h, int i) {
		JLabel picLabel = images.addImg(frame, panel, x, y, w, h, c, i);
		return picLabel;
    }

	public JPanel makeImg(int x, int y, int w, int h, Color c, int i){
		JPanel panel = new JPanel();
		panel.setBounds(x, y, w, h);
		panel.add(createImg(panel, c, x, y, w, h, i));
		return panel;
	}
	
    public JTextArea makeScreen() {
		JTextArea screen = new JTextArea();
		screen.setLineWrap(true);  //this tells it to break the string to fit the TextArea
		screen.setWrapStyleWord(true);  //this tells it to break at the word instead of at the character
		JScrollPane scroll = new JScrollPane (screen, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(1080, 11, 480, 778);
		frame.getContentPane().add(scroll);
		screen.setEditable(false);
		
		screen.setColumns(10);
        return screen;
    }
    
    public JTextArea makeRulesScreen() {
		JTextArea rules_screen = new JTextArea();
		rules_screen.setLineWrap(true);  //this tells it to break the string to fit the TextArea
		rules_screen.setWrapStyleWord(true);  //this tells it to break at the word instead of at the character
		rules_screen.setBounds(784, 11, 286, 668);
		rules_screen.setEditable(false);
		Color newColor = new Color(224, 224 , 224);
		rules_screen.setBackground(newColor);
		frame.getContentPane().add(rules_screen);
		rules_screen.setColumns(10);
        return rules_screen;
    }
    
    public void printNumberScreen(JTextArea screen, float f) {
        screen.append(String.valueOf(f));
    }
    
    public void printStringScreen(JTextArea screen, String s) {
        screen.append(s);
    }
    
    public void updateBalance(){
		lblBalanceValue.setText(String.valueOf(p.getPile().getBalance()));
    	jlbNumWhite.setText(Integer.toString(p.getPile().getWhiteStack().getNumberOfChips()));
    	jlbNumRed.setText(Integer.toString(p.getPile().getRedStack().getNumberOfChips()));
    	jlbNumGreen.setText(Integer.toString(p.getPile().getGreenStack().getNumberOfChips()));
    	jlbNumBlack.setText(Integer.toString(p.getPile().getBlackStack().getNumberOfChips()));
    	frame.revalidate();
    }
    
    public void handsLabels(){
		int vertical = 0;
    	if(/*p.numbHands==1*/g.handPlayer.countCards()!=0&&g.handPlayer2.countCards()==0&&g.handPlayer3.countCards()==0&&g.handPlayer4.countCards()==0){
    		JPanel panel = new JPanel();
    		panel.setBackground(newColor);
			panel.setBounds(70, 555, 100, 30);
			panel.add(createLabel("hand 1", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			panel.add(createLabel("("+String.valueOf(g.handPlayer.genTotal())+")", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			frame.getContentPane().add(panel);
    	}
		if(/*p.numbHands==2*/g.handPlayer.countCards()!=0&&g.handPlayer2.countCards()!=0&&g.handPlayer3.countCards()==0&&g.handPlayer4.countCards()==0){
    		JPanel panel = new JPanel();
    		panel.setBackground(newColor);
			panel.setBounds(70, 555, 100, 30);
			panel.add(createLabel("hand 1", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			panel.add(createLabel("("+String.valueOf(g.handPlayer.genTotal())+")", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			frame.getContentPane().add(panel);
    		vertical-=100;
    		panel = new JPanel();
    		panel.setBackground(newColor);
			panel.setBounds(70, 555+vertical, 100, 30);
			panel.add(createLabel("hand 2", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			panel.add(createLabel("("+String.valueOf(g.handPlayer2.genTotal())+")", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			frame.getContentPane().add(panel);
    	}
    	if(/*p.numbHands==3*/g.handPlayer.countCards()!=0&&g.handPlayer2.countCards()!=0&&g.handPlayer3.countCards()!=0&&g.handPlayer4.countCards()==0){
    		JPanel panel = new JPanel();
    		panel.setBackground(newColor);
			panel.setBounds(70, 555, 100, 30);
			panel.add(createLabel("hand 1", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			panel.add(createLabel("("+String.valueOf(g.handPlayer.genTotal())+")", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			frame.getContentPane().add(panel);
    		vertical-=100;
    		panel = new JPanel();
    		panel.setBackground(newColor);
			panel.setBounds(70, 555+vertical, 100, 30);
			panel.add(createLabel("hand 2", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			panel.add(createLabel("("+String.valueOf(g.handPlayer2.genTotal())+")", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			frame.getContentPane().add(panel);
    		vertical-=100;
    		panel = new JPanel();
    		panel.setBackground(newColor);
			panel.setBounds(70, 555+vertical, 100, 30);
			panel.add(createLabel("hand 3", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			panel.add(createLabel("("+String.valueOf(g.handPlayer3.genTotal())+")", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			frame.getContentPane().add(panel);
    	}
    	if(/*p.numbHands==4*/g.handPlayer.countCards()!=0&&g.handPlayer2.countCards()!=0&&g.handPlayer3.countCards()==0&&g.handPlayer4.countCards()!=0){
    		JPanel panel = new JPanel();
    		panel.setBackground(newColor);
			panel.setBounds(70, 555, 100, 30);
			panel.add(createLabel("hand 1", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			panel.add(createLabel("("+String.valueOf(g.handPlayer.genTotal())+")", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			frame.getContentPane().add(panel);
    		vertical-=100;
    		panel = new JPanel();
    		panel.setBackground(newColor);
			panel.setBounds(70, 555+vertical, 100, 30);
			panel.add(createLabel("hand 2", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			panel.add(createLabel("("+String.valueOf(g.handPlayer2.genTotal())+")", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			frame.getContentPane().add(panel);
    		vertical-=100;
    		panel = new JPanel();
    		panel.setBackground(newColor);
			panel.setBounds(70, 555+vertical, 100, 30);
			panel.add(createLabel("hand 3", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			panel.add(createLabel("("+String.valueOf(g.handPlayer3.genTotal())+")", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			frame.getContentPane().add(panel);
    		vertical-=100;
    		panel = new JPanel();
    		panel.setBackground(newColor);
			panel.setBounds(70, 555+vertical, 100, 30);
			panel.add(createLabel("hand 4", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			panel.add(createLabel("("+String.valueOf(g.handPlayer4.genTotal())+")", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			frame.getContentPane().add(panel);

    	}
    }
    
	public int searchCard(Card card){
		if(card.getNum().equals("2")){
			if(card.getSuit().equals("C")) return 0;
			if(card.getSuit().equals("D")) return 1;
			if(card.getSuit().equals("H")) return 2;
			if(card.getSuit().equals("S")) return 3;
		}
		if(card.getNum().equals("3")){
			if(card.getSuit().equals("C")) return 4;
			if(card.getSuit().equals("D")) return 5;
			if(card.getSuit().equals("H")) return 6;
			if(card.getSuit().equals("S")) return 7;
		}
		if(card.getNum().equals("4")){
			if(card.getSuit().equals("C")) return 8;
			if(card.getSuit().equals("D")) return 9;
			if(card.getSuit().equals("H")) return 10;
			if(card.getSuit().equals("S")) return 11;
		}
		if(card.getNum().equals("5")){
			if(card.getSuit().equals("C")) return 12;
			if(card.getSuit().equals("D")) return 13;
			if(card.getSuit().equals("H")) return 14;
			if(card.getSuit().equals("S")) return 15;
		}
		if(card.getNum().equals("6")){
			if(card.getSuit().equals("C")) return 16;
			if(card.getSuit().equals("D")) return 17;
			if(card.getSuit().equals("H")) return 18;
			if(card.getSuit().equals("S")) return 19;
		}
		if(card.getNum().equals("7")){
			if(card.getSuit().equals("C")) return 20;
			if(card.getSuit().equals("D")) return 21;
			if(card.getSuit().equals("H")) return 22;
			if(card.getSuit().equals("S")) return 23;
		}
		if(card.getNum().equals("8")){
			if(card.getSuit().equals("C")) return 24;
			if(card.getSuit().equals("D")) return 25;
			if(card.getSuit().equals("H")) return 26;
			if(card.getSuit().equals("S")) return 27;
		}
		if(card.getNum().equals("9")){
			if(card.getSuit().equals("C")) return 28;
			if(card.getSuit().equals("D")) return 29;
			if(card.getSuit().equals("H")) return 30;
			if(card.getSuit().equals("S")) return 31;
		}
		if(card.getNum().equals("A")){
			if(card.getSuit().equals("C")) return 32;
			if(card.getSuit().equals("D")) return 33;
			if(card.getSuit().equals("H")) return 34;
			if(card.getSuit().equals("S")) return 35;
		}
		if(card.getNum().equals("J")){
			if(card.getSuit().equals("C")) return 36;
			if(card.getSuit().equals("D")) return 37;
			if(card.getSuit().equals("H")) return 38;
			if(card.getSuit().equals("S")) return 39;
		}
		if(card.getNum().equals("K")){
			if(card.getSuit().equals("C")) return 40;
			if(card.getSuit().equals("D")) return 41;
			if(card.getSuit().equals("H")) return 42;
			if(card.getSuit().equals("S")) return 43;
		}				
		if(card.getNum().equals("Q")){
			if(card.getSuit().equals("C")) return 44;
			if(card.getSuit().equals("D")) return 45;
			if(card.getSuit().equals("H")) return 46;
			if(card.getSuit().equals("S")) return 47;
		}				
		if(card.getNum().equals("10")){
			if(card.getSuit().equals("C")) return 48;
			if(card.getSuit().equals("D")) return 49;
			if(card.getSuit().equals("H")) return 50;
			if(card.getSuit().equals("S")) return 51;
		}
		return -1;
	
	}
	
    
}