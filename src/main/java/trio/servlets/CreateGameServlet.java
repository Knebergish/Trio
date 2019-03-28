package trio.servlets;


import trio.core.Response;
import trio.core.TrioFacade;

import javax.servlet.annotation.WebServlet;
import java.util.Map;


@WebServlet("/createGame")
public class CreateGameServlet extends AbstractServlet<String> {
	@Override
	protected Response<String> handleRequest(TrioFacade trioFacade, Map<String, String[]> params)
	throws Exception {
		return trioFacade.createGame(parseInt(params.get("width")[0]),
		                             parseInt(params.get("height")[0]));
	}
	
	private int parseInt(String text) {
		return Integer.parseInt(text);
	}
}
