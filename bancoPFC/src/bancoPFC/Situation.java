package bancoPFC;


public class Situation {
	int id_hand;
	int id_player;
	String phase;
	int bankroll_start;
	String action;
	int min_bet;
	int actual_bet;
	int amount_paid;
	int real_paid;
	int pot;
	
	public Situation(int id_hand, int id_player, String phase, int bankroll_start, String action, int min_bet, int actual_bet, int amount_paid, int real_paid, int pot){
		this.id_hand = id_hand;
		this.id_player = id_player;
		this.phase = phase;
		this.bankroll_start = bankroll_start;
		this.action = action;
		this.min_bet = min_bet;
		this.actual_bet = actual_bet;
		this.amount_paid = amount_paid;
		this.real_paid = real_paid;
		this.pot = pot;
	}
	
	public String toString(){
		return "("+id_hand+", "+id_player+", '"+phase+"', "+pot+", '"+action+"', "+min_bet+", "+actual_bet+")";
	}
}
