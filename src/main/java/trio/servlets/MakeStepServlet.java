package trio.servlets;


import trio.core.ObjectMapperFactory;
import trio.core.Response;
import trio.core.TrioFacade;
import trio.model.field.Coordinates;
import trio.model.field.StepResult;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.Map;


@WebServlet("/makeStep")
public class MakeStepServlet extends AbstractServlet<StepResult> {
	@Override
	protected Response<StepResult> handleRequest(TrioFacade trioFacade, Map<String, String[]> params)
	throws Exception {
		return trioFacade.makeStep(getParam(params, "gameId"),
		                           getParam(params, "gamerId"),
		                           deserializeCoordinates(getParam(params, "source")),
		                           deserializeCoordinates(getParam(params, "dest")));
	}
	
	private Coordinates deserializeCoordinates(String value) throws IOException {
		return ObjectMapperFactory.createMapper().readerFor(Coordinates.class).readValue(value);
	}
}
