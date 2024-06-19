package ice.mapper.secondarydata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import ice.mapper.secondarydata.converter.FhirConverter;
import ice.mapper.secondarydata.server.FhirServer;

@SpringBootApplication
public class SecondarydataApplication {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private FhirConverter converter;

	public static void main(String[] args) {
		SpringApplication.run(SecondarydataApplication.class, args);
	}

	@Bean 
	public ServletRegistrationBean<FhirServer> servletRegistrationBean() {
		return new ServletRegistrationBean<>(new FhirServer(context,converter), "/fhir/secondary/*");
	}

}
