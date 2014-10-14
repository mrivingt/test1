

	
	

	
	/*
	 * Client.java - JMX client that interacts with the JMX agent. It gets
	 * attributes and performs operations on the Hello MBean and the QueueSampler
	 * MXBean example. It also listens for Hello MBean notifications.
	 */

	

	
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
	        // Create an RMI connector client and
	        // connect it to the RMI connector server
	        //
	        //echo("\nCreate an RMI connector client and " +
	        //     "connect it to the RMI connector server");
	    	
	    	
	    	/* Local attach method
	    	 
	        String pid = null;

	        List<VirtualMachineDescriptor> vms = VirtualMachine.list();
	        for (VirtualMachineDescriptor virtualMachineDescriptor : vms) {
	
	            if (virtualMachineDescriptor.displayName().equals("")) {
	            	pid = virtualMachineDescriptor.id();
	            	break;
	            }
	            
	        }
        	//echo("My pid: " + pid);

	        VirtualMachine vm = VirtualMachine.attach(pid);

	        
	        String connectorAddr = vm.getAgentProperties().getProperty("com.sun.management.jmxremote.localConnectorAddress");
	        
	        if (connectorAddr == null) {
	        	echo("Loading management agent");
	            String agent = vm.getSystemProperties().getProperty(
	              "java.home")+File.separator+"lib"+File.separator+
	              "management-agent.jar";
	            vm.loadAgent(agent);
	            connectorAddr = vm.getAgentProperties().getProperty(
	              "com.sun.management.jmxremote.localConnectorAddress");
	          }
            
	        JMXServiceURL serviceURL = new JMXServiceURL(connectorAddr);
	        
	        */
	        
	        JMXServiceURL serviceURL = new JMXServiceURL(
	                "service:jmx:rmi:///jndi/rmi://127.0.0.1:7199/jmxrmi");
	        JMXConnector jmxc = JMXConnectorFactory.connect(serviceURL); 


	        // Get an MBeanServerConnection
	        //
	        //echo("\nGet an MBeanServerConnection");
	        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
	

	        // Get domains from MBeanServer
	        //
	        //echo("\nDomains:");
	        //String domains[] = mbsc.getDomains();
	        //Arrays.sort(domains);
	        //for (String domain : domains) {
	        //   echo("\tDomain = " + domain);
	        //}
	
	        // Get MBeanServer's default domain
	        //
	        //echo("\nMBeanServer default domain = " + mbsc.getDefaultDomain());

	        // Get MBean count
	        //
	        //echo("\nMBean count = " + mbsc.getMBeanCount());

	        // Query MBean names
	        //
	        //echo("\nQuery MBeanServer MBeans:");
	        
	        String searchName = "org.apache.cassandra.metrics:type=Cache,scope=KeyCache,name=HitRate";
	        String attributeName = "Value";
	        String displayName ="Cassandra Key Cache hit ratio: ";
	        String source = "DellXPS";
	        
	        //getMbean(mbsc, searchName, attributeName, displayName);
	        
	        searchName = "org.apache.cassandra.internal:type=FlushWriter";
	        attributeName = "CompletedTasks";
	        displayName ="Cassandra Flush Writer completed tasks: ";
	        //getMbean(mbsc, searchName, attributeName, displayName);
	     
	        for (int i = 0; i < 1000; i++) {
	        
	        	searchName = "org.apache.cassandra.internal:type=FlushWriter";
	        	attributeName = "CompletedTasks";  // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< should be PendingTasks
	        	displayName ="Cassandra Flush Writer pending tasks: ";
	        	displayName = "CAS_FW_PT";
	        
	        	getMbean(mbsc, searchName, attributeName, displayName, source);
	        	
	        	Thread.sleep(1000);
	        }

	        jmxc.close();
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






