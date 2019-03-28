package trio.servlets;


import trio.core.Response;
import trio.core.TrioFacade;

import javax.servlet.annotation.WebServlet;
import java.util.Map;


@WebServlet("/connectToGame")
public class ConnectToGameServlet extends AbstractServlet<String> {
	@Override
	protected Response<String> handleRequest(TrioFacade trioFacade, Map<String, String[]> params)
	throws Exception {
		return trioFacade.connectToGame(getParam(params, "gameId"),
		                                getParam(params, "gamerName"));
	}
}
