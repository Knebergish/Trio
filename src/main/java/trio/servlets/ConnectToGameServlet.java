package trio.servlets;


import trio.core.Response;
import trio.core.TrioFacade;

import javax.servlet.annotation.WebServlet;
import java.rmi.RemoteException;
import java.util.Map;

@WebServlet("/connectToGame")
public class ConnectToGameServlet extends AbstractServlet<String> {
	@Override
	protected Response<String> handleRequest(TrioFacade trioFacade, Map<String, String[]> params)
	throws RemoteException {
		return trioFacade.connectToGame(params.get("gameId")[0], params.get("gamerName")[0]);
	}
}
