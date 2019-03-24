package trio.model.gamer;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import trio.model.IEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "gamers")
public class GamerImpl implements Gamer, IEntity<String>, Serializable {
	@Id
	@JsonIgnore
	private String id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("score")
	private int score;
	
	public GamerImpl() {
	}
	
	public GamerImpl(String id, String name, int score) {
		this.id = id;
		this.name = name;
		this.score = score;
	}
	
	@Override
	public String toString() {
		return "GamerImpl{" +
		       "id='" + id + '\'' +
		       ", name='" + name + '\'' +
		       ", score=" + score +
		       '}';
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}



