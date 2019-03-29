package trio.model;


import trio.model.field.CellType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public final class RandomGenerator {
	private static final Random rnd = new Random();
	
	private RandomGenerator() {
	}
	
	public static String generateId(int length) {
		String symbols = "abcdefghijklmnopqrstuvwxyz1234567890";
		
		return IntStream.generate(() -> rnd.nextInt(symbols.length()))
		                .limit(length)
		                .mapToObj(symbols::charAt)
		                .map(String::valueOf)
		                .collect(Collectors.joining());
	}
	
	public static Map<CellType, Integer> generateCosts() {
		CellType[] values = CellType.values();
		List<Integer> costs = IntStream.range(0, values.length)
		                               .boxed()
		                               .collect(Collectors.toList());
		Collections.shuffle(costs);
		
		var costsMap = new HashMap<CellType, Integer>(values.length);
		for (int i = 0; i < values.length; i++) {
			costsMap.put(values[i], costs.get(i));
		}
		return costsMap;
	}
}
