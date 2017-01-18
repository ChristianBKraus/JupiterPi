package jupiterpi.banking.services;
import javax.inject.Inject;

import org.springframework.stereotype.Component;

import jupiterpi.banking.models.*;

@Component
public class BankingService {
	
  final static public char c_transfer = 'T';
  final static public char c_buy = 'B';
  final static public char c_pay = 'P';
  final static public char c_hypothek = 'H';
	
  private GameRepository gameRepo;
  private PlayerRepository playerRepo;
  private TransactionRepository transactionRepo;
  
  public BankingService(GameRepository game, PlayerRepository player, TransactionRepository transaction) {
	gameRepo = game;
	playerRepo = player;
	transactionRepo = transaction;
  }
	
  public Game postGame(Game game) {
    Game savedGame = gameRepo.save(game);
    return savedGame;
  }
  public Player postPlayer(Player player) {
	Player savedPlayer = playerRepo.save(player);
	return savedPlayer;
  }

  public Transaction postTransaction(Transaction t) {
	  
	// Additional Actions  
	Player p1 = playerRepo.findOne(t.getPlayerId());
	switch (t.getType()) {
		case c_pay: 
			p1.setAmount(p1.getAmount() - t.getAmount());
			playerRepo.save(p1);
			break;
		case c_transfer: 
			Player p2 = playerRepo.findOne(t.getPartnerId());
			p1.setAmount(p1.getAmount() - t.getAmount());
			p2.setAmount(p2.getAmount() + t.getAmount());
			Transaction t2 = new Transaction(t.getGameId(), p2.getId(), c_transfer,
                    -t.getAmount(),p1.getId());
			playerRepo.save(p1);
			playerRepo.save(p2);
			transactionRepo.save(t2);
			break;
		case c_buy:
			p1.setAmount(p1.getAmount() - t.getAmount());
			p1.setCapital(p1.getCapital() + t.getAmount());
			playerRepo.save(p1);
			break;
		case c_hypothek:
			p1.setAmount(p1.getAmount() + t.getAmount());
			boolean create = t.getAmount() > 0; 
			if (create) {
				p1.setCapital(p1.getCapital() - 2 * t.getAmount());
			} else {
				p1.setCapital(p1.getCapital() - 2 * (int) Math.round( ( (float) t.getAmount() ) / 1.1 ) ); 
			}
			playerRepo.save(p1);
			break;
	}
	
	Transaction savedTransaction = transactionRepo.save(t);	
	return savedTransaction;
  }
  
}
