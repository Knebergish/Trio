package trio.model.game;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import trio.core.ObjectMapperFactory;
import trio.model.IEntity;
import trio.model.field.Field;
import trio.model.field.FieldImpl;
import trio.model.field.StepResult;
import trio.model.gamer.Gamer;
import trio.model.gamer.GamerImpl;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Entity
@Table(name = "games")
public class GameImpl implements Game, IEntity<String>, Serializable {
	@Id
	@JsonIgnore
	private String id;
	
	@Column(name = "field")
	@JsonProperty("field")
	private String field;
	
	@Column(name = "current_gamer_name")
	@JsonProperty("current_gamer_name")
	private String currentGamerName;
	
	@Column(name = "step_number", columnDefinition = "int default 0")
	@JsonProperty("step_number")
	private int stepNumber;
	
	@Column(name = "last_step_result")
	@JsonProperty("last_step_result")
	private String lastStepResult;
	
	@Column(name = "status", columnDefinition = "int default 0")
	@JsonProperty("status")
	private int status;
	
	@Column(name = "winner_gamer_name")
	@JsonProperty("winner_gamer_name")
	private String winnerGamerName;
	
	@OneToMany
	@JoinTable(
			name = "parties",
			joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "gamer_id", referencedColumnName = "id")
	)
	@JsonProperty("gamers")
	private List<GamerImpl> gamers;
	
	@Override
	public String toString() {
		return "GameImpl{" +
		       "id='" + id + '\'' +
		       ", field='" + field + '\'' +
		       ", currentGamerName='" + currentGamerName + '\'' +
		       ", stepNumber=" + stepNumber +
		       ", lastStepResult='" + lastStepResult + '\'' +
		       ", status=" + status +
		       ", winnerGamerName='" + winnerGamerName + '\'' +
		       ", gamers=" + gamers +
		       '}';
	}
	
	public boolean addGamer(GamerImpl gamer) {
		if (gamers.size() >= 2) {
			return false;
		}
		
		gamers.add(gamer);
		return true;
	}
	
	public GamerImpl getOpponent(String gamerId) {
		if (gamers.size() < 2) {
			return null;
		}
		
		int gamerIndex;
		if (gamers.get(0).getId().equals(gamerId)) {
			gamerIndex = 1;
		} else if (gamers.get(1).getId().equals(gamerId)) {
			gamerIndex = 0;
		} else {
			gamerIndex = -1;
		}
		return gamerIndex == -1 ? null : gamers.get(gamerIndex);
	}
	
	public boolean containGamer(String gamerId) {
		if (gamers.isEmpty()) return false;
		return gamers.stream().map(IEntity::getId).anyMatch(s -> s.equals(gamerId));
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public List<Gamer> getGamers() {
		if (gamers == null) {
			gamers = new ArrayList<>();
		}
		return Collections.unmodifiableList(gamers);
	}
	
	@Override
	@JsonIgnore
	public Field getField() {
		return deserialize(FieldImpl.class, field);
	}
	
	@Override
	public String getCurrentGamerName() {
		return currentGamerName;
	}
	
	@Override
	public int getStepNumber() {
		return stepNumber;
	}
	
	@Override
	@JsonIgnore
	public StepResult getLastStepResult() {
		return deserialize(StepResult.class, lastStepResult);
	}
	
	@Override
	public int getStatus() {
		return status;
	}
	
	@Override
	public String getWinnerGamerName() {
		return winnerGamerName;
	}
	
	public void setWinnerGamerName(String winner_gamer_id) {
		this.winnerGamerName = winner_gamer_id;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setLastStepResult(StepResult lastStepResult) {
		this.lastStepResult = serialize(StepResult.class, lastStepResult);
	}
	
	private <T> String serialize(Class<T> c, T value) {
		ObjectMapper mapper = ObjectMapperFactory.createMapper();
		String       s;
		try {
			s = mapper.writerFor(c).writeValueAsString(value);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			s = "";
		}
		return s;
	}
	
	public void setStepNumber(int stepNumber) {
		this.stepNumber = stepNumber;
	}
	
	public void setCurrentGamerName(String currentGamerName) {
		this.currentGamerName = currentGamerName;
	}
	
	private <T> T deserialize(Class<T> c, String value) {
		ObjectMapper mapper = ObjectMapperFactory.createMapper();
		T            o;
		try {
			o = mapper.readerFor(c).readValue(value);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return null;
		}
		return o;
	}
	
	public void setField(Field field) {
		this.field = serialize(FieldImpl.class, new FieldImpl(field.copyCells()));
	}
}
