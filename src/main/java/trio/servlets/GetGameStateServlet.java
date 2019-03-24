package trio.servlets;


import trio.core.Response;
import trio.core.TrioFacade;
import trio.model.game.Game;

import javax.servlet.annotation.WebServlet;
import java.util.Map;

@WebServlet("/getGameState")
public class GetGameStateServlet extends AbstractServlet<Game> {
	@Override
	protected Response<Game> handleRequest(TrioFacade trioFacade, Map<String, String[]> params) throws Exception {
		return trioFacade.getGameState(params.get("gameId")[0], params.get("gamerId")[0]);
	}
}
