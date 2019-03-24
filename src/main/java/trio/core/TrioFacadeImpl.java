package trio.core;


import trio.model.IdGenerator;
import trio.model.field.Coordinates;
import trio.model.field.StepResult;
import trio.model.game.Game;
import trio.model.game.GameImpl;
import trio.model.game.GameRepo;
import trio.model.gamer.Gamer;
import trio.model.gamer.GamerImpl;
import trio.model.gamer.GamerRepo;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.List;
import java.util.function.Predicate;


@Remote(TrioFacade.class)
@Stateless(name = "TrioFacadeImpl")
public class TrioFacadeImpl implements TrioFacade {
	private final GameRepo  gameRepo  = new GameRepo();
	private final GamerRepo gamerRepo = new GamerRepo();
	
	@Override
	public Response<String> createGame() {
		GameImpl game = new GameImpl();
		game.setId(generateId(10, newId -> (gameRepo.getById(newId) == null)));
		game.setField(FieldManipulator.createField(3));
		game.setLastStepResult(new StepResult(List.of(), 0));
		game = gameRepo.save(game);
		return new Response<>(game.getId());
	}
	
	@Override
	public Response<String> connectToGame(String gameId, String gamerName) {
		GameImpl game = gameRepo.getById(gameId);
		if (game == null) {
			return Response.createError("Игры с ID = " + gameId + " не существует.");
		}
		if (game.getStatus() > 0) {
			return Response.createError("Нельзя подключиться к игре с ID = "
			                            + gameId
			                            + ". Её статус = "
			                            + game.getStatus()
			                            + ".");
		}
		if (game.getGamers().stream().map(Gamer::getName).anyMatch(s -> s.equals(gamerName))) {
			return Response.createError("Имя " + gamerName + " уже занято.");
		}
		
		GamerImpl gamer = new GamerImpl();
		gamer.setId(generateId(10, newId -> (gamerRepo.getById(newId) == null)));
		gamer.setName(gamerName);
		gamer.setScore(0);
		gamer = gamerRepo.save(gamer);
		
		game.addGamer(gamer);
		game = gameRepo.save(game);
		
		if (game.getGamers().size() == 2) {
			game.setStatus(1);
			game.setCurrentGamerName(game.getGamers().get(0).getName());
			gameRepo.save(game);
		}
		
		return new Response<>(gamer.getId());
	}
	
	@Override
	public Response<Boolean> canMakeStep(String gameId, String gamerId) {
		if (accessDenied(gameId, gamerId)) {
			return Response.createError("У вас нет прав для выполнения данного действия.");
		}
		
		GameImpl game = gameRepo.getById(gameId);
		if (game.getStatus() != 1) {
			return Response.createError("Игра с ID = "
			                            + gameId
			                            + " не запущена. Её статус = "
			                            + game.getStatus()
			                            + ".");
		}
		
		GamerImpl gamer            = gamerRepo.getById(gamerId);
		boolean   can;
		String    currentGamerName = game.getCurrentGamerName();
		if (currentGamerName == null || currentGamerName.isEmpty()) {
			can = false;
		} else {
			can = game.getCurrentGamerName().equals(gamer.getName());
		}
		return new Response<>(can);
	}
	
	@Override
	public Response<StepResult> makeStep(String gameId, String gamerId, Coordinates source, Coordinates dest) {
		Response<Boolean> canMakeStep = canMakeStep(gameId, gamerId);
		if (canMakeStep.hasError()) {
			return Response.createError(canMakeStep.getErrorText());
		}
		if (!canMakeStep.getData()) {
			return new Response<>(new StepResult(List.of(), 0));
		}
		
		GameImpl game = gameRepo.getById(gameId);
		
		StepResult stepResult = FieldManipulator.move(game.getField(), source, dest);
		if (!stepResult.getStates().isEmpty()) {
			GamerImpl gamer = gamerRepo.getById(gamerId);
			gamer.setScore(gamer.getScore() + stepResult.getScore());
			gamerRepo.save(gamer);
			
			game.setField(stepResult.getStates().get(stepResult.getStates().size() - 1));
			game.setLastStepResult(stepResult);
			game.setStepNumber(game.getStepNumber() + 1);
			if (gamer.getScore() >= 100) {
				game.setStatus(2);
				game.setWinnerGamerName(gamer.getName());
				game.setCurrentGamerName(null);
			} else {
				GamerImpl opponent = game.getOpponent(gamerId);
				if (opponent == null) {
					return Response.createError("У вас нет противника?..");
				}
				game.setCurrentGamerName(opponent.getName());
			}
			gameRepo.save(game);
		}
		
		return new Response<>(stepResult);
	}
	
	@Override
	public Response<Game> getGameState(String gameId, String gamerId) {
		if (accessDenied(gameId, gamerId)) {
			return Response.createError("У вас нет прав для выполнения данного действия.");
		}
		
		GameImpl byId = gameRepo.getById(gameId);
		return new Response<>(byId);
	}
	
	private boolean accessDenied(String gameId, String gamerId) {
		GameImpl game = gameRepo.getById(gameId);
		if (game == null) {
			return true;
		}
		
		GamerImpl gamer = gamerRepo.getById(gamerId);
		if (gamer == null) {
			return true;
		}
		
		return !game.containGamer(gamer.getId());
	}
	
	private String generateId(int length, Predicate<String> validator) {
		String generatedId;
		do {
			generatedId = IdGenerator.generate(length);
		}
		while (!validator.test(generatedId));
		return generatedId;
	}
}