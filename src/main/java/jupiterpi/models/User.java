package jupiterpi.models;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class User {
	@Id
	private String user;
	private String name;
	
	private long version;
	
	public User() {};
	public User(String user, String name) {
		this.user = user;
		this.name = name;
	};
	
	public String getUser() { return this.user; }
	public String getName() { return this.name; }
	public long getVersion() { return this.version; }
	
	public void setName(String name) {
		this.name = name;
	}

}
