package eu.ihelp.datacapture.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectorArgumentsHealthentia extends ConnectorArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the url of healthentia API to get the data", required=true)
    private final String url;
    @ApiModelProperty(value="the username to connecto the healthentia", required=true)
    private final String username;
    @ApiModelProperty(value="the passwrod to connecto the healthentia", required=true)
    private final String password;
    @ApiModelProperty(value="the dataset to connecto the healthentia", required=true)
    private final String dataset;
    @ApiModelProperty(value="the list of studies that we want to get the data from", required=true)
    private final List<Integer> studies;
    @ApiModelProperty(value="the list of studies that we want to get the data from", required=false)
    private final boolean fromStart;

    @JsonCreator
    public ConnectorArgumentsHealthentia(
            @JsonProperty("url") String url, 
            @JsonProperty("username") String username, 
            @JsonProperty("password") String password, 
            @JsonProperty("datePattern") String datePattern,
            @JsonProperty("timePattern")  String timePattern,
            @JsonProperty("dataset") String dataset,
            @JsonProperty("studies") List<Integer> studies,
            @JsonProperty("fromStart") boolean fromStart) {
        super(ConnectorTypeEnum.HEALTHENTIA, datePattern, timePattern, null);
        this.url = url;
        this.username = username;
        this.password = password;
        this.dataset = dataset;
        this.studies = studies;
        this.fromStart = fromStart;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDataset() {
        return dataset;
    }
    
    public List<Integer> getStudies() {
        return studies;
    }

    public boolean isFromStart() {
        return fromStart;
    }

    @Override
    public String toString() {
        return "ConnectorArgumentsHealthentia{" + "url=" + url + ", username=" + username + ", password=" + password + ", dataset=" + dataset + ", studies=" + studies + ", fromStart=" + fromStart + '}';
    }
    
    
    
    
    
}
