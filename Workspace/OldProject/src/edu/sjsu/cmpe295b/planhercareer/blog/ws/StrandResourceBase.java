package edu.sjsu.cmpe295b.planhercareer.blog.ws;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import edu.sjsu.cmpe275.strand.zkclient.StrandServiceDecisionMaker;
import edu.sjsu.cmpe275.strand.zkclient.ZkStrandNodeData;

public class StrandResourceBase 
{
	public static final String LOGIN_ID_PARAM_KEY = "login_id";
	
	public static Logger LOG = Logger.getLogger(StrandResourceBase.class);

	private String _strand;
	
	public StrandResourceBase(String strand)
	{
		_strand = strand;
		LOG.info("Creating StrandResourceBase for strand :" + _strand);
	}

	public ZkStrandNodeData selectNodeForService(String loginId)
	{
		StrandServiceDecisionMaker decider = getDecisionMaker();

		if ( null == loginId)
		{
			LOG.info("Passed loginId is null !!");
			return decider.getDefaultStrandInstance();
		}
		
		int id = -1;
		
		try
		{
			id = Integer.parseInt(loginId);
		} catch (Exception ex) {
			id = loginId.hashCode();
		}
		
		ZkStrandNodeData node = decider.getRandomStrandInstanceWithDefaultPreferred(id);
		
		LOG.info("Selected Node for user (" + loginId +") is :" + node);
		if ( null == node)
			return decider.getDefaultStrandInstance();
		
		return node;
	}
	
	public boolean isLocalNode(ZkStrandNodeData node)
	{
		if ( null == node )
		{
			LOG.info("Node is null !!");
			return true;
		}
		
		String localNodeName = ZkResourceConfig.getInstance().getNodeName();
		
		if (localNodeName.equalsIgnoreCase(node.getNodeName()))
		{
			LOG.info("Passed Node is the local node !!");
			return true;
		}
		
		LOG.info("Passed node is not the local node !!");
		return false;
	}
	
	public URI getRedirectUrl(UriInfo uriInfo, ZkStrandNodeData node)
	{	
		String newNodeName = node.getNodeName();
		
		String[] hostPort = newNodeName.split(":");		
		String newHost = hostPort[0];
		String newPortStr = hostPort[1];
		int newPort = Integer.valueOf(newPortStr);

		URI uri = uriInfo.getAbsolutePath();
		URI newUri;
		try {
			newUri = new URI(uri.getScheme(),uri.getUserInfo(),newHost,newPort, uri.getPath(),uri.getQuery(), uri.getFragment());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			try
			{
				// Might be because of Host issue. So, try with default host
				newUri = new URI(uri.getScheme(),uri.getUserInfo(),"localhost",newPort, uri.getPath(),uri.getQuery(), uri.getFragment());
			} catch (URISyntaxException e2) {
				e2.printStackTrace();
				throw new RuntimeException(e2);
			}
		}
		
		LOG.info("The redirect URI is :" + newUri);
		return newUri;
	}
	
	private StrandServiceDecisionMaker getDecisionMaker()
	{
		return StrandServiceDecisionMaker.getInstance(_strand);
	}
}
