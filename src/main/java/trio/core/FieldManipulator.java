package trio.core;


import trio.model.field.*;

import java.util.*;


public final class FieldManipulator {
	private static final Random rnd = new Random();
	
	private FieldManipulator() {
	}
	
	public static StepResult move(Field field, Coordinates source, Coordinates dest) {
		final List<Field>  states = new ArrayList<>();
		int                score  = 0;
		final CellType[][] cells  = field.copyCells();
		
		CellType temp = cells[dest.getX()][dest.getY()];
		cells[dest.getX()][dest.getY()] = cells[source.getX()][source.getY()];
		cells[source.getX()][source.getY()] = temp;
		
		Field newField = new FieldImpl(cells);
		while (true) {
			final Set<Coordinates> cellsForDelete = getAllCellsForDelete(field);
			if (cellsForDelete.isEmpty()) {
				break;
			} else {
				for (Coordinates c : cellsForDelete) {
					score += newField.get(c.getX(), c.getY()).getCost();
				}
				newField = deleteCells(newField, cellsForDelete);
				newField = vacuumAndFillField(newField);
				states.add(newField);
			}
		}
		
		return new StepResult(states, score);
	}
	
	public static Set<Coordinates> getAllCellsForDelete(Field field) {
		Set<Coordinates> cellsForDelete;
		cellsForDelete = new HashSet<>();
		for (int i = 0; i < field.getHeight(); i++) {
			for (int j = field.getWidth() - 1; j >= 0; j--) {
				cellsForDelete.addAll(getCellsForDelete(field, new Coordinates(i, j)));
			}
		}
		return cellsForDelete;
	}
	
	public static Field deleteCells(Field field, Set<Coordinates> coordinates) {
		CellType[][] cells = field.copyCells();
		for (Coordinates coord : coordinates) {
			cells[coord.getX()][coord.getY()] = null;
		}
		return new FieldImpl(cells);
	}
	
	public static Field vacuumAndFillField(Field field) {
		CellType[][] cells = field.copyCells();
		for (int i = 0; i < field.getHeight(); i++) {
			for (int j = field.getWidth() - 1; j >= 0; j--) {
				if (cells[i][j] == null) {
					for (int k = j - 1; k >= 0; k--) {
						if (cells[i][k] != null) {
							cells[i][j] = cells[i][k];
							cells[i][k] = null;
							break;
						}
					}
				}
			}
			for (int j = 0; j < field.getWidth(); j++) {
				if (cells[i][j] == null) {
					cells[i][j] = getRandomCellValue();
				} else {
					break;
				}
			}
		}
		
		return new FieldImpl(cells);
	}
	
	public static Set<Coordinates> getCellsForDelete(Field field, Coordinates target) {
		Set<Coordinates> coordinates = new HashSet<>();
		
		int firstX = target.getX();
		while (firstX > 0
		       && field.get(firstX - 1, target.getY()) == field.get(target.getX(), target.getY())) {
			firstX--;
		}
		int countX = 1;
		while (firstX + countX < field.getWidth()
		       && field.get(firstX + countX, target.getY()) == field.get(target.getX(), target.getY())) {
			countX++;
		}
		
		int firstY = target.getY();
		while (firstY > 0
		       && field.get(target.getX(), firstY - 1) == field.get(target.getX(), target.getY())) {
			firstY--;
		}
		int countY = 1;
		while (firstY + countY < field.getHeight()
		       && field.get(target.getX(), firstY + countY) == field.get(target.getX(), target.getY())) {
			countY++;
		}
		
		if (countX > 2) {
			for (int i = firstX; i < firstX + countX; i++) {
				coordinates.add(new Coordinates(i, target.getY()));
			}
		}
		if (countY > 2) {
			for (int j = firstY; j < firstY + countY; j++) {
				coordinates.add(new Coordinates(target.getX(), j));
			}
		}
		
		return coordinates;
	}
	
	public static CellType getRandomCellValue() {
		CellType[] values = CellType.values();
		return values[rnd.nextInt(values.length)];
	}
	
	public static Field createField(int size) {
		CellType[][] cells = new CellType[size][size];
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				cells[i][j] = getRandomCellValue();
			}
		}
		Field field = new FieldImpl(cells);
		
		while (true) {
			final Set<Coordinates> cellsForDelete = getAllCellsForDelete(field);
			if (cellsForDelete.isEmpty()) {
				break;
			} else {
				field = deleteCells(field, cellsForDelete);
				field = vacuumAndFillField(field);
			}
		}
		
		return field;
	}
}
