package jupiterpi.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;

import jupiterpi.banking.controllers.*;
import jupiterpi.banking.models.*;

@SpringBootTest
public class BankingControllerTests extends ControllerTests{
	public BankingControllerTests() { 
		super(BankingController.PATH);
	}

	// ------------- GAME ---------------------------------
	@Test
	public void createGame() throws Exception {
		Game game = new Game();
		mockMvc.perform(buildPostRequest("", game))
		       .andExpect(status().isCreated())
			   .andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	private Long createGameID() throws Exception {
		 Game game = new Game();
		 MockHttpServletResponse response =		 
				 mockMvc.perform(buildPostRequest("",game))
	                    .andExpect(status().isCreated())
	                    .andExpect(content().contentType(APPLICATION_JSON_UTF8))
	                    .andReturn().getResponse();
		 Game saved_game = convertJsonContent(response,Game.class);	 
		 Long id = saved_game.getId();
		 return id;
	}

	 @Test
	 public void getGame() throws Exception {
		 Long id = createGameID();
     
		 mockMvc.perform(buildGetRequest("/"+id.toString()))
		 	    .andExpect(status().isOk())
	            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
	            .andExpect(jsonPath("$.id",is((id.intValue()))));
	 }

	 @Test
	 public void getGamesContainingCreatedOne() throws Exception {
		 Long id = createGameID();
     
		 MockHttpServletResponse get_response = 
				 mockMvc.perform(buildGetRequest(""))
		 	    	    .andExpect(status().isOk())
	                    .andExpect(content().contentType(APPLICATION_JSON_UTF8))
	                    .andReturn().getResponse();
		 Boolean exists = false;
		 ArrayList<Game>  games = convertJsonContentList(get_response,new TypeReference<ArrayList<Game>>(){});
		 Iterator<Game> e = games.iterator();
		 while (e.hasNext()) { 
			 if (e.next().getId() == id) exists = true;
		 }; 
		 assertTrue(exists);
	 }

	 // --------------------------- PLAYER -------------------------------------
	 @Test 
	 public void createSinglePlayer() throws Exception {
		 Long game_id = createGameID();
		 
		 Player player = new Player(game_id,1,"Player1");
		 mockMvc.perform(buildPostRequest("/player",player))
                .andExpect(status().isCreated())
	            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
	            .andExpect(jsonPath("$.name",is("Player1")))
	            .andExpect(jsonPath("$.amount",is(15000)))
	            .andExpect(jsonPath("$.capital",is(0)))
	            .andExpect(jsonPath("$.gameId",is(game_id.intValue())));
	 }
	 
	 private Player createPlayer(Long game_id, String name) throws Exception {
		 Player player = new Player(game_id,1,name);
		 MockHttpServletResponse response =		 
				 mockMvc.perform(buildPostRequest("/player",player))
	                    .andExpect(status().isCreated())
	                    .andExpect(content().contentType(APPLICATION_JSON_UTF8))
	                    .andReturn().getResponse();
		 Player saved_player = convertJsonContent(response,Player.class);	 
		 return saved_player;
	 }
	 
	 @Test
	 public void getCreatedPlayer() throws Exception {
		 Long game_id = createGameID();
		 Player p = createPlayer(game_id,"Player1");
		 
		 mockMvc.perform(buildGetRequest("/"+game_id+"/player/"+p.getId()))
		 	    .andExpect(status().isOk())
	            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
	            .andExpect(jsonPath("$.name",is("Player1")))
	            .andExpect(jsonPath("$.amount",is(15000)))
	            .andExpect(jsonPath("$.capital",is(0)))
	            .andExpect(jsonPath("$.gameId",is(game_id.intValue())));
	 }
	 
	 @Test
	 public void createPlayersAndGet() throws Exception {
		 Long game_id = createGameID();

		 Player p1 = createPlayer(game_id,"Player1");
		 Player p2 = createPlayer(game_id,"Player2");
		 MockHttpServletResponse get_response = 
		 mockMvc.perform(buildGetRequest("/"+game_id+"/player"))
		 	    .andExpect(status().isOk())
	            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn().getResponse();
		 ArrayList<Player>  players = convertJsonContentList(get_response,new TypeReference<ArrayList<Player>>(){});
		 assertTrue(players.size() == 2);
	 }
	 
	 private Player getPlayer(Long game_id, Long player_id) throws Exception {
         MockHttpServletResponse get_response = 
		 mockMvc.perform(buildGetRequest("/"+game_id+"/player/"+player_id))
		 	    .andExpect(status().isOk())
	            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn().getResponse();
		 Player player = convertJsonContent(get_response,Player.class);
		 return player;
	 }

	 // --------------------------- TRANSACTION -------------------------------------
	 @Test 
	 public void createSingleTransaction() throws Exception {
		 Long game_id = createGameID();
		 Long player_id = createPlayer(game_id,"Player1").getId();
		 Long partner_id = createPlayer(game_id,"Player2").getId();
		 
		 Transaction transaction = new Transaction(game_id,player_id,'C',100,partner_id);
		 mockMvc.perform(buildPostRequest("/transaction",transaction))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.gameId",is(game_id.intValue())))
    	 		.andExpect(jsonPath("$.playerId",is(player_id.intValue())))
	     		.andExpect(jsonPath("$.partnerId",is(partner_id.intValue())))
		 		.andExpect(jsonPath("$.amount",is(100)))
	            .andExpect(jsonPath("$.type",is("C")));
	 }
	 
	 private Transaction createTransaction(Transaction t) throws Exception {
		 MockHttpServletResponse response =		 
				 mockMvc.perform(buildPostRequest("/transaction",t))
	                    .andExpect(status().isCreated())
	                    .andExpect(content().contentType(APPLICATION_JSON_UTF8))
	                    .andReturn().getResponse();
		 Transaction saved_transaction = convertJsonContent(response,Transaction.class);	 
		 return saved_transaction;
	 }
	 
	 @Test
	 public void getCreatedTransaction() throws Exception {
		 Long game_id = createGameID();
		 Player p = createPlayer(game_id,"Player1");
		 Player p2 = createPlayer(game_id,"Player2");
		 Transaction t = createTransaction(new Transaction(game_id,p.getId(),'C',100,p2.getId()));
 		 
		 mockMvc.perform(buildGetRequest("/"+game_id+"/transaction/"+t.getId()))
		 	    .andExpect(status().isOk())
	            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.gameId",is(game_id.intValue())))
		 		.andExpect(jsonPath("$.playerId",is(p.getId().intValue())))
		 		.andExpect(jsonPath("$.partnerId",is(p2.getId().intValue())))
		 		.andExpect(jsonPath("$.amount",is(100)))
                .andExpect(jsonPath("$.type",is("C")));
	 }
	 
	 private ArrayList<Transaction> getTransaction(Long game_id) throws Exception {
         MockHttpServletResponse get_response = 
		 mockMvc.perform(buildGetRequest("/"+game_id+"/transaction"))
		 	    .andExpect(status().isOk())
	            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn().getResponse();
		 ArrayList<Transaction> transactions = convertJsonContentList(get_response,new TypeReference<ArrayList<Transaction>>(){});
		 return transactions;
	 }
	 
	 @Test
	 public void createTransactionsAndGet() throws Exception {
		 Long game_id = createGameID();
		 Player p = createPlayer(game_id,"Player1");
		 Player p2 = createPlayer(game_id,"Player2");
		 Transaction template = new Transaction(game_id,p.getId(),'C',100,p2.getId());
		 Transaction t = createTransaction(template);
 		 
		 ArrayList<Transaction> transactions = getTransaction(game_id);
		 assertTrue(transactions.size() == 1);
		 
		 Transaction t2 = createTransaction(template);
		 ArrayList<Transaction> transactions_2 = getTransaction(game_id);
		 assertTrue(transactions_2.size() == 2);
	 }
	 
	 // ----------------- BALANCES -----------------------------------------
	 
	 @Test
	 public void updatePlayerBalanceCash() throws Exception {
		 Long game_id = createGameID();
		 Player p = createPlayer(game_id,"Player1");
		 Player p2 = createPlayer(game_id,"Player2");
		 Transaction template = new Transaction(game_id,p.getId(),'P',100,0L);
		 Transaction t = createTransaction(template);
 		 
		 Player player = getPlayer(game_id,p.getId());
		 assertTrue(player.getAmount() == 14900);
		 assertTrue(player.getCapital() == 0);
		 Player partner = getPlayer(game_id,p2.getId());
		 assertTrue(player.getCapital() == 0);
		 assertTrue(partner.getAmount() == 15000);
	 }

	 @Test
	 public void updatePlayerBalanceTransfer() throws Exception {
		 Long game_id = createGameID();
		 Player p = createPlayer(game_id,"Player1");
		 Player p2 = createPlayer(game_id,"Player2");
		 Transaction template = new Transaction(game_id,p.getId(),'T',100,p2.getId());
		 Transaction t = createTransaction(template);
 		 
		 Player player = getPlayer(game_id,p.getId());
		 assertTrue(player.getAmount() == 14900);
		 assertTrue(player.getCapital() == 0);
		 Player partner = getPlayer(game_id,p2.getId());
		 assertTrue(partner.getAmount() == 15100);
		 assertTrue(player.getCapital() == 0);
	 }

	 @Test
	 public void updatePlayerBalanceBuy() throws Exception {
		 Long game_id = createGameID();
		 Player p = createPlayer(game_id,"Player1");
		 Player p2 = createPlayer(game_id,"Player2");
		 Transaction template = new Transaction(game_id,p.getId(),'B',100,p2.getId());
		 Transaction t = createTransaction(template);
 		 
		 Player player = getPlayer(game_id,p.getId());
		 assertTrue(player.getAmount() == 14900);
		 assertTrue(player.getCapital() == 100);
		 Player partner = getPlayer(game_id,p2.getId());
		 assertTrue(partner.getAmount() == 15000);
		 assertTrue(partner.getCapital() == 0);
	 }
	 

	 @Test
	 public void updatePlayerBalanceMultiple() throws Exception {
		 Long game_id = createGameID();
		 Long p = createPlayer(game_id,"Player1").getId();
		 Long p2 = createPlayer(game_id,"Player2").getId();
		 createTransaction(new Transaction(game_id,p,'B',100,0L));
		 createTransaction(new Transaction(game_id,p,'T',-100,p2));
		 createTransaction(new Transaction(game_id,p,'P',100,0L));
		 createTransaction(new Transaction(game_id,p2,'T',100,p));
 		 
		 Player player = getPlayer(game_id,p);
		 assertTrue(player.getAmount() == 15000);
		 assertTrue(player.getCapital() == 100);
		 Player partner = getPlayer(game_id,p2);
		 assertTrue(partner.getAmount() == 14800);
		 assertTrue(partner.getCapital() == 0);
	 }
	 
	 @Test 
	 public void updatePlayerBalanceHypothek() throws Exception {
		 Long game_id = createGameID();
		 Long p = createPlayer(game_id,"Player1").getId();
		 
		 createTransaction(new Transaction(game_id,p,'B',200,0L));
		 Player player = getPlayer(game_id,p);
		 assertTrue(player.getAmount() == 14800);
		 assertTrue(player.getCapital() == 200);
		 
		 createTransaction(new Transaction(game_id,p,'H',100,0L));
		 player = getPlayer(game_id,p);
		 assertTrue(player.getAmount() == 14900);
		 assertTrue(player.getCapital() == 0);

		 createTransaction(new Transaction(game_id,p,'H',-110,0L));
		 player = getPlayer(game_id,p);
		 assertTrue(player.getAmount() == 14790);
		 assertTrue(player.getCapital() == 200);
	 }
}
