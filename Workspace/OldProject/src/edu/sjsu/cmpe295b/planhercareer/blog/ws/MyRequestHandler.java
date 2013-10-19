package edu.sjsu.cmpe295b.planhercareer.blog.ws;

import java.net.InetAddress;
import java.util.Properties;

import org.apache.wink.server.handlers.HandlersChain;
import org.apache.wink.server.handlers.MessageContext;
import org.apache.wink.server.handlers.RequestHandler;

import org.apache.log4j.Logger;

public class MyRequestHandler implements RequestHandler 
{
	private Properties _props = null;
		
	public static Logger LOG = Logger.getLogger(MyRequestHandler.class);
	
	public static final String LOGIN_ID_PARAM_KEY = "login_id";
	public static final String STRAND_PARAM_KEY = "strand_name";

	private boolean isInitialized = false;
	
	@Override
	public void init(Properties props)
	{
		LOG.info("Init Properties is :" + props);
		
		_props = props;
		String localHostName = "localhost";
		
		try
		{
			localHostName = InetAddress.getLocalHost().getHostName();
		} catch (Exception ex) {
			ex.printStackTrace();
			localHostName = "localhost";
		}
		
		props.setProperty(ZkResourceConfig.SERVER_HOST_KEY, localHostName);
		
	}

	@Override
	public synchronized void handleRequest(MessageContext ctxt, HandlersChain chain)
			throws Throwable 
	{	
		LOG.info("handle Request called for ctxt :" + ctxt);

		if ( !isInitialized)
		{
			LOG.info("Initializing the ZKClient !!");
			isInitialized = true;
			int localPort = ctxt.getUriInfo().getAbsolutePath().getPort();
			_props.setProperty(ZkResourceConfig.SERVER_PORT_KEY, "" + localPort);
			ZkResourceConfig.initialize(_props);
		}
		
		chain.doChain(ctxt);
	}
	
	/*
		if ( null == _strandZkNode )
		{
			_localPort = ctxt.getUriInfo().getAbsolutePath().getPort();
			_nodeName = _localHostName + ":" + _localPort;
			_strandZkNode = new ZkStrandNodeData(_nodeName, _isPartitioned, _beginUserId, _endUserId, null);
			
			_blogDecisionMaker = StrandServiceDecisionMaker.getInstance(_serverBlogStrandConfig, _strandZkNode);
			_groupDecisionMaker = StrandServiceDecisionMaker.getInstance(_serverGroupStrandConfig, _strandZkNode);
			_albumDecisionMaker = StrandServiceDecisionMaker.getInstance(_serverAlbumStrandConfig, _strandZkNode);
		}
		
		String loginId = ctxt.getUriInfo().getQueryParameters().getFirst(LOGIN_ID_PARAM_KEY);
		String strand = ctxt.getUriInfo().getQueryParameters().getFirst(STRAND_PARAM_KEY);
		
		if ( ((null == loginId) || (loginId.trim().isEmpty()))
			|| ((null == strand) || (strand.trim().isEmpty())))
		{
			// not enough info. pass through
			chain.doChain(ctxt);
			return;
		} else {
			int id = -1;
			
			try
			{
				id = Integer.parseInt(loginId);
			} catch (Exception ex) {
				id = loginId.hashCode();
			}
			
			ZkStrandNodeData node = null;
			if ( BLOG_STRAND_NAME.equalsIgnoreCase(strand))
			{
				// this is a blog Strand
				node = _blogDecisionMaker.getRandomStrandInstanceWithDefaultPreferred(id);
			} else if (GROUP_STRAND_NAME.equalsIgnoreCase(strand)) {
				// this is a group strand
				node = _groupDecisionMaker.getRandomStrandInstanceWithDefaultPreferred(id);
			} else if (ALBUM_STRAND_NAME.equalsIgnoreCase(strand)) {
				// this is a album strand
				node = _albumDecisionMaker.getRandomStrandInstanceWithDefaultPreferred(id);
			} else {
				// unknown - pass through
				chain.doChain(ctxt);
				return;
			}
			
			if ( (null != node ) && (null != _nodeName))
			{
				if ( _nodeName.equalsIgnoreCase(node.getNodeName()))
				{
					// local instance will be servicing this
					chain.doChain(ctxt);
					return;
				} else {
					// Forward the request - How ?
					ctxt.
				}
			} else {
				// pass through
				chain.doChain(ctxt);
				return;
			}
		}
	}
	*/

}
