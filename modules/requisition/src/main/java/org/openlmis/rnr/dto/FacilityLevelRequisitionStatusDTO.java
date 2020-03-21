package org.openlmis.rnr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacilityLevelRequisitionStatusDTO {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String token_type;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private String expireIn;

    @JsonProperty("scope")
    private String scope;
}
