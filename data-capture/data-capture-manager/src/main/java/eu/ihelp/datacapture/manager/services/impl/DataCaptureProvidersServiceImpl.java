package eu.ihelp.datacapture.manager.services.impl;

import eu.ihelp.datacapture.commons.DataProviders;
import eu.ihelp.datacapture.manager.services.DataCaptureProvidersService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class DataCaptureProvidersServiceImpl implements DataCaptureProvidersService {
    private final List<String> primaryDataProviders;
    private final List<String> secondaryDataProviders;
    private final List<String> dataProviders;
    

    private DataCaptureProvidersServiceImpl(List<String> primaryDataProviders, List<String> secondaryDataProviders, List<String> dataProviders) {
        this.primaryDataProviders = primaryDataProviders;
        this.secondaryDataProviders = secondaryDataProviders;
        this.dataProviders = dataProviders;
    }
        
    
    private static final class DataCaptureProvidersServiceImplHolder {
        private static final DataCaptureProvidersServiceImpl INSTANCE = buildInstance();
        
        static DataCaptureProvidersServiceImpl buildInstance() {
            return new DataCaptureProvidersServiceImpl(
                    getPrimaryDataProviders(), 
                    getSecondaryDataProviders(), 
                    getDataProviders());
        }        
        
        static List<String> getDataProviders() {
            List<String> primaryDataProviders = getPrimaryDataProviders();
            List<String> secondaryDataProviders = getSecondaryDataProviders();

            List<String> result = new ArrayList<>(primaryDataProviders.size() + secondaryDataProviders.size());
            result.addAll(primaryDataProviders);
            result.addAll(secondaryDataProviders);

            return result;
        }

        
        static List<String> getPrimaryDataProviders() {
            List<String> list = new ArrayList<>(DataProviders.PrimaryDataProviders.values().length);
            for(DataProviders.PrimaryDataProviders primaryDataProviders : DataProviders.PrimaryDataProviders.values()) {
                list.add(primaryDataProviders.name());
            }

            return list;
        }

        
        static List<String> getSecondaryDataProviders() {
            List<String> list = new ArrayList<>(DataProviders.SecondaryDataProviders.values().length);
            for(DataProviders.SecondaryDataProviders secondaryDataProviders : DataProviders.SecondaryDataProviders.values()) {
                list.add(secondaryDataProviders.name());
            }

            return list;
        }
    }
    
    public static DataCaptureProvidersServiceImpl getInstance() {
        return DataCaptureProvidersServiceImplHolder.INSTANCE;
    }

    @Override
    public List<String> getDataProviders() {
        return this.dataProviders;
    }

    @Override
    public List<String> getPrimaryDataProviders() {
        return this.primaryDataProviders;
    }

    @Override
    public List<String> getSecondaryDataProviders() {
        return this.secondaryDataProviders;
    }    
}
