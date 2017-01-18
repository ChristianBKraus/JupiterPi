package jupiterpi.banking.models;

import javax.persistence.*;

import org.springframework.format.datetime.joda.LocalDateTimeParser;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "Games")
public class Game {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Timestamp createdAt;
	private long version;
	
	public Game() {
		this.createdAt = new Timestamp(new Date().getDate());
		this.version = 1;
	};
	
	public Long getId() { return this.id; }
	public Timestamp getCreatedAt() { return this.createdAt; }
	public long getVersion() { return this.version; }
	
	@Override
    public String toString() {
		if (id != null) 
          return "Game [id = " + id + "; " + "createdAt = " + createdAt + "]";
        else 
          return "Game";
    }
	
}
