package trio.servlets;


import trio.core.ObjectMapperFactory;
import trio.core.Response;
import trio.core.TrioFacade;
import trio.core.TrioFacadeImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Map;


public abstract class AbstractServlet<T> extends HttpServlet {
	private static final TrioFacade trioFacade;
	
	static {
		trioFacade = new TrioFacadeImpl();
		try {
			Naming.rebind("rmi://localhost:31014/TrioFacade", trioFacade);
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}
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
	
	private String serializeResponse(Response response) {
		String s;
		try {
			s = ObjectMapperFactory.createMapper().writerFor(Response.class).writeValueAsString(response);
		} catch (IOException e) {
			s = "Serialize Error. Data: " + response;
		}
		return s;
	}
	
	protected abstract Response<T> handleRequest(TrioFacade trioFacade, Map<String, String[]> params)
	throws Exception;
}
