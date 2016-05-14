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
import blackjack.GameType;
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
	
	//The constructor associates a Shoe, a Junk, a Player, the max and min bets and initial balance a Statistics and a Strategy to the interface.
	public App(Player player, Dealer dealer, Shoe deck, Junk junk, GameType game, Statistics statistics, Strategy str, int min_bet, int max_bet, double b) throws IOException {
		initialize(player, dealer, deck, junk, game, statistics, str, min_bet, max_bet, b);
	}

	//This method receives the parameters that are necessary to run the graphical interface.
	//It initializes the frame (window)  and its contents. It is not possible to change the size of the window since it is checked to not be resizable.
	//It starts by creating the window and then upload some labels that correspond to the number of chips that the player has, as well as his balance.
	//Two different screens are uploaded, one that it is not editable, with some general rules printed and one screen where the statistics, the advices, etc are printed throughout the game
	//Since this last screen has a scroll bar since a lot can be printed in it.
	private void initialize(Player player, Dealer dealer, Shoe deck,Junk junk, GameType game, Statistics statistics, Strategy str, int bet_min, int bet_max, double b) throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 1600, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		min_bet = bet_min;
		max_bet = bet_max;
		balance = b;
		g = (Game) game;
		j = junk;
		this.statistics = statistics;
		d = dealer;
		p = player;
		this.str = str;
		
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

		screen = makeScreen();
		rules_screen = makeRulesScreen();
		StringBuilder s = new StringBuilder();
		s.append("Important Rules:\n\nYour pile will always have the higher possible chips\n\nMaximum of 4 hands\n\n"); 
		s.append("Dealer must stand on 17\n\nIf the player busts all hands, the dealer wins and does not need to hit up to 17\n\n");
		s.append("The player can only place an insurance bet in the very beginning of the hand, not after a split\n\n");
		s.append("After a split the player can only hit, stand or double (or split again, up to four hands maximum\n\n");
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
	
	//Class that implements the action associated with the bet button.
	//If the value inserted in the textfield is between the min and max bets value that same value is bet.
	//If there is no value on the textfield there are two options: if there was a previous play then the previous bet is used. Otherwise the min bet is used.
	//If there is no valid number in the textfield, a print is sent to the JtextArea on the interface
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
    
    //Class that implements the action associated with the deal button.
    //The player's first hand is printed as well as the first card and hole card of the dealer's hand
    //The button checks if the bet is valid. if there is something that it is not supposed to be on bet space while clicking the button it is asked to the player to delete it
    private class ActionDeal implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	EventQueue.invokeLater( new Runnable() {
            @Override public void run() {    
                if(!g.ingame()){
            		try{
            			g.deal(Double.parseDouble(textField.getText()));
            		}catch(Exception a){
            			if(p.getOldbet()==0){
            				g.deal(min_bet);
            			}else{
            				g.deal(p.getOldbet());
            			}
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

	//Class that implements the action associated with the exit button.
    //The player leaves the game graciously.
    private class ActionExit implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
			System.exit(0);
        }
    }
    
	//Class that implements the action associated with the clear button.
    //At any time, the player can clean the space where the cards are printed.
    private class ActionClear implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	frame.revalidate();
        	frame.repaint();
        }
    }
    
    //Class that implements the action associated with the hit button.
    //The player's hands are printed as well as their values.
    //If the player is still in action then the dealer's hand shown correspond only to the first cards and the hole card.
    //The value of the dealer's hand is only printed when his whole hand is shown
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
    
    //Class that implements the action associated with the stand button.
    //The player's hands are printed as well as their values.
    //If the player is still in action then the dealer's hand shown correspond only to the first cards and the hole card.
    //The value of the dealer's hand is only printed when his whole hand is shown
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
				if(d.getDealerHand().countCards()==2){
    				int k = searchCard(d.getDealerHand().getFirst());
		    		makeImg(230+space, 155, 73, 110, newColor, k);
		    		space += 73;
		    		makeImg(230+space, 155, 73, 110, newColor, 52);
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
        		handsLabels();
				frame.revalidate();
            	}   
	        }); 	
        }
    }
    
  //Class that implements the action associated with the split button.
  //The player's hands are printed as well as their values.
  //The dealer's hand shown correspond only to the first cards and the hole card.
    private class ActionSplit implements ActionListener {
		public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	frame.repaint();
        	frame.revalidate();
        	EventQueue.invokeLater( new Runnable() {
        	@Override public void run() {   
        		if(g.ingame()){
					g.split();
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
	    		space = 0;
	    		int k = searchCard(g.handDealer.getCards().getFirst());
	    		makeImg(230+space, 155, 73, 110, newColor, k);
	    		space += 73;
	   			makeImg(230+space, 155, 73, 110, newColor, 52);			
        		handsLabels();
	   			frame.revalidate();
			}
        }); 
      }
  }
    
    //Class that implements the action associated with the doubledown button.
    //The player's hands are printed as well as their values.
    //If the player is still in action then the dealer's hand shown correspond only to the first cards and the hole card.
    //The value of the dealer's hand is only printed when his whole hand is shown
    private class ActionDoubleDown implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	if(g.ingame()&&g.firstplay()){
        			g.doubleDown();
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
				panel.add(createLabel("Dealer's hand", Color.WHITE, "ARIAL", 16,130, 155, 180, 30));
				panel.add(createLabel("("+String.valueOf(g.handDealer.genTotal())+")", Color.WHITE, "ARIAL", 16, 130, 155, 180, 30));
				frame.getContentPane().add(panel);
				}
			}
        	handsLabels();
    		frame.revalidate();
        }
    }

    //Class that implements the action associated with the insurance button.
    //The player's first hands is printed as well as its values.
    //The value of the dealer's hand is only printed when his whole hand is shown
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
    			space += 73;
    		}
    		updateBalance();
    		space = 0;
			int k = searchCard(d.getDealerHand().getFirst());
    		makeImg(230+space, 155, 73, 110, newColor, k);
    		space += 73;
    		makeImg(230+space, 155, 73, 110, newColor, 52);
    		frame.revalidate();
        }
    }
    
    //Class that implements the action associated with the surrender button.
    //The player can only surrender on the first play so after the cards are dealt and a surrender is performed, the dealer's hand is printed as well as its value
    private class ActionSurrender implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//What we want the button to do
        	if(g.ingame()&&g.firstplay()){
        		g.surrender();
        		updateBalance();
        		int space = 0;
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
        		handsLabels();
    			frame.revalidate();
        	}
        }
    } 
	
    //Class that implements the action associated with the advice button.
    //The advice is given according to the multiple game strategies.
    //These advices are printed on a JtextArea named screen.
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

    //Class that implements the action associated with the statistics button.
    //These statistics are printed on a JtextArea named screen.
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
	
    //This method creates a JButton.
    //It receives a String named label that corresponds to the name that appears on the button
    //It receives an ActionListener al that corresponds to the action performed by the same button.
    //It receives the position coordinates (x, y) where the button should appear on the window and the width and height of the button (w and h) 
    public JButton createButton(String label, ActionListener al, int x, int y, int w, int h) {
        JButton button = new JButton(label);
        button.setBounds(x, y, w, h);
        button.addActionListener(al);
        return button;
    }
	
    //This method adds all the JButtons created to a panel on the frame/window.
    //The panel with all the buttons with the corresponding actions is returned.
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
	
	//This method creates a JLabel.
	//It receives a String named label that corresponds to the name that appears on the label
    //It receives color c that corresponds to the color of the font of the label and also the font that is wanted as well as its size.
    //It receives the position coordinates (x, y) where the label should appear on the window and the width and height of the button (w and h) 
    public JLabel createLabel(String label, Color c, String font, int size, int x, int y, int w, int h) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(c);
		lbl.setFont(new Font(font, Font.BOLD, size));
        lbl.setBounds(x, y, w, h);
		return lbl;
    }
	
    //This method adds two labels to a panel on the frame/window.
    //The first label corresponds to a welcoming message.
    //The second label corresponds to the String "Balance: " printed before the actual value of the player's balance.
    //The panel with the two Jlabels is returned.
	public JPanel makeLabel(){
		JPanel panel = new JPanel();
		frame.getContentPane().add(createLabel("Welcome to the POO BlackJack Game", Color.WHITE, "ARIAL", 16, 246, 16, 311, 23));
		frame.getContentPane().add(createLabel("Balance: ", Color.WHITE, "ARIAL", 30, 30, 775, 155, 38));
		return panel;
	}
	
	//This method creates a JLabel.
	//It receives a panel where the image will be printed.
	//It receives color c that corresponds to the color of the background of the panel.
	//It receives the position coordinates (x, y) where the label should appear on the window and the width and height of the button (w and h) 
	//It receives an integer i taht corresponds to the position i of a vector that stores all the images that can be printed.
	//The Jlabel with the image is returned
    public JLabel createImg(JPanel panel, Color c, int x, int y, int w, int h, int i) {
		JLabel picLabel = images.addImg(frame, panel, x, y, w, h, c, i);
		return picLabel;
    }

    //This method creates a new panel with the image created with the method createImg().
	//The new panel is returned.
    public JPanel makeImg(int x, int y, int w, int h, Color c, int i){
		JPanel panel = new JPanel();
		panel.setBounds(x, y, w, h);
		panel.add(createImg(panel, c, x, y, w, h, i));
		return panel;
	}
	
    //This method creates a new JTextArea named screen
    //screen.setLineWrap(true); tells it to break the string in order to fit the TextArea
    //screen.setWrapStyleWord(true); tells it to break at the word instead of breaking it at the character
    //The JTextArea has a scroll bar associated that allows the user to move it up and down if he wants to check older text printed or newer.
    //screen.setEditable(false); means that nothing can be inserted in it directly by the user meaning that the values shown cannot be modified
    public JTextArea makeScreen() {
		JTextArea screen = new JTextArea();
		screen.setLineWrap(true);
		screen.setWrapStyleWord(true);  
		JScrollPane scroll = new JScrollPane (screen, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(1080, 11, 480, 778);
		frame.getContentPane().add(scroll);
		screen.setEditable(false);
		
		screen.setColumns(10);
        return screen;
    }
    
    //This method creates a new JTextArea named makeRulesScreen
    //screen.setLineWrap(true); tells it to break the string in order to fit the TextArea
    //screen.setWrapStyleWord(true); tells it to break at the word instead of breaking it at the character
    //rules_screen.setEditable(false); means that nothing can be inserted in it directly by the user meaning that the values shown cannot be modified
    public JTextArea makeRulesScreen() {
		JTextArea rules_screen = new JTextArea();
		rules_screen.setLineWrap(true);
		rules_screen.setWrapStyleWord(true);
		rules_screen.setBounds(784, 11, 286, 668);
		rules_screen.setEditable(false);
		Color newColor = new Color(224, 224 , 224);
		rules_screen.setBackground(newColor);
		frame.getContentPane().add(rules_screen);
		rules_screen.setColumns(10);
        return rules_screen;
    }
    
    //This method prints a float on the JTextArea named screen
    public void printNumberScreen(JTextArea screen, float f) {
        screen.append(String.valueOf(f));
    }
    
    //This method prints a String on the JTextArea named screen
    public void printStringScreen(JTextArea screen, String s) {
        screen.append(s);
    }
    
    //This method updates multiple labels of the window.
    //The label with the value of the player's balance is updated.
    //The labels with the numbers of chips corresponding to each chip color are updated.
    //The window is then refreshed.
    public void updateBalance(){
		lblBalanceValue.setText(String.valueOf(p.getPile().getBalance()));
    	jlbNumWhite.setText(Integer.toString(p.getPile().getWhiteStack().getNumberOfChips()));
    	jlbNumRed.setText(Integer.toString(p.getPile().getRedStack().getNumberOfChips()));
    	jlbNumGreen.setText(Integer.toString(p.getPile().getGreenStack().getNumberOfChips()));
    	jlbNumBlack.setText(Integer.toString(p.getPile().getBlackStack().getNumberOfChips()));
    	frame.revalidate();
    }
    
    //This method updates multiple labels of the window.
    //The labels with the numbers of the corresponding hand and also the value of the corresponding hand are updated.
    //The number of labels printed depends on the the number of playable hands.
    //The window is then refreshed.
    public void handsLabels(){
		int vertical = 0;
    	if(g.handPlayer.countCards()!=0&&g.handPlayer2.countCards()==0&&g.handPlayer3.countCards()==0&&g.handPlayer4.countCards()==0){
    		JPanel panel = new JPanel();
    		panel.setBackground(newColor);
			panel.setBounds(70, 555, 100, 30);
			panel.add(createLabel("hand 1", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			panel.add(createLabel("("+String.valueOf(g.handPlayer.genTotal())+")", Color.WHITE, "ARIAL", 16, 70, 555, 100, 20));
			frame.getContentPane().add(panel);
    	}
		if(g.handPlayer.countCards()!=0&&g.handPlayer2.countCards()!=0&&g.handPlayer3.countCards()==0&&g.handPlayer4.countCards()==0){
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
    	if(g.handPlayer.countCards()!=0&&g.handPlayer2.countCards()!=0&&g.handPlayer3.countCards()!=0&&g.handPlayer4.countCards()==0){
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
    	if(g.handPlayer.countCards()!=0&&g.handPlayer2.countCards()!=0&&g.handPlayer3.countCards()!=0&&g.handPlayer4.countCards()!=0){
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
    
    //This method is used to help find the image of certain card.
    //It receives a specific card and then checks its "num" and "suit".
    //Given the two parameters it returns a specific int, let's call it "i", that will be later used to get the image from the position "i" of the vector that stores the images.
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
	
    