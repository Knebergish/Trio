package trio.model.gamer;


import trio.model.AbstractRepo;


public class GamerRepo extends AbstractRepo<String, GamerImpl> {
	@Override
	protected Class<GamerImpl> getEntityClass() {
		return GamerImpl.class;
	}
}
