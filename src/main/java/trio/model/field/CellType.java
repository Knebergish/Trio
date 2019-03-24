package trio.model.field;


public enum CellType {
	TYPE1(1),
	TYPE2(2);
	
	private final int cost;
	
	CellType(int cost) {
		this.cost = cost;
	}
	
	public int getCost() {
		return cost;
	}
}
