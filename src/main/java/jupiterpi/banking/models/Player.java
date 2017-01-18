package jupiterpi.banking.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;


@Entity
@Table(name = "Players")
@NamedQueries({
	  @NamedQuery(name = "Player.findByGameId", query = "SELECT p FROM Player p WHERE p.gameId = ?1")
	})
public class Player {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long gameId;
	private int number;
	private String name;
	private int amount;
	private int capital;
	private long version;
	

	public Player() {
		this.amount = 15000;
		this.capital = 0;
		this.version = 1;
	};
	public Player(Long gameId, int number, String name) {
		this.gameId = gameId;
		this.name = name;
		this.amount = 15000;
		this.capital = 0;
		this.version = 1;
	};
	
	public Long getId() { return this.id; }
	public Long getGameId() { return this.gameId; }
	public int getNumber() { return this.number; }
	public String getName() { return this.name; }
	public int getAmount() { return this.amount; }
	public int getCapital() { return this.capital; }
	public long getVersion() { return this.version; }

//	@OneToMany(mappedBy="id")
//    private Collection<Transaction> transactions = new ArrayList<Transaction>();
	
	public void setName(String name) {
		this.name = name;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void setCapital(int capital) {
		this.capital = capital;
	}

	@Override
    public String toString() {
        return "Player [id =" + id + ", gameid=" + gameId + ", name=" + name + ", amount=" + amount + ", capital=" + capital + "]";
    }
	
}
