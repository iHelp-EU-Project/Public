package eu.ihelp.store.services.model;

import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class IhelpPatient {
    
    private final List<PatientProvider> patientProviders;

    public IhelpPatient(List<PatientProvider> patientProvider) {
        this.patientProviders = patientProvider;
    }

    public List<PatientProvider> getPatientProviders() {
        return patientProviders;
    }

    @Override
    public String toString() {
        return "IhelpPatient{" + "patientProvider=" + patientProviders + '}';
    }
    
    public String toWhereClause() {
        final StringBuilder sb = new StringBuilder(" WHERE ");
        for(PatientProvider pp : this.patientProviders) {
            if(pp.isHealthentia()) {
                continue;
            }
            
            sb.append(" ( ");
            sb.append(" PATIENTID = '").append(pp.getPatientID()).append("'");
            sb.append(" AND PROVIDERID = '").append(pp.getProviderID()).append("'");
            sb.append(" ) ");
            sb.append(" OR ");
        }
        return sb.toString().substring(0, sb.toString().length()-" OR ".length());
    }
}
