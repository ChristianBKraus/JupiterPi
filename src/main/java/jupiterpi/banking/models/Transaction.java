package jupiterpi.banking.models;

import javax.persistence.*;

@Entity
@Table(name = "Transactions")
@NamedQueries({
  @NamedQuery(name = "Transaction.findByGameId", query = "SELECT p FROM Transaction p WHERE p.gameId = ?1")
})
public class Transaction {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long gameId;
	private Long playerId;
	private char type;
	private int amount;
	private Long partnerId;
	private long version;
	
	public Transaction() {};
	public Transaction(Long gameId, Long playerId, char type, int amount, 
			           Long partnerId) {
		this.gameId = gameId;
		this.playerId = playerId;
		this.type = type;
		this.amount = amount;
		this.partnerId = partnerId;
		this.version = 1;
	};
	
	public Long getId() { return this.id; }
	public Long getGameId() { return this.gameId; }
	public Long getPlayerId() { return this.playerId; }
	public char getType() { return this.type; }
	public int getAmount() { return this.amount; }
	public Long getPartnerId() { return this.partnerId; }
	public long getVersion() { return this.version; }
	
	@Override
    public String toString() {
        return "Transaction [playerId =" + playerId + ", amount=" + amount + "]";
    }
	
}
