package jupiterpi.banking.models;

import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long>{
	  Iterable<Player> findByGameId(Long game_id);
}
