package bancoPFC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class Main {
	static String URL = "jdbc:mysql://localhost:3306/texas_holdem";
	static String USER = "root";
	static String PASS = "admin";
 //	static String URL = "jdbc:mysql://50.19.234.99:3306/texas_holdem";
//	static String USER = "pfc";
//	static String PASS = "macaco!";
	static int countx = 0;
	
	public static void main(String args[]){
		while(true)
		try{
			populateSituations();
		}catch(Exception ex){
			continue;
		}
	}
	
	static public ResultSet loadPdb(Connection con, String where){
		ResultSet rs = null;
		try{
			Statement stpdb = (Statement) con.createStatement();
			System.out.println("Loading pdb...");
		    String querypdb = "select * from pdb60_order2" + where;
		    rs = stpdb.executeQuery(querypdb);
		    System.out.println("OK!");
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	    return rs;
	}
	
	static void getPhCards(PlayerHand ph, String cards){
		StringTokenizer strtkn = new StringTokenizer(cards, ";");  
		List<String> tokens = new ArrayList<String>();
		for(int j = 0; j < 2; j++){
			if (strtkn.hasMoreTokens()) {  
				tokens.add(strtkn.nextToken());  
			}
			else{
				tokens.add("null");  
			}
		}
		for (String t : tokens) {
			int num = getCardID(String.valueOf(t.charAt(0)),String.valueOf(t.charAt(1)));
			ph.hand_cards.add(num);
		}
	}
	
	static public ResultSet loadHdb(Connection con, String where){
		ResultSet rs = null;
		try{
			Statement stpdb = (Statement) con.createStatement();
			System.out.println("Loading hdb...");
		    String queryhdb = "select * from hdb60" + where;
		    rs = stpdb.executeQuery(queryhdb);
		    System.out.println("OK!");
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	    return rs;
	}
	
	static int getMaxId(Statement st){
		int min = 0;
		ResultSet idhand = null;
		try {
			System.out.println("Loading maxID...");
		    String queryid = "select max(id_hand) from situation";
		    idhand = st.executeQuery(queryid);
			idhand.next();
		    min = idhand.getInt(1);
		    if(min<797210868) min = 797210868;
		    System.out.println(min);
		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return min;
	}
	
	
	static void populateSituations(){
		Connection con = null;
		Statement st = null;
		ResultSet rspdb = null;
		ResultSet rshdb = null;
		try{
			con = DriverManager.getConnection( URL, USER, PASS );
			st = con.createStatement();
//////////////////// maxID
			int min = getMaxId(st);
			int max = min+10000000;
		    System.out.println(max);
//////////////////////
		    String where = " where id > "+ min +" AND id <"+max+" order by id";
//			File ids = openDir( "bancoPFC/handsfodoes" );
//		    FileReader fr = null;
//		    BufferedReader br = null;
//			
//		    fr = new FileReader(ids);
//			br = new BufferedReader(fr);
//			
//			String where = " where id = ";
//					String line = "";
//			for(int g = 0; g<10000;g++){
//				line = br.readLine();
//				where+=line+" OR id = ";
//			}
//			where+=br.readLine();
//			System.out.println(line);
		    //String where = " where id = 827108774 OR "

		    HashMap<String, Integer> hmIdPlayer = getHashIdPlayer(con);

		    int count=0;
		    int numplayers;
		    ArrayList<PlayerHand> aph = new ArrayList<PlayerHand>();
		    ArrayList<Situation> sits = new ArrayList<Situation>();
		    ArrayList<PlayerHand> aphs = new ArrayList<PlayerHand>();
	    	String saida ="";
	    	rspdb = loadPdb(con, where);
	    	rshdb = loadHdb(con, where);
		    while(rspdb.next()){
		    	PlayerHand ph = new PlayerHand();
		    	numplayers = rspdb.getInt(3);

		    	ph.id_hand = rspdb.getInt(2);
		    	//ph.id_player = getPlayerIdByName(con,rspdb.getString(1));
		    	//System.out.println(rspdb.getString(1));
		    	ph.id_player = hmIdPlayer.get(rspdb.getString(1));
		    	ph.position = rspdb.getInt(4);
		    	ph.action_pflop = tokenActions(rspdb.getString(5));
		    	ph.action_flop = tokenActions(rspdb.getString(6));
		    	ph.action_turn = tokenActions(rspdb.getString(7));
		    	ph.action_river = tokenActions(rspdb.getString(8));
		    	ph.bankroll_start = rspdb.getInt(9);
		    	ph.amount_received = rspdb.getInt(11);
		    	ph.stack = ph.bankroll_start;
		    	ph.real_paid = rspdb.getInt(10);
		    	getPhCards(ph, rspdb.getString(12));
		    	
				aph.add(ph);
				
				if(ph.position == 1){
//					rshdb.next();
//					int id_hand = rshdb.getInt(1);
//					if(id_hand != ph.id_hand){
//						System.out.println("Erro - id's diferentes\nhdb="+id_hand+"\npdb"+ph.id_hand);
//						return;
//					}
//				    String queryhdb = "select * from hdb60 where id = '"+ph.id_hand+"'";
//				    ResultSet rshdb = st2.executeQuery(queryhdb);
				    rshdb.next();
					Hand hand = new Hand();
					int id_hand = rshdb.getInt(1);
					hand.pot_pflop = rshdb.getInt(6);
					hand.pot_flop = rshdb.getInt(8);
					hand.pot_turn = rshdb.getInt(10);
					hand.pot_river = rshdb.getInt(12);
					saida+=rshdb.getInt(1)+"\n";
					Collections.reverse(aph);
					ArrayList<Situation> tempsits = processPlayerHand(aph, hand);
					for(PlayerHand p: aph){
						aphs.add(p);
					}
					for( Situation s : tempsits ){
						count++;
						countx++;
						sits.add(s);
						//System.out.println(countx);
					}
					aph.clear();
					//System.out.println(999-tempsits.size());
				}
				if(count>999){
			    	String query1="insert into situation (id_hand, id_player, phase, pot, action, min_bet, actual_bet) values ";
			    	for( Situation s : sits ){
			    		query1+= s.toString() + ",";
			    	}
			    	query1 = query1.substring(0, query1.length()-1);
			    	query1+= ";";
			    	
			    	String query2="insert into player_hand values ";
			    	for( PlayerHand p : aphs ){
			    		if(p!=null)	query2+= p.toString() + ",";
			    	}
			    	query2 = query2.substring(0, query2.length()-2);
			    	query2+= ");";
			    	//System.out.println(query2);
			    	st.executeUpdate(query1);
			    	st.executeUpdate(query2);
			    	//System.out.println(saida);
			    	saida="";
			    	count = 0;
			    	aphs.clear();
				}
		    
		    }
		}catch(SQLException ex){
		ex.printStackTrace();
		}
//		catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		finally {
		    try { if (rspdb != null) rspdb.close(); } catch (Exception e) {};
		    try { if (rshdb != null) rshdb.close(); } catch (Exception e) {};
		    try { if (st != null) st.close(); } catch (Exception e) {};
		    try { if (con != null) con.close(); } catch (Exception e) {};
		}
	}

	private static int getPlayerIdByName(Connection con, String string) {
		int resp = 0;
		Statement st = null;
		try {
			st = (Statement) con.createStatement();
			System.out.println(string);
			String query = "select id from player where name = '"+string+"'";
		    ResultSet rs = st.executeQuery(query);
		    rs.next();
		    resp = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("id não encontrado para " + string);
		} finally{
			try { if (st != null) st.close(); } catch (Exception e) {};
		}
		return resp;
	}

	static ArrayList<Situation> processPlayerHand(ArrayList<PlayerHand> aph, Hand hand){
		ArrayList<Situation> sits = new ArrayList<Situation>();
		int sumpflop=0, sumflop=0, sumturn=0, sumriver=0;
		int moreActions = 0;
		int minBet = 1;
		int actualBet = 1;
		int maxPaid = 0;
		for(PlayerHand ph : aph){
			if(ph.action_pflop.size()>moreActions) moreActions = ph.action_pflop.size();
		}
		for(PlayerHand ph : aph){
			while(ph.action_pflop.size()<moreActions) ph.action_pflop.add("-");
		}
		for( int i = 0; i < moreActions ; i++ ){
			//System.out.println("   "+(moreActions-i));
			for(int j = 0; j < aph.size() ; j++){
				//System.out.println("pflop    "+(aph.size()-j));
				PlayerHand ph = aph.get(j);
				
				
				if(!(ph.action_pflop.get(i).equals("-"))){
					if(ph.action_pflop.get(i).equals("B")){
							if(ph.position==1){
								minBet = 1;
								actualBet = 1;
								ph.amount_paid+=actualBet;
								maxPaid = ph.amount_paid;
								
							}
							else{
								minBet = 2;
								actualBet = 2;
								ph.amount_paid+=actualBet;
								maxPaid = ph.amount_paid;
							}
					}
					if(ph.action_pflop.get(i).equals("f")||ph.action_pflop.get(i).equals("Q")||ph.action_pflop.get(i).equals("K")){
						minBet = maxPaid - ph.amount_paid;
						actualBet = 0;
					}
					if(ph.action_pflop.get(i).equals("k")){
						minBet = 0;
						actualBet = 0;
					}
					if(ph.action_pflop.get(i).equals("c")){
						minBet = maxPaid - ph.amount_paid;
						actualBet = minBet;
						ph.amount_paid+=actualBet;
					}
					if(ph.action_pflop.get(i).equals("r")||ph.action_pflop.get(i).equals("b")){
						minBet = maxPaid - ph.amount_paid;
						actualBet = minBet+2;
						ph.amount_paid+=actualBet;
						maxPaid = ph.amount_paid;
					}
					Situation sit = new Situation( ph.id_hand, ph.id_player, "pflop", ph.bankroll_start, ph.action_pflop.get(i), minBet, actualBet, ph.amount_paid, ph.real_paid, sumpflop);
					sumpflop+=actualBet;
					sits.add(sit);
				}
			}
		}
		
		///////////////////////////////////flop
		
		for(PlayerHand ph : aph){
			if(ph.action_flop.size()>moreActions) moreActions = ph.action_flop.size();
		}
		
		for(PlayerHand ph : aph){
			while(ph.action_flop.size()<moreActions) ph.action_flop.add("-");
		}
		
		for( int i = 0; i < moreActions ; i++ ){
			//System.out.println("   "+(moreActions-i));
			for(int j = 0; j < aph.size() ; j++){
				//System.out.println("flop    "+(aph.size()-j));
				PlayerHand ph = aph.get(j);
				
				
				if(!(ph.action_flop.get(i).equals("-"))){
					if(ph.action_flop.get(i).equals("B")){
							if(ph.position==1){
								minBet = 1;
								actualBet = 1;
								ph.amount_paid+=actualBet;
								maxPaid = ph.amount_paid;
								
							}
							else{
								minBet = 2;
								actualBet = 2;
								ph.amount_paid+=actualBet;
								maxPaid = ph.amount_paid;
							}
					}
					if(ph.action_flop.get(i).equals("f")||ph.action_flop.get(i).equals("Q")||ph.action_flop.get(i).equals("K")){
						minBet = maxPaid - ph.amount_paid;
						actualBet = 0;
					}
					if(ph.action_flop.get(i).equals("k")){
						minBet = 0;
						actualBet = 0;
					}
					if(ph.action_flop.get(i).equals("c")){
						minBet = maxPaid - ph.amount_paid;
						actualBet = minBet;
						ph.amount_paid+=actualBet;
					}
					if(ph.action_flop.get(i).equals("r")||ph.action_flop.get(i).equals("b")){
						minBet = maxPaid - ph.amount_paid;
						actualBet = minBet+2;
						ph.amount_paid+=actualBet;
						maxPaid = ph.amount_paid;
					}
					Situation sit = new Situation( ph.id_hand, ph.id_player, "flop", ph.bankroll_start, ph.action_flop.get(i), minBet, actualBet, ph.amount_paid, ph.real_paid,sumpflop+sumflop);
					sumflop+=actualBet;
					sits.add(sit);
				}
			}
		}
		
		////////////////////////////turn
		
		for(PlayerHand ph : aph){
			if(ph.action_turn.size()>moreActions) moreActions = ph.action_turn.size();
		}
		for(PlayerHand ph : aph){
			while(ph.action_turn.size()<moreActions) ph.action_turn.add("-");
		}
		for( int i = 0; i < moreActions ; i++ ){
			//System.out.println("   "+(moreActions-i));
			for(int j = 0; j < aph.size() ; j++){
				//System.out.println("turn    "+(aph.size()-j));
				PlayerHand ph = aph.get(j);
				
				
				if(!(ph.action_turn.get(i).equals("-"))){
					if(ph.action_turn.get(i).equals("B")){
							if(ph.position==1){
								minBet = 1;
								actualBet = 1;
								ph.amount_paid+=actualBet;
								maxPaid = ph.amount_paid;
								
							}
							else{
								minBet = 2;
								actualBet = 2;
								ph.amount_paid+=actualBet;
								maxPaid = ph.amount_paid;
							}
					}
					if(ph.action_turn.get(i).equals("f")||ph.action_turn.get(i).equals("Q")||ph.action_turn.get(i).equals("K")){
						minBet = maxPaid - ph.amount_paid;
						actualBet = 0;
					}
					if(ph.action_turn.get(i).equals("k")){
						minBet = 0;
						actualBet = 0;
					}
					if(ph.action_turn.get(i).equals("c")){
						minBet = maxPaid - ph.amount_paid;
						actualBet = minBet;
						ph.amount_paid+=actualBet;
					}
					if(ph.action_turn.get(i).equals("r")||ph.action_turn.get(i).equals("b")){
						minBet = maxPaid - ph.amount_paid;
						actualBet = minBet+2;
						ph.amount_paid+=actualBet;
						maxPaid = ph.amount_paid;
					}
					Situation sit = new Situation( ph.id_hand, ph.id_player, "turn", ph.bankroll_start, ph.action_turn.get(i), minBet, actualBet, ph.amount_paid, ph.real_paid,sumpflop+sumflop+sumturn);
					sumturn+=actualBet;
					sits.add(sit);
				}
			}
		}
		
		///////////////////////river
		
		for(PlayerHand ph : aph){
			if(ph.action_river.size()>moreActions) moreActions = ph.action_river.size();
		}
		for(PlayerHand ph : aph){
			while(ph.action_river.size()<moreActions) ph.action_river.add("-");
		}
		for( int i = 0; i < moreActions ; i++ ){
			//System.out.println("   "+(moreActions-i));
			for(int j = 0; j < aph.size() ; j++){
				//System.out.println("river    "+(aph.size()-j));
				PlayerHand ph = aph.get(j);
				
				
				if(!(ph.action_river.get(i).equals("-"))){
					if(ph.action_river.get(i).equals("B")){
							if(ph.position==1){
								minBet = 1;
								actualBet = 1;
								ph.amount_paid+=actualBet;
								maxPaid = ph.amount_paid;
								
							}
							else{
								minBet = 2;
								actualBet = 2;
								ph.amount_paid+=actualBet;
								maxPaid = ph.amount_paid;
							}
					}
					if(ph.action_river.get(i).equals("f")||ph.action_river.get(i).equals("Q")||ph.action_river.get(i).equals("K")){
						minBet = maxPaid - ph.amount_paid;
						actualBet = 0;
					}
					if(ph.action_river.get(i).equals("k")){
						minBet = 0;
						actualBet = 0;
					}
					if(ph.action_river.get(i).equals("c")){
						minBet = maxPaid - ph.amount_paid;
						actualBet = minBet;
						ph.amount_paid+=actualBet;
					}
					if(ph.action_river.get(i).equals("r")||ph.action_river.get(i).equals("b")){
						minBet = maxPaid - ph.amount_paid;
						actualBet = minBet+2;
						ph.amount_paid+=actualBet;
						maxPaid = ph.amount_paid;
					}
					Situation sit = new Situation( ph.id_hand, ph.id_player, "river", ph.bankroll_start, ph.action_river.get(i), minBet, actualBet, ph.amount_paid, ph.real_paid,sumpflop+sumflop+sumturn+sumriver);
					sumriver+=actualBet;
					sits.add(sit);
				}
			}
		}
		///////////////////////////////////////////
		double fator = 1;
		for(Situation s : sits){
			if(s.phase.equals("pflop")){
				fator = hand.pot_pflop*s.real_paid/(sumpflop*s.amount_paid+1);
			}
			if(s.phase.equals("flop")){
				fator = hand.pot_flop*s.real_paid/(sumflop*s.amount_paid+1);
			}
			if(s.phase.equals("turn")){
				fator = hand.pot_turn*s.real_paid/(sumturn*s.amount_paid+1);
			}
			if(s.phase.equals("river")){
				fator = hand.pot_river*s.real_paid/(sumriver*s.amount_paid+1);
			}
			fator = Math.round( fator );
			s.min_bet = (int) (s.min_bet*fator);
			s.actual_bet = (int) (s.actual_bet*fator);
			s.pot = (int) (s.pot*fator);
		}
		
		return sits;
		
		
	}
	
	
	
	static ArrayList<String> tokenActions(String actions){
		ArrayList<String> arrayActions = new ArrayList<String>();
		StringTokenizer strtkn = new StringTokenizer(actions, ",");  
		List<String> tokens = new ArrayList<String>();
		while((strtkn.hasMoreTokens())){
			tokens.add(strtkn.nextToken());
		}
		for (String t : tokens) {
			arrayActions.add(t);
		}
		return arrayActions;
	}
		
	static HashMap<String, Integer> getHashIdPlayer(Connection con){
		HashMap<String, Integer> hm = null;
		try {
			Statement st = (Statement) con.createStatement();
			String query = "select id,name from player";
		    ResultSet rs = st.executeQuery(query);
		    hm = new HashMap<>();
		    while(rs.next()){
		    	hm.put(rs.getString(2), rs.getInt(1));
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return hm;
	}
	
	static Integer getCardID(String value, String suit){
		int valor,naipe=0;
		switch( value ){
			
			case "A": valor=1;
        		break;
        	case "T": valor=10;
        		break;
            case "J": valor=11;
            	break;
            case "Q": valor=12;
                break;
            case "K": valor=13;
                break;
            case "n": valor=14;
				break;
            default: valor= Integer.parseInt(value);
                break;
        }
        switch(suit){
        	case "u": naipe=1;
        		break;
        	case "c": naipe=1;
                break;
            case "s": naipe=2;
                break;
            case "h": naipe=3;
                break;
            case "d": naipe=4;
                break;
    	}
    	return 4*(valor-1) + naipe;
	}
	
	public static void populateHand(){
		java.sql.Connection con = null;
		try{
			con = DriverManager.getConnection( URL, USER, PASS );
		    Statement st = (Statement) con.createStatement(); 
		    
		    File ids = openDir( "bancoPFC/id_hand60+" );
		    FileReader fr = null;
		    BufferedReader br = null;
			
		    File sqlError = openDir( "bancoPFC/sqlError" );
			
			fr = new FileReader(ids);
			br = new BufferedReader(fr);
			String lines = "";
			String line = "";
			int ind = 0;
			int i = 0;
			int faltam = 1463378;
			int atual = 0;
			String insert = "";
			while( (line = br.readLine()) != null ){
				if((!line.equals("000000000")&&(i!=999))){
					line = line + " OR id = ";
					lines+="\n"+line;
					i++;
				}
				else{
					lines = "select id,cards from texas_holdem.hdb where id =" + lines ;
					if (!line.equals("000000000")) lines+= line ;
					else lines = lines.substring(0, lines.length() - 9 );
					lines+= ";";
				//	System.out.println( faltam );
					try{
				//		System.out.println(lines);
						ResultSet idCard = st.executeQuery(lines);
						while(idCard.next()){
							insert+= " ("+idCard.getInt(1)+", ";
							StringTokenizer strtkn = new StringTokenizer(idCard.getString(2), ";");  
							List<String> tokens = new ArrayList<String>();
							for(int j = 0; j < 5; j++){
								if (strtkn.hasMoreTokens()) {  
									tokens.add(strtkn.nextToken());  
								}
								else{
									tokens.add("null");  
								}
							}
							for (String t : tokens) {
								if(!t.equals("null")){
									insert+=getCardID(t.substring(0,1),t.substring(1,2))+", ";
								}
								else{
									insert+="null, ";
								}
							}
							insert = insert.substring(0,insert.length()-2);
							insert += ", null), ";
						}
					insert = "insert into hand values "+insert.substring(0,insert.length()-2)+";";
					System.out.println(insert);
					faltam -= 1000;
					atual += 1000;
					st.executeUpdate(insert);
					insert = "";	
					System.out.println("Atual:"+atual+" (faltam +"+faltam+")");
					}catch(SQLException ex){
						ex.printStackTrace();
						write(sqlError,atual+"-"+atual+1000);
						
					}
					
					i = 0;
					lines = "";
				}
			}
		    
		}catch(SQLException e){
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void populateHdb60(){
		java.sql.Connection con = null;
		try{
			con = DriverManager.getConnection( URL, USER, PASS );
		    Statement st = (Statement) con.createStatement(); 
		    
		    File ids = openDir( "bancoPFC/id_hand60+" );
		    FileReader fr = null;
		    BufferedReader br = null;
			
		    File sqlError = openDir( "bancoPFC/sqlError" );
			
			fr = new FileReader(ids);
			br = new BufferedReader(fr);
			String lines = "";
			String line = "";
			int ind = 0;
			int i = 0;
//			while( (line = br.readLine()) != null ){
//				lines = "insert into texas_holdem.pdb60 select * from texas_holdem.pdb where id =" + line + ";";
//				st.executeUpdate(lines);
//				System.out.println(1669885-i);
//				i++;
//			}
			int faltam = 1463378;
			int atual = 0;
			while( (line = br.readLine()) != null ){
				if( i != 999){
					line = line + " OR id = ";
					lines+="\n"+line;
					i++;
				}
				else{
					lines = "insert into texas_holdem.hdb60 select * from texas_holdem.hdb where id =" + lines + line + ";";
					System.out.println( faltam );
					try{
						//System.out.println(lines);
						st.executeUpdate(lines);
						faltam -= 1000;
						atual += 1000;
					}catch(SQLException ex){
						ex.printStackTrace();
						write(sqlError,atual+"-"+atual+1000);
						
					}
					
					i = 0;
					lines = "";
				}
			}
			if(lines != null && lines != ""){
				lines = "insert into texas_holdem.pdb60 select * from texas_holdem.pdb where id =" + lines + line + ";";
				System.out.println( faltam );
				try{
					st.executeUpdate(lines);
					faltam -= i;
					atual += i;
				}catch(SQLException ex){
					ex.printStackTrace();
					write(sqlError,atual+"-"+atual+1000);
				}
			}
		    
		}catch(SQLException e){
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void populatePdb60(){

		java.sql.Connection con = null;
		try{
			con = DriverManager.getConnection( URL, USER, PASS );
		    Statement st = (Statement) con.createStatement(); 
		    
		    File ids = openDir( "bancoPFC/faltam" );
		    FileReader fr = null;
		    BufferedReader br = null;
			
		    File sqlError = openDir( "bancoPFC/sqlError" );
			
			fr = new FileReader(ids);
			br = new BufferedReader(fr);
			String lines = "";
			String line = "";
			int ind = 0;
			int i = 0;
//			while( (line = br.readLine()) != null ){
//				lines = "insert into texas_holdem.pdb60 select * from texas_holdem.pdb where id =" + line + ";";
//				st.executeUpdate(lines);
//				System.out.println(1669885-i);
//				i++;
//			}
			int faltam = 1463378;
			int atual = 0;
			while( (line = br.readLine()) != null ){
				if( i != 377){
					line = line + " OR id = ";
					lines+="\n"+line;
					i++;
				}
				else{
					lines = "insert into texas_holdem.pdb60 select * from texas_holdem.pdb where id =" + lines + line + ";";
					faltam -= 1000;
					atual += 1000;
					System.out.println( faltam );
					try{
						//System.out.println(lines);
						st.executeUpdate(lines);
					}catch(SQLException ex){
						ex.printStackTrace();
						write(sqlError,atual+"-"+atual+1000);
						
					}
					
					i = 0;
					lines = "";
				}
			}
			if(lines != null && lines != ""){
				lines = "insert into texas_holdem.pdb60 select * from texas_holdem.pdb where id =" + lines + line + ";";
				lines = new StringBuilder(lines).replace(ind-1, ind+1, ";").toString();
				System.out.println( 1669885-i*1000 );
				try{
					st.executeUpdate(lines);
				}catch(SQLException ex){
					ex.printStackTrace();
					write(sqlError,atual+"-"+atual+1000);
				}
			}
		    
		}catch(SQLException e){
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
			
			
			
	
	
	
	////////////////////////
	public static File openDir(String path){
		File folder = null;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			URL url = classLoader.getResource(path);
			folder = new File(url.toURI());
		} catch (URISyntaxException e) {
			return null;
		}
		return folder;
	}
	
	public static void transferAndUploadPdb( int dir, int finit ){
		transferAndUploadPdb(dir, dir, finit);
	}
	
	
	
	public static void splitFile(File f){
		
	}
	
	public static void compileFiles(){
		File direc = openDir("bancoPFC/bd");
		for(File nextBD : direc.listFiles()){
			System.out.println(nextBD);
			for (File nextFolder : nextBD.listFiles()) {
				for( File nextFile : nextFolder.listFiles()){
					if(nextFile.isDirectory()){
						for( File pdb : nextFile.listFiles()){
							transfer(pdb, openDir("bancoPFC/bd").getAbsolutePath()+"/pdb");
						}
					}
					else{
						if( nextFile.getName().equals("hroster")){
							transfer( nextFile, openDir("bancoPFC/bd").getAbsolutePath()+"/hroster");
						}
						else{	
							if( nextFile.getName().equals("hdb")){
								transfer( nextFile, openDir("bancoPFC/bd").getAbsolutePath()+"/hdb");
							}
						}
					}
				}
			}
		}
	}
	public static void uploadhdb(){
		java.sql.Connection con = null;
		try {
		     con = DriverManager.getConnection( URL, USER, PASS );
		     Statement st = (Statement) con.createStatement(); 

		     
		     File direc = openDir("bancoPFC/bd");
				for(File nextFile : direc.listFiles()){
					if( nextFile.getName().equals("hdb")){
						String line = null;
						FileReader fr = null;
						BufferedReader br = null;
						try {
							fr = new FileReader(nextFile);
							br = new BufferedReader(fr);
							while( (line = br.readLine()) != null ){
								line = line.replace(" ", ";");
								int ind = line.lastIndexOf(";;");
								line = new StringBuilder(line).replace(ind+1, ind+2,";\"").toString();
								while(line.contains(";;")) line = line.replace(";;", ";");
								ind = line.length();
								line = new StringBuilder(line).replace(ind, ind+1, "\");").toString();
								line = line.replace("/", ";");
								line = new StringBuilder(line).replace(0, 0, "insert into texas_holdem.hdb values (").toString();
								for( ind = 0 ; ind < 12 ; ind++ ) line = line.replaceFirst(";", ",");
								System.out.println(line);
								if(line.contains(";")) st.executeUpdate(line);
							}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		     con.close();
		}

		catch (SQLException ex) {
		     Logger lgr = Logger.getLogger(Main.class.getName());
		     lgr.log(Level.SEVERE, ex.getMessage(), ex);

		 } 
		
//		File direc = openDir("bancoPFC/bd/teste");
//		for(File nextFile : direc.listFiles()){
//			String line = null;
//			FileReader fr = null;
//			BufferedReader br = null;
//			ArrayList<String> lines = new ArrayList<String>();
//			try {
//				fr = new FileReader(nextFile);
//				br = new BufferedReader(fr);
//				while( (line = br.readLine()) != null ){
//					line = line.replace(" ", ";");
//					int ind = line.lastIndexOf(";;");
//					line = new StringBuilder(line).replace(ind+1, ind+2,";\"").toString();
//					while(line.contains(";;")) line = line.replace(";;", ";");
//					ind = line.length();
//					line = new StringBuilder(line).replace(ind, ind+1, "\");").toString();
//					line = line.replace("/", ";");
//					line = new StringBuilder(line).replace(0, 1, "insert into test.hdb values (").toString();
//			//		line = line.replace(", newChar)
//					lines.add(line);
//					System.out.println(line);
//				}
//				FileWriter fw = new FileWriter(nextFile);
//	            BufferedWriter bw = new BufferedWriter(fw);
//	            for( String nline : lines){
//	            	bw.write( nline );
//	            	bw.write("\n");
//	            }
//	            //bw.write(lines.toString());
//	            bw.close();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
	
	
	public static void transfer(File f, String newPath){
		File f1 = new File(f.getPath());
		File f2 = new File(newPath);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(f1);
			out = new FileOutputStream(f2, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			IOUtils.copy(in, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
    }
	
	public static void write(File f, String content){
		OutputStream out = null;
		try {
			out = new FileOutputStream(f, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			IOUtils.write(content, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			IOUtils.closeQuietly(out);
		}
	}
	
	public static void moveFile(File from, String sto){
		InputStream inStream = null;
		OutputStream outStream = null;
	 
	    	try{
	    		File dir = openDir("bancoPFC/bd/pdb/split/ok");
	    		File to = new File(dir.getAbsolutePath()+"/"+sto);
	    		//System.out.println(to.getPath());
	    		to.createNewFile();
	    		
	    	    inStream = new FileInputStream(from);
	    	    outStream = new FileOutputStream(to);
	 
	    	    byte[] buffer = new byte[1024];
	 
	    	    int length;
	    	    //copy the file content in bytes 
	    	    while ((length = inStream.read(buffer)) > 0){
	 
	    	    	outStream.write(buffer, 0, length);
	 
	    	    }
	 
	    	    inStream.close();
	    	    outStream.close();
	 
	    	    //delete the original file
	    	    from.delete();
	 
	    	    System.out.println("File is copied successful!");
	 
	    	}catch(IOException e){
	    	    e.printStackTrace();
	    	}
	}
	
	public static void transferAndUploadHdb(){
		java.sql.Connection con = null;
		File nextFile = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			con = DriverManager.getConnection( URL, USER, PASS );
		    Statement st = (Statement) con.createStatement(); 
		    int f = 0;
		    for( f = 1; f <= 5658 ; f++ ){
			    
		    	nextFile = openDir( "bancoPFC/bd/hdb/split/" + f );
    			String line = null;
				try {
					fr = new FileReader(nextFile);
					br = new BufferedReader(fr);
					String lines = "";
					int ind = 0;
					int i = 0;
					while( (line = br.readLine()) != null ){
						//System.out.println(line);
						line = line.replace(" ", ";");
						ind = line.lastIndexOf(";;");
						line = new StringBuilder(line).replace(ind+1, ind+2,";\"").toString();
						while(line.contains(";;")) line = line.replace(";;", ";");
						ind = line.length();
						line = new StringBuilder(line).replace(ind, ind+1, "\"),").toString();
						line = line.replace("/", ";");
						line = new StringBuilder(line).replace(0, 0, " (").toString();
						
						for( ind = 0 ; ind < 12 ; ind++ ) line = line.replaceFirst(";", ",");
						line = line.replace(" ", "");
						lines+="\n"+line;
						//System.out.println(i +  " - " + line + " - " + nextFile.getName());
						i++;
						if( i == 1000){
							lines = "insert into texas_holdem.hdb values " + lines;
							ind = lines.length();
							lines = new StringBuilder(lines).replace(ind-1, ind+1, ";").toString();
							//System.out.println(lines);
							System.out.println("in: uploading file " + f );
							st.executeUpdate(lines);
							i = 0;
							lines = "";
						}
					}
					if(lines != null && lines != ""){
						lines = "insert into texas_holdem.hdb values " + lines;
						ind = lines.length();
						lines = new StringBuilder(lines).replace(ind-1, ind+1, ";").toString();
						//System.out.println(lines);
						System.out.println("out: uploading file " + f );
						st.executeUpdate(lines);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			con.close();
			}catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Main.class.getName());
				lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	public static void transferAndUploadHroster(){
		java.sql.Connection con = null;
		try {
			con = DriverManager.getConnection( URL, USER, PASS );
		    Statement st = (Statement) con.createStatement(); 
		    int f = 0;
		    for( f = 1; f <= 5658 ; f++ ){
		    	File nextFile = openDir( "bancoPFC/bd/hroster/split/" + f );
				String line = null;
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(nextFile);
					br = new BufferedReader(fr);
					String lines = "";
					int ind = 0;
					int i = 0;
					while( (line = br.readLine()) != null ){
						line = line.replace(" ", ";");
						while(line.contains(";;")) line = line.replace(";;", ";");
						line = line.replaceFirst(";", ",");
						line = line.replaceFirst(";", ",\"");
						ind = line.length();
						line = new StringBuilder(line).replace(ind, ind+1, "\"),").toString();
						line = line.replace("/", ";");
						line = new StringBuilder(line).replace(0, 0, " (").toString();
						line = line.replace(" ", "");
						lines+="\n"+line;
						i++;
						if( i == 1000){
							lines = "insert into texas_holdem.hroster values " + lines;
							ind = lines.length();
							lines = new StringBuilder(lines).replace(ind-1, ind+1, ";").toString();
							System.out.println("in: uploading file " + f );
							st.executeUpdate(lines);
							i = 0;
							lines = "";
						}
					}
					if(lines != null && lines != ""){
						lines = "insert into texas_holdem.hroster values " + lines;
						ind = lines.length();
						lines = new StringBuilder(lines).replace(ind-1, ind+1, ";").toString();
						System.out.println("out: uploading file " + f );
						st.executeUpdate(lines);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			con.close();
		}catch (SQLException ex) {
			Logger lgr = Logger.getLogger(Main.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	public static void transferAndUploadPdb(int spinit, int spfin, int finit){
		java.sql.Connection con = null;
		try {
			con = DriverManager.getConnection( URL, USER, PASS );
		    Statement st = (Statement) con.createStatement(); 
		    int f = 0;
		    int sp = 1;
		    File lineError = openDir( "bancoPFC/bd/pdb/lineError" );
		    File fileError = openDir( "bancoPFC/bd/pdb/fileError" );
			for( sp = spinit; sp <= spfin ; sp++ ){
				try{
			    	File len = openDir("bancoPFC/bd/pdb/split/split"+sp+"/");
			    	//for( f = finit; f <= 3158 ; f++ ){
			    	//////////////////////
			    	ArrayList<Integer> files = new ArrayList<Integer>();
			    	int numf = 0;
			    	for( File x : len.listFiles()){
			    		files.add(numf, Integer.parseInt(x.getName()));
			    		numf++;
			    	}
			    	for( numf = 0 ; numf < files.size() ; numf++ ){
			    		f = files.get(numf);
			    	////////////////////////////
			    			//<= len.listFiles().length - 1 ; f++ ){
			    		File nextFile = openDir( "bancoPFC/bd/pdb/split/split"+sp+"/" + f );
			    		String line = null;
						FileReader fr = null;
						BufferedReader br = null;
						try {
							fr = new FileReader(nextFile);
							br = new BufferedReader(fr);
							String lines = "";
							int ind = 0;
							int i = 0;
							int fi = 0;
							while( (line = br.readLine()) != null ){
								
							//	System.out.println(line);
								line = line.replace(" ", ";");
								line = line.replace("ak47\\", "ak47");
								while(line.contains(";;")) line = line.replace(";;", ";");
								line = line.replaceFirst(";", "\",");
								line = line.replaceFirst(";", ",");
								line = line.replaceFirst(";", ",");
								
								line = line.replaceFirst(";", ",");
								line = line.replaceFirst(";", ",");
								line = line.replaceFirst(";", ",");
								line = line.replaceFirst(";", ",");
								line = line.replaceFirst(",", ";");
								line = line.replaceFirst(",", ";");
								line = line.replaceFirst(",", ";");
								line = line.replaceFirst(",", ";");
								line = line.replace(",", "\",\"");
	
							//	System.out.println(line);
								line = line.replaceFirst(";", ",");
								line = line.replaceFirst(";", ",");
								line = line.replaceFirst(";", ",");
								ind = line.indexOf(";");
								line = line.replaceFirst(";", ",\"");
								line = line.replaceFirst(";", "\",");
								line = line.replaceFirst(";", ",");
								line = line.replaceFirst(";", ",");
								line = line.replaceFirst(";", ",\"");
							//	System.out.println(line);
								try{
									for( fi = ind + 1 ; fi <= line.length() ; fi++ ){
										if( line.charAt(fi) == '\"' ){
							//				System.out.println(1+"-"+line.charAt(fi)+line.charAt(fi+1));
											continue;
										}
										if( line.charAt(fi) == ',' ){
											if( line.charAt(fi+1) == '\"' ){
							//					System.out.println(2+"-"+line.charAt(fi)+line.charAt(fi+1));
												continue;
											}
											else{
							//					System.out.println(3+"-"+line.charAt(fi)+line.charAt(fi+1));
												break;
											}
										}
										if( line.charAt(fi+1) == '\"' ){
							//				System.out.println(4+"-"+line.charAt(fi)+line.charAt(fi+1));
											continue;
										}
										if( line.charAt(fi+1) == ',' ){
							//				System.out.println(5+"-"+line.charAt(fi)+line.charAt(fi+1));
											continue;
										}
										String line1 = line.substring(0, fi + 1);
										String line2 = line.substring( fi + 1, line.length() );
										line = line1 + "," + line2;
										fi++;
									}
								}catch(StringIndexOutOfBoundsException ex){
									System.out.println("lineError");
									write(lineError,line+"\n");
									continue;
								}
						//		System.out.println(line);
								
								//line = line.replaceFirst(";", ",\"");
								ind = line.length();
								line = new StringBuilder(line).replace(ind, ind+1, "\"),").toString();
								line = line.replace("/", ";");
								line = new StringBuilder(line).replace(0, 0, " (\"").toString();
								line = line.replace(" ", "");
						//		line = line.replace(",)", ")");
						//		System.out.println(line);
								lines+="\n"+line;
								i++;
								if( i == 1000){
									lines = "insert into texas_holdem.pdb values " + lines;
									ind = lines.length();
									lines = new StringBuilder(lines).replace(ind-1, ind+1, ";").toString();
									System.out.println("1-uploading file " + f + " of split" + sp );
									try{
										st.executeUpdate(lines);
									}catch(SQLException ex){
										//createLog(ex,);
										st.executeUpdate(lines);
										System.out.println("lineError");
										write(lineError,line+"\n");
										continue;
									}
									
									i = 0;
									lines = "";
								}
							}
							if(lines != null && lines != ""){
								lines = "insert into texas_holdem.pdb values " + lines;
								ind = lines.length();
								lines = new StringBuilder(lines).replace(ind-1, ind+1, ";").toString();
								System.out.println("2-uploading file " + f + " of split" + sp );
								try{
									st.executeUpdate(lines);
								}catch(SQLException ex){
									System.out.println("lineError");
									write(lineError,line+"\n");
									continue;
								}
							}
						} catch (FileNotFoundException e) {
							System.out.println("fileError");
							write(fileError,"split"+sp+": "+f+"\n");
							continue;
						} catch (IOException e) {
							System.out.println("fileError");
							write(fileError,"split"+sp+": "+f+"\n");
							continue;
						} catch (SQLException e){
							continue;
						}
						moveFile(nextFile, sp+"-"+f);
					}
				}catch(NullPointerException ex){
					System.out.println("fileError");
					write(fileError,"split"+sp+": "+f+"\n");
					continue;
				}
		    }
			
			con.close();
		}catch (SQLException ex) {
			System.out.println("lalalal");
			Logger lgr = Logger.getLogger(Main.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
}
