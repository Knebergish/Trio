package trio.servlets;


import trio.core.ObjectMapperFactory;
import trio.core.Response;
import trio.core.TrioFacade;

import javax.ejb.EJB;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


@SuppressWarnings("WeakerAccess")
public abstract class AbstractServlet<T> extends HttpServlet {
	@EJB
	private TrioFacade trioFacade;
	
	protected AbstractServlet() {
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		Response<T> r;
		try {
			r = handleRequest(trioFacade, request.getParameterMap());
		} catch (Exception e) {
			r = Response.createError(e.getMessage());
		}
		writer.println(serializeResponse(r));
		writer.close();
	}
	
	protected abstract Response<T> handleRequest(TrioFacade trioFacade, Map<String, String[]> params)
	throws Exception;
	
	private String serializeResponse(Response response) {
		String s;
		try {
			s = ObjectMapperFactory.createMapper().writerFor(Response.class).writeValueAsString(response);
		} catch (IOException e) {
			s = "Serialize Error. Data: " + response;
		}
		return s;
	}
	
	protected String getParam(Map<String, String[]> params, String name) throws Exception {
		try {
			return params.get(name)[0];
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new Exception("Отсутствует необходимый параметр " + name);
		}
	}
}
