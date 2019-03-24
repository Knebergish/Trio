package trio.model.field;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class FieldImpl implements Field, Serializable {
	@JsonProperty("cells")
	private final CellType[][] cells;
	
	@JsonCreator
	public FieldImpl(@JsonProperty("cells") CellType[][] cells) {
		this.cells = cells.clone();
	}
	
	public CellType[][] copyCells() {
		return cells.clone();
	}
	
	@Override
	@JsonIgnore
	public CellType get(int x, int y) {
		return cells[x][y];
	}
	
	@Override
	@JsonIgnore
	public int getWidth() {
		return cells[0].length;
	}
	
	@Override
	@JsonIgnore
	public int getHeight() {
		return cells.length;
	}
}
