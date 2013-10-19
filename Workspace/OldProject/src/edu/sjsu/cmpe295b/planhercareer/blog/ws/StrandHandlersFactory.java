package edu.sjsu.cmpe295b.planhercareer.blog.ws;

import java.util.ArrayList;
import java.util.List;

import org.apache.wink.server.handlers.HandlersFactory;
import org.apache.wink.server.handlers.RequestHandler;

public class StrandHandlersFactory 
	extends HandlersFactory 
{
	private List<RequestHandler> _requestHandler = new ArrayList<RequestHandler>();
	
	public StrandHandlersFactory()
	{
		super();
		_requestHandler.add(new MyRequestHandler());
	}
	@Override
	public List<? extends RequestHandler> getRequestHandlers() 
	{
		return _requestHandler;
	}

}
