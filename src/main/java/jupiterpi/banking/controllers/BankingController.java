package jupiterpi.banking.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import javax.inject.Inject;

import jupiterpi.banking.models.*;
import jupiterpi.banking.services.*;

@RequestMapping(path = BankingController.PATH)
@RestController
public class BankingController {
	public static final String PATH ="/banking/api";
	
	private GameRepository gameRepo;
	private PlayerRepository playerRepo;
	private TransactionRepository transactionRepo;
	private BankingService service;
	
	@Inject
	public BankingController(BankingService service,
			                 GameRepository game_repo,
			                 PlayerRepository player_repo,
			                 TransactionRepository transaction_repo) {
		gameRepo = game_repo;
		playerRepo = player_repo;
		transactionRepo = transaction_repo;
		this.service = service;
	}

	//---------------------------------------- Game ---------------------------------------
	@GetMapping(path = "")
	public Iterable<Game> games() {
		Iterable<Game> games = gameRepo.findAll();
		games.forEach(game -> System.out.println(game));
		return games;
	}
    @GetMapping(path = "/{id}") 
    public Game game(@PathVariable("id") Long id) {
        return gameRepo.findOne(id);
    }
    
    @PostMapping()
    public ResponseEntity<Game> create(@RequestBody Game game,
                                       UriComponentsBuilder uriComponentsBuilder) {
      Game savedGame = service.postGame(game);
      UriComponents uriComponents = uriComponentsBuilder.path(PATH + "/{game_id}")
                    .buildAndExpand(savedGame.getId());
      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(uriComponents.toUri());
      return new ResponseEntity<>(savedGame, headers, HttpStatus.CREATED);
    }

    //---------------------------------------- Player ---------------------------------------
	@GetMapping(path = "/{game_id}/player")
	public Iterable<Player> players(@PathVariable("game_id") Long game_id) {
		return playerRepo.findByGameId(game_id); 
	}
    @GetMapping(path = "/{game_id}/player/{player_id}") 
    public Player player(@PathVariable("game_id") Long game_id,
    		             @PathVariable("player_id") Long player_id) {
        return playerRepo.findOne(player_id);
    }

    @PostMapping(path = "/player")
    public ResponseEntity<Player> create(@RequestBody Player player,
                                         UriComponentsBuilder uriComponentsBuilder) {
      Player savedPlayer = service.postPlayer(player);
      UriComponents uriComponents = uriComponentsBuilder.path(PATH + "/{game_id}/player/{player_id}")
                    .buildAndExpand(savedPlayer.getGameId(), savedPlayer.getId());
      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(uriComponents.toUri());
      return new ResponseEntity<>(savedPlayer, headers, HttpStatus.CREATED);
    }
    
	//---------------------------------------- Transaction ---------------------------------------
	@GetMapping(path = "/{game_id}/transaction")
	public Iterable<Transaction > transactions(@PathVariable("game_id") Long game_id) {
		return transactionRepo.findByGameId(game_id);
	}
    @GetMapping(path = "/{game_id}/transaction/{transaction_id}") 
    public Transaction transaction(@PathVariable("game_id") Long game_id,
    		                       @PathVariable("transaction_id") Long transaction_id) {
        return transactionRepo.findOne(transaction_id);
    }

    @PostMapping(path = "/transaction")
    public ResponseEntity<Transaction> create(@RequestBody Transaction transaction,
                                              UriComponentsBuilder uriComponentsBuilder) {
      Transaction savedTransaction = service.postTransaction(transaction);
      UriComponents uriComponents = uriComponentsBuilder.path(PATH + "/{game_id}/transaction/{transaction_id}")
                    .buildAndExpand(savedTransaction.getGameId(), savedTransaction.getId());
      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(uriComponents.toUri());
      return new ResponseEntity<>(savedTransaction, headers, HttpStatus.CREATED);
    }
 }
