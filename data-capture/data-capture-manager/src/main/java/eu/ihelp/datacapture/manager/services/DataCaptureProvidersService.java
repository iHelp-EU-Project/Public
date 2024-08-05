package eu.ihelp.datacapture.manager.services;

import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public interface DataCaptureProvidersService {
    
    public List<String> getDataProviders();
    
    public List<String> getPrimaryDataProviders();
    
    public List<String> getSecondaryDataProviders();
}
