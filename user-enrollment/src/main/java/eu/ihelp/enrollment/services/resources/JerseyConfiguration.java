package eu.ihelp.enrollment.services.resources;

import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class JerseyConfiguration extends ResourceConfig {
    
    public JerseyConfiguration() {
        packages(this.getClass().getPackage().getName());
    }
    
}
