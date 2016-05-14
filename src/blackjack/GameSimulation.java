package blackjack;


/** This class extends an abstract class GameType. It has the same implementation that Class Game, also extended from GameType, but
 * has no System.out.println() methods and similar or any other methods that call them. It also does't have any methods or parameters
 * related to the package graphical, so in simulation there is no graphical interface launched.
 * @author Pedro Esteves, Ricardo Cristino, Rui Bras
 * @version 1.0
 * */
public class GameSimulation extends GameType {

	public GameSimulation(Shoe shoe, Junk junk, Player p, Dealer d, Statistics s, Strategy str) {
		super(shoe, junk, p, d, s, str);
	}

	@Override
	public void makeBet(double b, double min_bet) {
		if(alreadyBet()) {
			if(!player.reBet(b,min_bet)){
				return;
			}
			return;
		}
		if(!player.bet(b,min_bet)){
			return;
		}
		setAlreadyBet(true);
	}

	@Override
	public void deal(double min_bet) {
		if(ingame()){
			return;
		}
		
		if(!player.deal()) {
			return;
		}
		dealer.drawFirstFour(player);
		statistics.addPlays();
		str.countCard(player.getHand().getCards().getFirst());
		str.countCard(player.getHand().getCards().getLast());
		str.countCard(dealer.getDealerHand().getCards().getFirst());
		setInGame(true);
		
	}

	@Override
	public void split() {
		if(!ingame() || !player.getHand().cardsSameValue()){
			return;
		}
		if(count_splits==3){
			stand();
			return;
		}
		if(player.getBalance()<player.getBet()){
			stand();
			return;
		}
		player.split(dealer.dealCard(),dealer.dealCard());
		str.countCard(player.hands.get(player.getCurrentHand()).getCards().get(1));
		str.countCard(player.hands.get(player.getCurrentHand()+1).getCards().get(1));
		statistics.addPlays();
		++count_splits;
		setWasASplit(true);
		setFirstplay(true);
	}

	@Override
	public void insurance() {
		if(wasASplit() || insuranceMode()|| !firstplay()|| !ingame() || !dealer.canHaveBlackjack()){
			return;
		}
		if(player.setInsuranceBet()){
			setInsuranceMode(true);
		}
	}

	@Override
	public void hit() {
		if(!ingame()){
			System.out.println("h: Illegal command");
			return;
		}
		setFirstplay(false);
		player.hit(dealer.dealCard());
		str.countCard(player.getHand().getCards().getLast());
		if(player.getHand().getTotal()>21){
			player.setBetZero();
			player.setCurrentHand(player.getCurrentHand()+1);
			if(player.getCurrentHand()==player.getNumHands()){
				player.setCurrentHand(player.getCurrentHand()-1);
				finalizeDealer();
				return;
			}
			setFirstplay(true);
		}
	}

	@Override
	public void stand() {
		if(!ingame()){
			return;
		}
		setFirstplay(true);
		if(player.getCurrentHand()+1<player.getNumHands()){
			player.setCurrentHand(player.getCurrentHand()+1);
			return;
		}
		else{
			finalizeDealer();
		}
	}

	@Override
	public boolean dealerFinalizeCards() {
		if(player.allHandsBusted()){
			return true;
		}
		while(dealer.getDealerHand().getTotal()<17){
			dealer.drawCardToDealer();
			str.countCard(dealer.getDealerHand().getCards().getLast());
			if(dealer.getDealerHand().getTotal()>21){
				/* When the dealer busts */
				int i = 0;
				player.setCurrentHand(i);
				while(player.getCurrentHand()<player.getNumHands()){
					if(player.getBet()==0){
						statistics.addLoses();
					}else{
						player.setBalance(player.getBet()*2);
						statistics.addWins();
					}
					player.setCurrentHand(++i);
				}
				cleanTable();
				return false;
			}
		}
		return true;
	}

	@Override
	public void finalizeDealer() {
		setFirstplay(true);
		str.countCard(dealer.getDealerHand().getCards().get(1));
		/* Check if the player has blackjack */
		if(player.getHand().blackjack() && !wasASplit()){
			statistics.addPlayerbj();
			if(dealer.getDealerHand().blackjack()){
				if(insuranceMode()){
					player.setBalance(2*player.getInsuranceBet());
					setInsuranceMode(false);
				}
				player.setBalance(player.getBet());
				statistics.addDealerbj();
				statistics.addPushes();
				cleanTable();
				return;
			}else{
				if(insuranceMode()){
					setInsuranceMode(false);
				}
				player.setBalance((player.getBet())*2.5);
				statistics.addWins();
				cleanTable();
				return;
			}
		}
		/* Checks if the dealer has blackjack */
		if(dealer.getDealerHand().blackjack()){
			statistics.addDealerbj();
			if(insuranceMode()){
				player.setBalance(2*player.getInsuranceBet());
				statistics.addLoses();
				cleanTable();
				setInsuranceMode(false);
				return;
			}else{
				statistics.addLoses();
				setInsuranceMode(false);
				cleanTable();
				return;
			}
		}else{
			if(insuranceMode()){
				setInsuranceMode(false);
			}
		}
		
		if(dealerFinalizeCards()==false){
			return;
		}
		int n = 0;
		player.setCurrentHand(n);
		while((player.getCurrentHand())<player.getNumHands()){
			if(player.getBet()==0){
				statistics.addLoses();
				player.setCurrentHand(++n);
				continue;
			}
			if(player.getHand().getTotal()>dealer.getDealerHand().getTotal()){
				player.setBalance((player.getBet())*2);
				statistics.addWins();
			}else if(player.getHand().getTotal()==dealer.getDealerHand().getTotal()){
				//If he has a 21 hand value of two cards, it wins an ordinary 21 made of more than 2 cards
				if(player.getHand().countCards()==2 && player.getHand().getTotal()==21){
					player.setBalance(2*player.getBet());
					statistics.addWins();
				}else{
					player.setBalance(player.getBet());
					statistics.addPushes();
				}
			}
			else{
				statistics.addLoses();
			}
			player.setCurrentHand(++n);
		}
		cleanTable();
	}

	@Override
	public void doubleDown() {
		if(!ingame() || !firstplay()){
			return;
		}
		if(player.getBalance()<player.getBet()){
			stand();
			return;
		}
		if(!player.getHand().canDouble()||wasASplit()){
			stand();
			return;
		}
		player.doubleDown(dealer.dealCard());
		str.countCard(player.getHand().getCards().getLast());
		if(player.getHand().getTotal()>21){
			player.setBetZero();
			player.setCurrentHand(player.getCurrentHand()+1);
			if(player.getCurrentHand()==player.getNumHands()){
				player.setCurrentHand(player.getCurrentHand()-1);
				finalizeDealer();
				return;
			}
			setFirstplay(true);
		}	
		stand();
	}

	@Override
	public void cleanTable() {
		setFirstplay(true);
		dealer.cleanDealerHand();
		dealer.cleanPlayerHands(player);
		setWasASplit(false);
		setInGame(false);
		setAlreadyBet(false);
		count_splits=0;
	}

	@Override
	public void surrender() {
		if(!ingame()||insuranceMode()){
			return;
		}
		if(wasASplit() || !firstplay()){
			stand();
			return;
		}
		player.setBalance(0.5*player.getBet());
		statistics.addLoses();
		cleanTable();
	}

}
