package eu.ihelp.enrollment.utils;

import com.wordnik.swagger.jaxrs.config.BeanConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class SwaggerBootStrap extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0-SNAPSHOT");
        beanConfig.setBasePath("/ihelp/*");
        beanConfig.setResourcePackage("eu.ihelp.enrollment.services.resources");
        beanConfig.setScan(true);
       
    }
       
}
