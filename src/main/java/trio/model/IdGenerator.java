package trio.model;


import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public final class IdGenerator {
	private IdGenerator() {
	}
	
	public static String generate(int length) {
		String symbols = "abcdefghijklmnopqrstuvwxyz1234567890";
		Random rnd     = new Random();
		return IntStream.generate(() -> rnd.nextInt(symbols.length()))
		                .limit(length)
		                .mapToObj(symbols::charAt)
		                .map(String::valueOf)
		                .collect(Collectors.joining());
	}
}
