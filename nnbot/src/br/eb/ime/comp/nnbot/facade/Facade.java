package br.eb.ime.comp.nnbot.facade;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import br.eb.ime.comp.nnbot.learning.TrainingSet;
import br.eb.ime.comp.nnbot.model.Action;
import br.eb.ime.comp.nnbot.model.Card;
import br.eb.ime.comp.nnbot.model.OddsEstimator;
import br.eb.ime.comp.nnbot.model.Situation;
import br.eb.ime.comp.nnbot.model.Suit;
import br.eb.ime.comp.nnbot.model.Value;

public class Facade {
	private static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		//Loading the JDBC driver for MySql
		Class.forName("com.mysql.jdbc.Driver");

		//Getting a connection to the database. Change the URL parameters
		return DriverManager.getConnection("jdbc:mysql://50.19.234.99:3306/texas_holdem", "pfc", "macaco!");
	}
	
	private static Card cardFromInt(int i)
	{
		return new Card(Value.getByValue(i / 4), Suit.getByValue(i % 4));
	}
	
	public static void fillAllProbabilities()
	{
		try (Connection conn = getConnection()) {
			String query = String.format("select id_hand, flop_0, flop_1, flop_2, turn, river from hand");
			ArrayList<Card> table = new ArrayList<Card>();
			Statement outerSt = conn.createStatement();
			ResultSet outerRs = outerSt.executeQuery(query);
			while (outerRs.next())
			{
				int flop_0 = outerRs.getInt("flop_0");
				if (!outerRs.wasNull()) table.add(cardFromInt(flop_0));
				int flop_1 = outerRs.getInt("flop_1");
				if (!outerRs.wasNull()) table.add(cardFromInt(flop_1));
				int flop_2 = outerRs.getInt("flop_2");
				if (!outerRs.wasNull()) table.add(cardFromInt(flop_2));
				int turn = outerRs.getInt("turn");
				if (!outerRs.wasNull()) table.add(cardFromInt(turn));
				int river = outerRs.getInt("river");
				if (!outerRs.wasNull()) table.add(cardFromInt(river));
			
				// Get cards for each player
				int idhand = outerRs.getInt("id_hand");
				String inquery = String.format("select h.id_hand as id_hand, p.id_player as id_player, p.hand_card_0 as hand_0, p.hand_card_1 as hand_1  from player_hand p, hand h where h.id_hand = p.id_hand and h.id_hand = %d", idhand);
				
				Statement innerSt = conn.createStatement();
				ResultSet innerRs = innerSt.executeQuery(inquery);
				ArrayList<ArrayList<Card>> players = new ArrayList<ArrayList<Card>>();
				ArrayList<Integer> playerIds = new ArrayList<Integer>();
				
				while (innerRs.next())
				{
					ArrayList<Card> thisPlayer = new ArrayList<Card>();
					int hand_0 = innerRs.getInt("hand_0");
					if (!innerRs.wasNull()) thisPlayer.add(cardFromInt(hand_0));
					int hand_1 = innerRs.getInt("hand_1");
					if (!innerRs.wasNull()) thisPlayer.add(cardFromInt(hand_1));
					players.add(thisPlayer);
				}
				innerSt.close();
				innerRs.close();
				
				for (int i = 0; i < players.size(); i++)
				{
					ArrayList<ArrayList<Card>> opponents = new ArrayList<ArrayList<Card>>();
					for (int j = 0; j < players.size(); j++)
					{
						if (i != j)
							opponents.add(players.get(j));
					}
					double odds = OddsEstimator.estimateFlopOddsByMonteCarlo(players.get(i), table, opponents);
					String setquery = String.format("update player_hand where id_player = %d and h.id_hand = %d set hand_odds = %f", playerIds.get(i), idhand, odds);
					Statement setSt = conn.createStatement();
					setSt.executeUpdate(setquery);
					setSt.close();
				}
			}
			outerSt.close();
			outerRs.close();
			
			
		} catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		
	}
	
	public static ArrayList<TrainingSet<Situation, Action>> retrieveDatasets(String filename) throws FileNotFoundException
	{
		ArrayList<TrainingSet<Situation, Action>> result = new ArrayList<TrainingSet<Situation,Action>>();
		
        try (Connection conn = getConnection()) {
                        //Creating a statement object
            Statement stmt = conn.createStatement();
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String playerNames = "";
            String line;
            try {
            	line = in.readLine();

            	playerNames += line;
            	while (line != null)
            	{
            		line = in.readLine();
            		playerNames += ", " + "\"" + line + "\"";
            	}
            }
            catch (IOException e)
            {
            	e.printStackTrace();
            }

            String query = String.format("select s.id_situation as id_situation, s.phase as phase, s.pot as pot, s.action as action, s.actual_bet as actual_bet, s.id_player as id_player, h.odds as hand_odds from situation s, player p, player_hand h where s.id_hand = h.id_hand s.id_player = p.id_player and p.name in (%s)", playerNames);
            
            //Executing the query and getting the result set
            ResultSet rs = stmt.executeQuery(query);
            
            double[] playerStacks = new double[11]; 
            
            //Iterating the resultset and printing the 3rd column            
            while (rs.next()) {
                int idsit = rs.getInt("id_situation");
                
                String subquery = String.format("select s.stack as stack from situation s where s.id_player != %d and id_situation = %d", playerNames);
                Statement innerStmt = conn.createStatement();
                ResultSet innerResult = innerStmt.executeQuery(subquery);
                int i = 0;
                while (innerResult.next())
                {
                	playerStacks[i] = innerResult.getDouble("stack");
                	++i;
                }
                Situation s = new Situation();
                s.cashByPlayer = playerStacks;
                s.cashInHand = rs.getDouble("stack");
                s.cashInPot = rs.getDouble("pot");
                s.isFlop = rs.getString("phase").equals("FLOP");
                s.isPreFlop = rs.getString("phase").equals("PFLOP");
                s.isTurn = rs.getString("phase").equals("TURN");
                s.isRiver = rs.getString("phase").equals("RIVER");
                s.numPlayers = i + 1;
                
                Action a = new Action();
                a.foldOdds = rs.getString("action").equals("f") ? 1 : 0;
                a.raiseOdds = rs.getString("action").equals("c") || rs.getString("action").equals("r") || rs.getString("action").equals("b") ? 1 : 0;
                a.checkOdds = rs.getString("action").equals("k") ? 1 : 0;
                a.raiseAmt = rs.getDouble("actualbet") - rs.getDouble("minbet");
                result.add(new TrainingSet<Situation, Action>(s, a));
            }
            //close the resultset, statement and connection.
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally
        {
        	return result;
        }
	}
}
