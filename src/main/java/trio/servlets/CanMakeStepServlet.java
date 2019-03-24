package trio.servlets;


import trio.core.Response;
import trio.core.TrioFacade;

import javax.servlet.annotation.WebServlet;
import java.rmi.RemoteException;
import java.util.Map;

@WebServlet("/canMakeStep")
public class CanMakeStepServlet extends AbstractServlet<Boolean> {
	@Override
	protected Response<Boolean> handleRequest(TrioFacade trioFacade, Map<String, String[]> params)
	throws RemoteException {
		return trioFacade.canMakeStep(params.get("gameId")[0], params.get("gamerId")[0]);
	}
}
