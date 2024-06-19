package ice.mapper.secondarydata.server;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import org.springframework.context.ApplicationContext;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ice.mapper.secondarydata.converter.FhirConverter;


@WebServlet(name = "FhirServlet", description = "Fhir server for secondary data", urlPatterns = {"/fhir/secondary/*"})
public class FhirServer extends RestfulServer {
  
  private static final long serialVersionUID = 1L;

  private final ApplicationContext applicationContext;

  
  public FhirServer(ApplicationContext context, FhirConverter converter) {
    
    this.applicationContext = context;

    registerProvider(new FhirProvider(converter));
  }

  @Override
  protected void initialize() throws ServletException {

    super.initialize();
    setFhirContext(FhirContext.forR4());


    OpenApiInterceptor openApiInterceptor = new OpenApiInterceptor();
    registerInterceptor(openApiInterceptor);

  }

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }
}
