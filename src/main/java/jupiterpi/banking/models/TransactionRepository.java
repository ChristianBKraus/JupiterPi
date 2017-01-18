package jupiterpi.banking.models;

import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long>{
  Iterable<Transaction> findByGameId(Long gameId);
}
