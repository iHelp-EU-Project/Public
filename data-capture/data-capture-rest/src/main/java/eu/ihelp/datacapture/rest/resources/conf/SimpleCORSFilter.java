package eu.ihelp.datacapture.rest.resources.conf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class SimpleCORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Cache-Control");
		chain.doFilter(req, res);
                
                //System.out.println("filter has been invoked!");
                
                final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                final byte [] bytes = buffer.toByteArray();
                if(bytes.length>0) {
                    response.getOutputStream().write(bytes);
                }
                response.setContentLength(bytes.length);
                response.flushBuffer();
                  
	}

    @Override
    public void destroy() {
        //DO NOTHING
        return;
    }
    
    
    
}
