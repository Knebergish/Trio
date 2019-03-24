package trio.servlets;


import trio.core.Response;
import trio.core.TrioFacade;

import javax.servlet.annotation.WebServlet;
import java.rmi.RemoteException;
import java.util.Map;


@WebServlet("/createGame")
public class CreateGameServlet extends AbstractServlet<String> {
	@Override
	protected Response<String> handleRequest(TrioFacade trioFacade, Map<String, String[]> params)
	throws RemoteException {
		return trioFacade.createGame();
	}
}
