package eu.ihelp.enrollment.services.utils;

import eu.ihelp.enrollment.utils.IhelpServerProperties;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCaptureClientBuilder {
    
    public static DataCaptureClient createInstance(MUPPrimaryDataParser dataParser) {
        return new DataCaptureClient(IhelpServerProperties.getInstance().getDataCaptureUrl(), IhelpServerProperties.getInstance().getDataCapturePort(), dataParser);
    }
}
