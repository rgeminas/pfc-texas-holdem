package bancoPFC;

import java.util.ArrayList;

public class PlayerHand {
	public int id_hand;
	public int id_player;
	public int position;
	public int amount_paid;
	public int amount_received;
	public int bankroll_start;
	public int sumcolumn;
	public int stack;
	public int real_paid;
	public ArrayList<Integer> hand_cards;
	public ArrayList<String> action_pflop;
	public ArrayList<String> action_flop;
	public ArrayList<String> action_turn;
	public ArrayList<String> action_river;
	
	public PlayerHand() {
		hand_cards   = new ArrayList<Integer>();
		action_pflop = new ArrayList<String>();
		action_flop  = new ArrayList<String>();
		action_turn  = new ArrayList<String>();
		action_river = new ArrayList<String>();
	}
	
	public String toString(){
		return "("+id_hand+", "+id_player+", "+hand_cards.get(0)+", "+hand_cards.get(1)+", "+position+", "+real_paid+", "+amount_received+",null)";
	}
}
