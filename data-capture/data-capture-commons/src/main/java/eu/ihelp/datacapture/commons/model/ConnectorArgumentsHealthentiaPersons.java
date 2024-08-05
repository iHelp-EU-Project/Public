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
public class ConnectorArgumentsHealthentiaPersons extends ConnectorArgumentsAbstract {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="the url of healthentia API to get the data", required=true)
    private final String url;
    @ApiModelProperty(value="the username to connecto the healthentia", required=true)
    private final String username;
    @ApiModelProperty(value="the passwrod to connecto the healthentia", required=true)
    private final String password;
    @ApiModelProperty(value="the list of studies that we want to get the data from", required=true)
    private final List<Integer> studies;

    @JsonCreator
    public ConnectorArgumentsHealthentiaPersons(
            @JsonProperty("url") String url, 
            @JsonProperty("username") String username, 
            @JsonProperty("password") String password, 
            @JsonProperty("datePattern") String datePattern,
            @JsonProperty("timePattern")  String timePattern,
            @JsonProperty("studies") List<Integer> studies) {
        super(ConnectorTypeEnum.HEALTHENTIA_PERSONS, datePattern, timePattern, null);
        this.url = url;
        this.username = username;
        this.password = password;
        this.studies = studies;
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

    public List<Integer> getStudies() {
        return studies;
    }
    
    @Override
    public String toString() {
        return "ConnectorArgumentsHealthentia{" + "url=" + url + ", username=" + username + ", password=" + password + '}';
    }
    
}
