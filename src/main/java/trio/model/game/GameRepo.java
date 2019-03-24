package trio.model.game;


import trio.model.AbstractRepo;


public class GameRepo extends AbstractRepo<String, GameImpl> {
	@Override
	protected Class<GameImpl> getEntityClass() {
		return GameImpl.class;
	}
}
