package edu.sjsu.cmpe295b.planhercareer.ws;

import java.util.Properties;

import edu.sjsu.cmpe275.strand.zkclient.StrandServiceDecisionMaker;
import edu.sjsu.cmpe275.strand.zkclient.ZkServerStrandConfig;
import edu.sjsu.cmpe275.strand.zkclient.ZkStrandNodeData;

public class ZkResourceConfig 
{
	public static final String ZK_SERVER_HOST_KEY = "zk_host";
	public static final String ZK_SERVER_PORT_KEY = "zk_port";
	public static final String IS_PARTITIONED_KEY = "partitioned";
	public static final String BEGIN_USER_ID_KEY = "begin_userid";
	public static final String END_USER_ID_KEY = "end_userid";
	
	// generated Properties
	public static final String SERVER_HOST_KEY = "host";
	public static final String SERVER_PORT_KEY = "port";
	
	public static final String DEFAULT_ZK_HOST = "localhost";
	public static final String DEFAULT_HOST = "localhost";
	public static final String DEFAULT_ZK_PORT = "2181";
	public static final int DEFAULT_PORT = 80;
	public static final boolean DEFAULT_IS_PARTITIONED = false;
	public static final int DEFAULT_BEGIN_USER_ID = 0;
	public static final int DEFAULT_END_USER_ID = 100;

	private String _localHostName = "localhost";
	private int _localPort = 8080;
	private String _nodeName;
	private boolean _isPartitioned;
	private int _beginUserId;
	private int _endUserId;
	
	private boolean isInitialized =false;
	
	private static ZkResourceConfig _sResourceConfig = new ZkResourceConfig();
	
	private ZkResourceConfig()
	{}
	
	public void init(Properties props)
	{
		String zkHost = props.getProperty(ZK_SERVER_HOST_KEY);
		String zkPort = props.getProperty(ZK_SERVER_PORT_KEY);
		String isPartitionedStr = props.getProperty(IS_PARTITIONED_KEY);
		String beginUserIdStr = props.getProperty(BEGIN_USER_ID_KEY);
		String endUserIdStr = props.getProperty(END_USER_ID_KEY);
		String hostStr = props.getProperty(SERVER_HOST_KEY);
		String portStr = props.getProperty(SERVER_PORT_KEY);
		
		_isPartitioned = false;
		_beginUserId = DEFAULT_BEGIN_USER_ID;
		_endUserId = DEFAULT_END_USER_ID;
		
		if ( (null == zkHost) || (zkHost.trim().isEmpty()))
			zkHost = DEFAULT_ZK_HOST;
		
		if ( (null == zkPort) || (zkPort.trim().isEmpty()))
			zkPort = DEFAULT_ZK_PORT;
		
		if ((null == isPartitionedStr) || (isPartitionedStr.trim().isEmpty()))
		{
			_isPartitioned = DEFAULT_IS_PARTITIONED;
		} else {
			try
			{
				_isPartitioned = Boolean.parseBoolean(isPartitionedStr);
			} catch ( Exception ex) {
				_isPartitioned = DEFAULT_IS_PARTITIONED;
			}
		}
		
		if ( (null == beginUserIdStr) || ( beginUserIdStr.trim().isEmpty()))
		{
			_beginUserId = DEFAULT_BEGIN_USER_ID;
		} else {
			try
			{
				_beginUserId = Integer.valueOf(beginUserIdStr);
			} catch (Exception ex) {
				_beginUserId = DEFAULT_BEGIN_USER_ID;
			}
		}
		
		if ( (null == endUserIdStr) || ( endUserIdStr.trim().isEmpty()))
		{
			_endUserId = DEFAULT_END_USER_ID;
		} else {
			try
			{
				_endUserId = Integer.valueOf(endUserIdStr);
			} catch (Exception ex) {
				_endUserId = DEFAULT_END_USER_ID;
			}
		}
		
		if ((null == hostStr) || ((hostStr.trim().isEmpty())))
		{
			_localHostName = DEFAULT_HOST;
		} else {
			_localHostName = hostStr;
		}
		
		if ((null == portStr) || ((portStr.trim().isEmpty())))
		{
			_localPort = DEFAULT_PORT;
		} else {
			try
			{
				_localPort = Integer.valueOf(portStr);
			} catch (Exception ex) {
				_localPort = DEFAULT_PORT;
			}
		}
		_nodeName = _localHostName + ":" + _localPort;
		isInitialized = true;
		ZkStrandNodeData strandZkNode = new ZkStrandNodeData(_nodeName, _isPartitioned, _beginUserId, _endUserId, null);
		ZkServerStrandConfig serverConfig = new ZkServerStrandConfig(zkHost, zkPort);
		
		StrandServiceDecisionMaker.initialize(serverConfig, strandZkNode);		
	}

	
	public String getLocalHostName() {
		return _localHostName;
	}

	public int getLocalPort() {
		return _localPort;
	}

	public String getNodeName() {
		return _nodeName;
	}

	public boolean isPartitioned() {
		return _isPartitioned;
	}

	public int getBeginUserId() {
		return _beginUserId;
	}

	public int getEndUserId() {
		return _endUserId;
	}

	public static ZkResourceConfig get_sResourceConfig() {
		return _sResourceConfig;
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public static synchronized void initialize(Properties props)
	{
		_sResourceConfig.init(props);
	}
	
	public static synchronized ZkResourceConfig getInstance()
	{	
		return _sResourceConfig;
	}
}
