package eu.ihelp.datacapture.rest.server;

import eu.ihelp.datacapture.rest.resources.JerseyConfiguration;
import eu.ihelp.datacapture.rest.resources.conf.SimpleCORSFilter;
import eu.ihelp.datacapture.rest.server.exceptions.TomcatGenericException;
import eu.ihelp.datacapture.rest.server.utils.TomcatServerProperties;
import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class TomcatServerHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(TomcatServerHandler.class);
    private static final String JERSEY_SERVLET_NAME = "data-capture-tomcat-container-servlet";
    private final int port;
    private final Tomcat tomcat;
    
    public TomcatServerHandler(int port, String swaggerWebAppBase) throws TomcatGenericException {
        super(JERSEY_SERVLET_NAME);
        log.info("ConsoleServer(): Starting the data capture tomcat server");

        this.port = port;
        
        String contextPath = "/ihelp";
        String appBase = ".";
        String swaggerWebContextPath = "";
        if(swaggerWebAppBase==null) {
            swaggerWebAppBase = System.getenv("swagger_path");
            if((swaggerWebAppBase==null) || (swaggerWebAppBase.isEmpty())) {
                log.info("Didn't find an env varibale for swagger base directory");
                swaggerWebAppBase = System.getProperty("swagger_path");
            }
            if((swaggerWebAppBase==null) || (  swaggerWebAppBase.isEmpty())) {        
                log.info("Didn't find an external property for swagger base directory");
                swaggerWebAppBase = appBase + File.separator + "src" + File.separator + "main" + File.separator + "webapp";
            }
        }
        log.info("Swagger base directory is set to: {}", new File(swaggerWebAppBase).getAbsolutePath());
        System.out.println("Swagger base directory is set to: " + new File(swaggerWebAppBase).getAbsolutePath());
        
        try {
             this.tomcat = new Tomcat();
            
            //this.tomcat.getService().addConnector(getSslConnector(TomcatServerProperties.getInstance().getKeyStorePath(), port));
            
            tomcat.setPort(port);  
            tomcat.getHost().setAppBase(appBase);

            Context context = tomcat.addContext(contextPath, appBase);
            Tomcat.addServlet(context, JERSEY_SERVLET_NAME,
                    new ServletContainer(new JerseyConfiguration()));
            context.addServletMappingDecoded("/*", JERSEY_SERVLET_NAME);
            
            
            //code to programmatically add CORS Filter
            Class simpleCorsFilter = SimpleCORSFilter.class;
            String filterName = "cross-origin";
            FilterDef def = new FilterDef();
            def.setFilterName(filterName);
            def.setFilterClass(simpleCorsFilter.getName());
            context.addFilterDef(def);
            FilterMap map = new FilterMap();
            map.setFilterName(filterName);
            map.addURLPattern("/*");
            context.addFilterMap(map);
            //-->end to programmatically add CORS Filter
            
            //now add swagger
            Context webContext = tomcat.addWebapp(swaggerWebContextPath, new File(swaggerWebAppBase).getAbsolutePath());
            
            //and finally start tomcat
            tomcat.start();
        //} catch (LifecycleException | ServletException ex) {
        } catch (LifecycleException ex) {
            ex.printStackTrace();
            log.error("run(): init server(): exception", ex);
            throw new TomcatGenericException(ex);
        }
    }
    
    
    @Override
    public void run() {
        tomcat.getServer().await();
    }
    
    public void shutdown() {
        try {
            this.tomcat.stop();
            this.tomcat.destroy();
        } catch (LifecycleException ex) {
            log.error("shutdown(): exception", ex);
        }
    }
    
    
    @Override
    public String toString() {
      return Integer.toString(port);
    }
    
    
    private static Connector getSslConnector(String path, int port) {
        Connector connector = new Connector();
        connector.setPort(port);
        connector.setSecure(true);
        connector.setScheme("https");
        connector.setAttribute("keyAlias", "lxs.ihelp-project.eu");
        connector.setAttribute("keystorePass", "123456");
        connector.setAttribute("keystoreType", "JKS");
        connector.setAttribute("keystoreFile", path);
        connector.setAttribute("clientAuth", "false");
        connector.setAttribute("protocol", "HTTP/1.1");
        connector.setAttribute("sslProtocol", "TLS");
        connector.setAttribute("maxThreads", "200");
        connector.setAttribute("protocol", "org.apache.coyote.http11.Http11AprProtocol");
        connector.setAttribute("SSLEnabled", true);
        return connector;
     }
}
