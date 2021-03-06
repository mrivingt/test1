	/*
	 * Initial attempt to get JMX data out of Cassandra. Uses local host port 7199 to connect.
	 */
	
	import java.net.InetAddress;
	import java.net.UnknownHostException;
	import java.util.Set;
	import javax.management.MBeanServerConnection;
	import javax.management.ObjectInstance;
	import javax.management.ObjectName;
	import javax.management.remote.JMXConnector;
	import javax.management.remote.JMXConnectorFactory;
	import javax.management.remote.JMXServiceURL;
	
		public class CassandraJMX {

	    /* For simplicity, we declare "throws Exception".
	       Real programs will usually want finer-grained exception handling. */
	    public static void main(String[] args) throws Exception {
	     
	    	String hostname = "Unknown";

	    	try
	    	{
	    	    InetAddress addr;
	    	    addr = InetAddress.getLocalHost();
	    	    hostname = addr.getHostName();
	    	}
	    	catch (UnknownHostException ex)
	    	{
	    	    echo("Hostname can not be resolved");
	    	}
	        
	        JMXServiceURL serviceURL = new JMXServiceURL(
	                "service:jmx:rmi:///jndi/rmi://localhost:7199/jmxrmi");
	        JMXConnector jmxc = JMXConnectorFactory.connect(serviceURL); 


	        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
	
	        String source = hostname;
	        String searchName, displayName, attributeName;        
	
	        
	        while (true) {   // Forever
	        
	        	searchName = "org.apache.cassandra.internal:type=FlushWriter";
	        	attributeName = "PendingTasks";  
	        	displayName = "CAS_FW_PT";
	        		        
	        	getMbean(mbsc, searchName, attributeName, displayName, source);
	        	
	        	searchName = "org.apache.cassandra.internal:type=FlushWriter";
	        	attributeName = "CompletedTasks";  
	        	displayName = "CAS_FW_CT";
	        		        
	        	getMbean(mbsc, searchName, attributeName, displayName, source);
	        	
	        	Thread.sleep(1000);
	        	
	        }

	        // jmxc.close();
	    }
	    
	    private static void getMbean(MBeanServerConnection mbsc, String searchName, String attributeName, 
	    		String displayName, String source) throws Exception
	    {
	    	ObjectName myMbeanName = new ObjectName(searchName);
        	
        	Set<ObjectInstance> mbeans  = mbsc.queryMBeans(myMbeanName,null);    // This should only return 1 instance
        	if (!mbeans.isEmpty()) {
    
   		        for (ObjectInstance name: mbeans) {
        			echo(displayName + " " + mbsc.getAttribute( name.getObjectName(), attributeName).toString() + " " + source);
        		}
            	
        	}
        	else { echo("Unable to locate MBean: "+ searchName);}
	    	
	    }

	    private static void echo(String msg) {
	        System.out.println(msg);
	    }

	
	}






