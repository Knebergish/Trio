package trio.servlets;


import trio.core.Response;
import trio.core.TrioFacade;

import javax.servlet.annotation.WebServlet;
import java.util.Map;


@WebServlet("/canMakeStep")
public class CanMakeStepServlet extends AbstractServlet<Boolean> {
	@Override
	protected Response<Boolean> handleRequest(TrioFacade trioFacade, Map<String, String[]> params)
	throws Exception {
		return trioFacade.canMakeStep(getParam(params, "gameId"),
		                              getParam(params, "gamerId"));
	}
}
